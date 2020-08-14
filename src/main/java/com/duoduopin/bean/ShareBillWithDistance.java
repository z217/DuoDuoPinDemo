package com.duoduopin.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 带距离参数的拼单类
 *
 * @author z217
 * @date 2020/08/14
 */
@Getter
@Setter
public class ShareBillWithDistance extends ShareBill {
  private double distance;

  public ShareBillWithDistance(ShareBill shareBill, double distance) {
    super(shareBill);
    this.distance = distance;
  }
}
