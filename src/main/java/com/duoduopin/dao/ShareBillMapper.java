package com.duoduopin.dao;

import com.duoduopin.bean.SearchInfo;
import com.duoduopin.bean.ShareBill;
import com.duoduopin.config.BillType;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 拼单映射类
 *
 * @author z217
 * @date 2020/08/07
 */
public interface ShareBillMapper {
  public void insertShareBill(ShareBill shareBill);
  
  public List<ShareBill> getShareBillsByUserId(long userId);
  
  public ShareBill getShareBillByBillId(long billId);
  
  public Long getUserIdByBillId(Long billId);
  
  public List<ShareBill> getShareBillsBySearchInfo(
    @Param("type") BillType type,
    @Param("descriptions") String[] descriptions,
    @Param("startTime") Timestamp startTime,
    @Param("endTime") Timestamp endTime,
    @Param("minPrice") BigDecimal minPrice,
    @Param("maxPrice") BigDecimal maxPrice,
    @Param("geohashs") String[] geohashs,
    @Param("distance") SearchInfo.Distance distance);
  
  public void deleteShareBill(long billId);
}
