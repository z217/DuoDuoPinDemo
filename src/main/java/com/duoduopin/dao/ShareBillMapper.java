package com.duoduopin.dao;

import com.duoduopin.bean.ShareBill;
import com.duoduopin.config.BillType;
import com.duoduopin.pojo.SearchPOJO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author z217
 * @description 拼单映射类
 * @date 2021/01/20
 */
public interface ShareBillMapper {
  public int updateShareBillCurPeople(
    @Param("billId") long billId, @Param("curPeople") int curPeople);
  
  public int insertShareBill(ShareBill shareBill);
  
  public List<ShareBill> getShareBillsByUserId(long userId);
  
  public ShareBill getShareBillByBillId(long billId);
  
  //  获取当前拼单人数信息
  public ShareBill getShareBillPeopleByBillId(long billId);
  
  public Long getUserIdByBillId(Long billId);
  
  public Long getLastInsertId();
  
  public List<ShareBill> getShareBillsBySearchInfo(
    @Param("type") BillType type,
    @Param("descriptions") String[] descriptions,
    @Param("startTime") Timestamp startTime,
    @Param("endTime") Timestamp endTime,
    @Param("minPrice") BigDecimal minPrice,
    @Param("maxPrice") BigDecimal maxPrice,
    @Param("geohashs") String[] geohashs,
    @Param("distance") SearchPOJO.Distance distance);
  
  public int deleteShareBill(long billId);
}
