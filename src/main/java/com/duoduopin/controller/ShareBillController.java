package com.duoduopin.controller;

import com.duoduopin.annotation.Authorization;
import com.duoduopin.annotation.CurrentUser;
import com.duoduopin.bean.SearchInfo;
import com.duoduopin.bean.ShareBill;
import com.duoduopin.bean.ShareBillWithDistance;
import com.duoduopin.bean.User;
import com.duoduopin.config.BillType;
import com.duoduopin.config.ResultStatus;
import com.duoduopin.model.ResultModel;
import com.duoduopin.service.ShareBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 拼单控制器
 *
 * @author z217
 * @date 2020/08/14
 * @see com.duoduopin.service.ShareBillService
 */
@RestController
@RequestMapping("/ShareBill")
public class ShareBillController {
  @Autowired
  public ShareBillService shareBillService;
  
  @Authorization
  @PutMapping("/add")
  public ResponseEntity<ResultModel> addShareBill(
    @CurrentUser User user,
    @RequestParam BillType type,
    @RequestParam String description,
    @RequestParam String address,
    @RequestParam Timestamp time,
    @RequestParam int curPeople,
    @RequestParam int maxPeople,
    @RequestParam BigDecimal price,
    @RequestParam double longitude,
    @RequestParam double latitude) {
    shareBillService.createShareBill(
      user.getUserId(),
      type,
      description,
      address,
      time,
      curPeople,
      maxPeople,
      price,
      longitude,
      latitude);
    return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
  }
  
  @PostMapping("/{id}")
  public ShareBill getShareBill(@PathVariable("id") long billId) {
    return shareBillService.getShareBillByBillId(billId);
  }
  
  @PostMapping("/info")
  public List<ShareBillWithDistance> getShareBillBySearchInfo(SearchInfo info) {
    return shareBillService.getShareBillBySearchInfo(info);
  }
  
  @PostMapping("/user/{id}")
  public List<ShareBill> getShareBillsByUesrId(@PathVariable("id") long userId) {
    return shareBillService.getShareBillsByUserId(userId);
  }
  
  @Authorization
  @DeleteMapping("/del/{id}")
  public ResponseEntity<ResultModel> deleteShareBill(
    @PathVariable("id") long billId, @CurrentUser User user) {
    if (!shareBillService.deleteShareBill(billId, user.getUserId()))
      return new ResponseEntity<>(
        ResultModel.error(ResultStatus.UNAUTHORITY), HttpStatus.UNAUTHORIZED);
    return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
  }
}
