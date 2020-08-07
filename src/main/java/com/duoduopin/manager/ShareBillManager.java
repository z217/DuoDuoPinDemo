package com.duoduopin.manager;

import com.duoduopin.bean.ShareBill;
import com.duoduopin.config.BillType;

import java.math.BigDecimal;
import java.sql.Timestamp;

public interface ShareBillManager {
  /**
   * 创建一个拼单，通过Redis计算Geohash值
   *
   * @param userId 用户id
   * @param type 拼单类型
   * @param description 拼单描述
   * @param address 地址
   * @param time 时间
   * @param price 预计价格
   * @param longitude 经度
   * @param latitude 纬度
   * @return 拼单
   */
  public ShareBill createShareBill(
      long userId,
      BillType type,
      String description,
      String address,
      Timestamp time,
      BigDecimal price,
      double longitude,
      double latitude);
}
