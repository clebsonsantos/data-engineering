package com.clebsonsantos.backend.job;

import java.math.BigDecimal;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.clebsonsantos.backend.entity.Transactions;
import com.clebsonsantos.backend.entity.TypeTransaction;
import com.clebsonsantos.backend.entity.TransactionCNAB;

@Configuration
public class BatchConfig {
  private PlatformTransactionManager transactionManager;
  private JobRepository jobRepository;

  public BatchConfig(PlatformTransactionManager transactionManager, JobRepository jobRepository) {
    this.transactionManager = transactionManager;
    this.jobRepository = jobRepository;
  }

  @Bean
  Job job(Step step) {
    return new JobBuilder("job", this.jobRepository)
        .start(step)
        .incrementer(new RunIdIncrementer())
        .build();
  }

  @Bean
  Step step(ItemReader<TransactionCNAB> reader, ItemProcessor<TransactionCNAB, Transactions> processor,
      ItemWriter<Transactions> writer) {
    return new StepBuilder("step", this.jobRepository)
        .<TransactionCNAB, Transactions>chunk(1000, this.transactionManager)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .build();
  }

  @StepScope
  @Bean
  FlatFileItemReader<TransactionCNAB> reader(
      @Value("#{jobParameters['cnabFile']}") Resource resource) {
    return new FlatFileItemReaderBuilder<TransactionCNAB>()
        .name("reader")
        .resource(resource)
        .fixedLength()
        .columns(new Range(1, 1), new Range(2, 9), new Range(10, 19), new Range(20, 30),
            new Range(31, 42), new Range(43, 48), new Range(49, 62), new Range(63, 80))
        .names("type", "createdAt", "amount", "cpf", "card", "dateHour", "storeOwner", "storeName")
        .targetType(TransactionCNAB.class)
        .build();
  }

  @Bean
  ItemProcessor<TransactionCNAB, Transactions> processor() {
    return item -> {
      var typeTransaction = TypeTransaction.findByType(item.type());
      var newAmount = item.amount().divide(new BigDecimal(100)).multiply(typeTransaction.getSignal());

      var transaction = new Transactions(
          null, item.type(), null,
          newAmount,
          item.cpf(), item.card(), null,
          item.storeOwner().trim(), item.storeName().trim())
          .withData(item.createdAt())
          .withHora(item.dateHour());

      return transaction;
    };
  }

  @Bean
  JdbcBatchItemWriter<Transactions> writer(DataSource dataSource) {
    return new JdbcBatchItemWriterBuilder<Transactions>()
        .dataSource(dataSource)
        .sql("""
              INSERT INTO transactions (
                type, created_at, amount, cpf, card,
                date_hour, store_owner, store_name
              ) VALUES (
                :type, :createdAt, :amount, :cpf, :card,
                :dateHour, :storeOwner, :storeName
              )
            """)
        .beanMapped()
        .build();
  }

  @Bean
  JobLauncher jobLauncherAsync(JobRepository jobRepository) throws Exception {
    var jobLauncher = new TaskExecutorJobLauncher();
    jobLauncher.setJobRepository(jobRepository);
    jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
    jobLauncher.afterPropertiesSet();
    return jobLauncher;
  }
}
