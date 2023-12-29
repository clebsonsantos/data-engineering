CREATE TABLE IF NOT EXISTS transactions (
  id SERIAL primary key,
  type int,
  created_at date,
  amount decimal,
  cpf bigint,
  card varchar(255),
  date_hour time,
  store_owner varchar(255),
  store_name varchar(255)
);