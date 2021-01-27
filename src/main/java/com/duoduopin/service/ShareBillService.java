package com.duoduopin.service;

import com.duoduopin.bean.*;
import com.duoduopin.config.BillType;
import com.duoduopin.config.DuoDuoPinUtils;
import com.duoduopin.dao.*;
import com.duoduopin.manager.Spatial4jManager;
import com.duoduopin.pojo.SearchPOJO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * ShareBill服务层
 *
 * @author z217
 * @date 2021/01/18
 */
@Service
@Slf4j
public class ShareBillService {
  @Autowired
  private ShareBillMapper shareBillMapper;
  @Autowired
  private Spatial4jManager spatial4jManager;
  @Autowired
  private UserMapper userMapper;
  @Autowired
  private TeamMemberMapper teamMemberMapper;
  @Autowired
  private ChatMessageMapper chatMessageMapper;
  @Autowired
  private SystemMessageMapper systemMessageMapper;
  
  public Long createShareBill(
    long userId,
    String title,
    BillType type,
    String description,
    String address,
    Timestamp time,
    int curPeople,
    int maxPeople,
    BigDecimal price,
    double longitude,
    double latitude) {
    if (longitude < -180.0 || longitude > 180.0 || latitude < -90.0 || latitude > 90.0) return null;
    String geohash = spatial4jManager.getGeohash(longitude, latitude);
    ShareBill shareBill =
      new ShareBill(
        userId,
        title,
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
    if (shareBill.getBillId() == 0) {
      log.warn(userId + " create share bill failed, exec in ShareBillService.createShareBill().");
      return null;
    }
    if (teamMemberMapper.joinTeam(shareBill.getBillId(), userId) == 0) {
      log.warn(
        userId
          + " join team failed, delete share bill, exec in ShareBillService.createShareBill().");
      shareBillMapper.deleteShareBill(shareBill.getBillId());
      return null;
    }
    log.info(
      "A new sharebill is created by "
        + userId
        + ", exec in ShareBillService.createShareBill().");
    return shareBill.getBillId();
  }

  public List<ShareBill> getShareBillByBillId(long billId) {
    List<ShareBill> shareBills = new ArrayList<>();
    ShareBill bill = shareBillMapper.getShareBillByBillId(billId);
    if (bill != null) {
      shareBills.add(setShareBillNickname(bill));
    }
    return shareBills;
  }

  public List<ShareBill> getShareBillsByUserId(long userId) {
    List<ShareBill> shareBills = shareBillMapper.getShareBillsByUserId(userId);
    setShareBillNickname(shareBills);
    return shareBills;
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
          setShareBillNickname(shareBill),
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
    if (!DuoDuoPinUtils.checkIfAdmin(userId)
      && shareBillMapper.getUserIdByBillId(billId) != userId) {
      log.warn(
        "An unauthorized delete is requested by "
          + userId
          + ", exec in ShareBillService.deleteShareBill().");
      return false;
    }
    shareBillMapper.deleteShareBill(billId);
    log.info("A ShareBill is deleted, exec in ShareBillService.deleteShareBill().");
    return true;
  }
  
  public boolean isTeamMember(long userId, long billId) {
    List<TeamMember> members = teamMemberMapper.getTeamMemberByBillId(billId);
    for (TeamMember member : members) if (member.getUserId() == userId) return true;
    if (DuoDuoPinUtils.checkIfAdmin(userId)) return true;
    return false;
  }
  
  public boolean isApplicable(long billId) {
    ShareBill shareBill = shareBillMapper.getShareBillPeopleByBillId(billId);
    return shareBill.getMaxPeople() - shareBill.getCurPeople() != 0;
  }
  
  public boolean isLeader(long billId, long userId) {
    return userId == shareBillMapper.getUserIdByBillId(billId);
  }
  
  @Async
  public void joinTeam(long billId, long userId) {
    ShareBill shareBill = shareBillMapper.getShareBillPeopleByBillId(billId);
    shareBillMapper.updateShareBillCurPeople(billId, shareBill.getCurPeople() + 1);
    teamMemberMapper.joinTeam(billId, userId);
    chatMessageMapper.createChatMessage(
      userId,
      billId,
      ChatMessage.MessageType.JOIN,
      Timestamp.valueOf(LocalDateTime.now()),
      userMapper.getNickNameByUserId(userId) + "加入了小组");
  }
  
  @Async
  public void quitTeam(User user, long billId, long userId) {
    ShareBill shareBill = shareBillMapper.getShareBillPeopleByBillId(billId);
    if (shareBill.getUserId() == userId) {
      //      组长退出自动解散
      shareBillMapper.deleteShareBill(billId);
      return;
    }
    shareBillMapper.updateShareBillCurPeople(billId, shareBill.getCurPeople() - 1);
    teamMemberMapper.quitTeam(billId, userId);
    String nickname = userMapper.getNickNameByUserId(userId);
    String content = "";
    if (user.getUserId() == userId) content = nickname + "退出了小组";
    else content = nickname + "被" + user.getNickname() + "请出了小组";
    chatMessageMapper.createChatMessage(
      userId,
      billId,
      ChatMessage.MessageType.QUIT,
      Timestamp.valueOf(LocalDateTime.now()),
      content);
  }
  
  public Long getUserIdByBillId(long billId) {
    return shareBillMapper.getUserIdByBillId(billId);
  }
  
  private ShareBill setShareBillNickname(ShareBill shareBill) {
    shareBill.setNickname(userMapper.getUserById(shareBill.getUserId()).getNickname());
    return shareBill;
  }
  
  private void setShareBillNickname(List<? extends ShareBill> shareBills) {
    for (ShareBill shareBill : shareBills) {
      shareBill.setNickname(userMapper.getUserById(shareBill.getUserId()).getNickname());
    }
  }
}
