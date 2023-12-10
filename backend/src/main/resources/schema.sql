CREATE TABLE IF NOT EXISTS transactions (
  id SERIAL primary key,
  type int,
  "date" date,
  amount decimal,
  cpf bigint,
  card varchar(255),
  "hour" time,
  store_owner varchar(255),
  store_name varchar(255)
);