package com.clebsonsantos.backend.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

public record Transactions(
    @Id Long id,
    Integer type,
    @Column("CREATED_AT") Date createdAt,
    BigDecimal amount,
    Long cpf,
    String card,
    @Column("DATE_HOUR") Time dateHour,
    @Column("STORE_OWNER") String storeOwner,
    @Column("STORE_NAME") String storeName) {

  public Transactions withData(String date) throws ParseException {
    var dateFormat = new SimpleDateFormat("yyyyMMdd");
    var dateTime = dateFormat.parse(date);

    return new Transactions(
        id, type, new Date(dateTime.getTime()), amount, cpf,
        card, dateHour, storeOwner, storeName);
  }

  public Transactions withHora(String dateHour) throws ParseException {
    var dateFormat = new SimpleDateFormat("HHmmss");
    var hour = dateFormat.parse(dateHour);

    return new Transactions(
        id, type, createdAt, amount, cpf,
        card, new Time(hour.getTime()), storeOwner, storeName);
  }
}