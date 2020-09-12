package com.duoduopin.controller;

import com.duoduopin.annotation.Authorization;
import com.duoduopin.annotation.CurrentUser;
import com.duoduopin.bean.ShareBill;
import com.duoduopin.bean.ShareBillWithDistance;
import com.duoduopin.bean.User;
import com.duoduopin.config.ResultStatus;
import com.duoduopin.model.ResultModel;
import com.duoduopin.pojo.AddShareBillPOJO;
import com.duoduopin.pojo.SearchPOJO;
import com.duoduopin.service.ShareBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 拼单控制器
 *
 * @author z217
 * @date 2020/08/14
 * @see com.duoduopin.service.ShareBillService
 */
@CrossOrigin("*")
@RestController
@RequestMapping(value = "/ShareBill", produces = "application/json;charset=UTF-8")
public class ShareBillController {
  @Autowired
  public ShareBillService shareBillService;
  
  @Authorization
  @PutMapping("/add")
  public ResponseEntity<ResultModel> addShareBill(
    @CurrentUser User user, @RequestBody AddShareBillPOJO shareBill) {
    shareBillService.createShareBill(
      user.getUserId(),
      shareBill.getType(),
      shareBill.getDescription(),
      shareBill.getAddress(),
      shareBill.getTime(),
      1,
      shareBill.getMaxPeople(),
      shareBill.getPrice(),
      shareBill.getLongitude(),
      shareBill.getLatitude());
    return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
  }

  @PostMapping("/{id}")
  public ShareBill getShareBill(@PathVariable("id") long billId) {
    return shareBillService.getShareBillByBillId(billId);
  }
  
  @PostMapping("/info")
  public List<ShareBillWithDistance> getShareBillBySearchInfo(@RequestBody SearchPOJO info) {
    return shareBillService.getShareBillBySearchInfo(info);
  }
  
  @PostMapping("/user/{id}")
  public List<ShareBill> getShareBillsByUserId(@PathVariable("id") long userId) {
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
