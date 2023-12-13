package com.clebsonsantos.backend.domain;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public record Transaction(
    Long id,
    Integer type,
    Date date,
    BigDecimal amount,
    Long cpf,
    String card,
    Time hour,
    String storeOwner,
    String storeName) {

  public Transaction withData(String date) throws ParseException {
    var dateFormat = new SimpleDateFormat("yyyyMMdd");
    var dateTime = dateFormat.parse(date);

    return new Transaction(
        id, type, new Date(dateTime.getTime()), amount, cpf,
        card, hour, storeOwner, storeName);
  }

  public Transaction withHora(String hour) throws ParseException {
    var dateFormat = new SimpleDateFormat("HHmmss");
    var dateHour = dateFormat.parse(hour);

    return new Transaction(
        id, type, date, amount, cpf,
        card, new Time(dateHour.getTime()), storeOwner, storeName);
  }
}