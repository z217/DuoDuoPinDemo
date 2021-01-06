package com.duoduopin.service;

import com.duoduopin.bean.ShareBill;
import com.duoduopin.bean.ShareBillWithDistance;
import com.duoduopin.config.BillType;
import com.duoduopin.config.Constants;
import com.duoduopin.dao.ShareBillMapper;
import com.duoduopin.manager.Spatial4jManager;
import com.duoduopin.pojo.SearchPOJO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * ShareBill服务层
 *
 * @author z217
 * @date 2020/08/14
 */
@Service
@Slf4j
public class ShareBillService {
  @Autowired
  private ShareBillMapper shareBillMapper;
  @Autowired
  private Spatial4jManager spatial4jManager;
  
  public Long createShareBill(
    long userId,
    BillType type,
    String description,
    String address,
    Timestamp time,
    int curPeople,
    int maxPeople,
    BigDecimal price,
    double longitude,
    double latitude) {
    String geohash = spatial4jManager.getGeohash(longitude, latitude);
    ShareBill shareBill =
      new ShareBill(
        userId,
        type,
        description,
        address,
        time,
        curPeople,
        maxPeople,
        price,
        longitude,
        latitude,
        geohash);
    shareBillMapper.insertShareBill(shareBill);
    log.info(
      "A new sharebill is created by "
        + userId
        + ", exec in ShareBillService.createShareBill().");
    return shareBillMapper.getLastInsertId();
  }

  public ShareBill getShareBillByBillId(long billId) {
    return shareBillMapper.getShareBillByBillId(billId);
  }

  public List<ShareBill> getShareBillsByUserId(long userId) {
    return shareBillMapper.getShareBillsByUserId(userId);
  }
  
  public List<ShareBillWithDistance> getShareBillBySearchInfo(SearchPOJO info) {
    if (info.getDistance() != null && info.getLongitude() != null && info.getLatitude() != null)
      info.getDistance().setGeohashs(info.getLongitude(), info.getLatitude(), info.getGeohashs());
    String[] descriptions = null;
    if (info.getDescription() != null) descriptions = info.getDescription().split(" ");
    List<ShareBill> shareBills =
      shareBillMapper.getShareBillsBySearchInfo(
        info.getType(),
        descriptions,
        info.getStartTime(),
        info.getEndTime(),
        info.getMinPrice(),
        info.getMaxPrice(),
        info.getGeohashs(),
        info.getDistance());
    log.info(
      "Sharebills are got by searchinfo, exec in ShareBillService.getShareBillBySearchInfo()");
    List<ShareBillWithDistance> shareBillWithDistances = new LinkedList<>();
    for (ShareBill shareBill : shareBills) {
      shareBillWithDistances.add(
        new ShareBillWithDistance(
          shareBill,
          spatial4jManager.getDistance(
            shareBill.getLongitude(),
            shareBill.getLatitude(),
            info.getLongitude(),
            info.getLatitude())));
    }
    if (info.getDistance() != null) {
      info.getDistance().distanceFilter(shareBillWithDistances);
      log.info("Sharebills are filtered, exec in ShareBillService.getShareBillBySearchInfo()");
    }
    return shareBillWithDistances;
  }

  public boolean deleteShareBill(long billId, long userId) {
    if (Arrays.binarySearch(Constants.ADMIN_ID, userId) < 0
      && shareBillMapper.getUserIdByBillId(billId) != userId) {
      log.warn(
        "An unauthorized delete is done by "
          + userId
          + ", exec in ShareBillService.deleteShareBill().");
      return false;
    }
    shareBillMapper.deleteShareBill(billId);
    log.info("A ShareBill is deleted, exec in ShareBillService.deleteShareBill().");
    return true;
  }
}
