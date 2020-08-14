package com.duoduopin.dao;

import com.duoduopin.bean.SearchInfo;
import com.duoduopin.bean.ShareBill;

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
  
  public List<ShareBill> getShareBillsBySearchInfo(SearchInfo searchInfo);
  
  public void deleteShareBill(long billId);
}
