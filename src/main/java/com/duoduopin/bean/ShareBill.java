package com.duoduopin.bean;

import com.duoduopin.config.BillType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 拼单实体类
 *
 * @author z217
 * @date 2020/08/07
 */
@Getter
@Setter
@NoArgsConstructor
public class ShareBill {
  protected long billId;
  protected long userId;
  protected BillType type;
  protected String description;
  protected String address;
  protected Timestamp time;
  protected int curPeople;
  protected int maxPeople;
  protected BigDecimal price;
  protected double longitude;
  protected double latitude;
  protected String geohash;
  
  public ShareBill(ShareBill shareBill) {
    this.billId = shareBill.billId;
    this.userId = shareBill.userId;
    this.type = shareBill.type;
    this.description = shareBill.description;
    this.address = shareBill.address;
    this.time = shareBill.time;
    this.curPeople = shareBill.curPeople;
    this.maxPeople = shareBill.maxPeople;
    this.price = shareBill.price;
    this.longitude = shareBill.longitude;
    this.latitude = shareBill.latitude;
    this.geohash = shareBill.geohash;
  }
  
  public ShareBill(
    long userId,
    BillType type,
    String description,
    String address,
    Timestamp time,
    int curPeople,
    int maxPeople,
    BigDecimal price,
    double longitude,
    double latitude,
    String geohash) {
    this.userId = userId;
    this.type = type;
    this.description = description;
    this.address = address;
    this.time = time;
    this.price = price;
    this.curPeople = curPeople;
    this.maxPeople = maxPeople;
    this.longitude = longitude;
    this.latitude = latitude;
    this.geohash = geohash;
  }
  
  public ShareBill(
    long billId,
    long userId,
    BillType type,
    String description,
    String address,
    Timestamp time,
    int curPeople,
    int maxPeople,
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
    this.curPeople = curPeople;
    this.maxPeople = maxPeople;
    this.price = price;
    this.longitude = longitude;
    this.latitude = latitude;
    this.geohash = geohash;
  }
}
