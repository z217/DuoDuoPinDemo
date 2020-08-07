package com.duoduopin.controller;

import com.duoduopin.annotation.Authorization;
import com.duoduopin.annotation.CurrentUser;
import com.duoduopin.bean.ShareBill;
import com.duoduopin.bean.User;
import com.duoduopin.config.BillType;
import com.duoduopin.config.ResultStatus;
import com.duoduopin.dao.ShareBillMapper;
import com.duoduopin.manager.ShareBillManager;
import com.duoduopin.model.ResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@RestController
@RequestMapping("/ShareBill")
public class ShareBillController {
  @Autowired public ShareBillMapper shareBillMapper;
  @Autowired public ShareBillManager shareBillManager;

  @Authorization
  @PutMapping("/add")
  public ResponseEntity<ResultModel> addShareBill(
      @CurrentUser User user,
      @RequestParam BillType type,
      @RequestParam String description,
      @RequestParam String address,
      @RequestParam Timestamp time,
      @RequestParam BigDecimal price,
      @RequestParam double longitude,
      @RequestParam double latitude) {
    ShareBill shareBill =
        shareBillManager.createShareBill(
            user.getUserId(), type, description, address, time, price, longitude, latitude);
    if (shareBill == null)
      return new ResponseEntity<>(ResultModel.error(ResultStatus.UNKOWN), HttpStatus.BAD_REQUEST);
    shareBillMapper.insertShareBill(shareBill);
    return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
  }

  @PostMapping("/{id}")
  public ShareBill getShareBill(@PathVariable("id") long billId) {
    return shareBillMapper.getShareBillByBillId(billId);
  }

  @Authorization
  @DeleteMapping("/del/{id}")
  public ResponseEntity<ResultModel> deleteShareBill(
      @PathVariable("id") long billId, @CurrentUser User user) {
    if (user.getUserId() != 1 && shareBillMapper.getUserIdByBillId(billId) != user.getUserId())
      return new ResponseEntity<>(
          ResultModel.error(ResultStatus.UNAUTHORITY), HttpStatus.UNAUTHORIZED);
    shareBillMapper.deleteShareBill(billId);
    return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
  }
}
