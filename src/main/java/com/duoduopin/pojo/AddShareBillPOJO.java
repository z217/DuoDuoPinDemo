package com.duoduopin.pojo;

import com.duoduopin.config.BillType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
public class AddShareBillPOJO {
  private BillType type;
  private String title;
  private String description;
  private String address;
  private Timestamp time;
  private int maxPeople;
  private BigDecimal price;
  private double longitude;
  private double latitude;
}
