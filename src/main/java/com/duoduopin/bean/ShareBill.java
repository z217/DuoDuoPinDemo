package com.duoduopin.bean;

import com.duoduopin.config.BillType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
public class ShareBill {
  private long billId;
  private long userId;
  private BillType type;
  private String description;
  private String address;
  private Timestamp time;
  private BigDecimal price;
  private double longitude;
  private double latitude;
  private String geohash;

  public ShareBill(
      long userId,
      BillType type,
      String description,
      String address,
      Timestamp time,
      BigDecimal price,
      double longitude,
      double latitude) {
    this.userId = userId;
    this.type = type;
    this.description = description;
    this.address = address;
    this.time = time;
    this.price = price;
    this.longitude = longitude;
    this.latitude = latitude;
  }

  public ShareBill(
      long billId,
      long userId,
      BillType type,
      String description,
      String address,
      Timestamp time,
      BigDecimal price,
      double longitude,
      double latitude,
      String geohash) {
    this.billId = billId;
    this.userId = userId;
    this.type = type;
    this.description = description;
    this.address = address;
    this.time = time;
    this.price = price;
    this.longitude = longitude;
    this.latitude = latitude;
    this.geohash = geohash;
  }
}
