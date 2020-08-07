package com.duoduopin.dao;

import com.duoduopin.bean.ShareBill;

import java.util.List;

public interface ShareBillMapper {
  public void insertShareBill(ShareBill shareBill);

  public List<ShareBill> getShareBillsByUserId(long userId);

  public ShareBill getShareBillByBillId(long billId);

  public Long getUserIdByBillId(Long billId);
  
  public void deleteShareBill(long billId);
}
