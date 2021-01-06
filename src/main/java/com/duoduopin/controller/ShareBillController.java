package com.duoduopin.controller;

import com.duoduopin.annotation.Authorization;
import com.duoduopin.annotation.CurrentUser;
import com.duoduopin.bean.IdCarrier;
import com.duoduopin.bean.ShareBill;
import com.duoduopin.bean.ShareBillWithDistance;
import com.duoduopin.bean.User;
import com.duoduopin.config.ResultStatus;
import com.duoduopin.model.ResultModel;
import com.duoduopin.pojo.AddShareBillPOJO;
import com.duoduopin.pojo.SearchPOJO;
import com.duoduopin.service.ShareBillService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RestController
@RequestMapping(value = "/ShareBill", produces = "application/json;charset=UTF-8")
public class ShareBillController {
  @Autowired
  public ShareBillService shareBillService;
  
  @Authorization
  @PutMapping("/add")
  public ResponseEntity<ResultModel> addShareBill(
    @CurrentUser User user, @RequestBody AddShareBillPOJO shareBill) {
    log.info(String.valueOf(shareBill));
    IdCarrier carrier = new IdCarrier();
    carrier.setId(
      shareBillService.createShareBill(
        user.getUserId(),
        shareBill.getTitle(),
        shareBill.getType(),
        shareBill.getDescription(),
        shareBill.getAddress(),
        shareBill.getTime(),
        1,
        shareBill.getMaxPeople(),
        shareBill.getPrice(),
        shareBill.getLongitude(),
        shareBill.getLatitude()));
    return new ResponseEntity<>(ResultModel.ok(carrier), HttpStatus.OK);
  }
  
  @PostMapping("/{id}")
  public ResponseEntity<ResultModel> getShareBill(@PathVariable("id") long billId) {
    ShareBill shareBill = shareBillService.getShareBillByBillId(billId);
    if (shareBill == null)
      return new ResponseEntity<>(
        ResultModel.error(ResultStatus.BILL_NOT_FOUND), HttpStatus.NOT_FOUND);
    return new ResponseEntity<>(ResultModel.ok(shareBill), HttpStatus.OK);
  }
  
  @PostMapping("/info")
  public ResponseEntity<ResultModel> getShareBillBySearchInfo(@RequestBody SearchPOJO info) {
    List<ShareBillWithDistance> shareBills = shareBillService.getShareBillBySearchInfo(info);
    return new ResponseEntity<>(ResultModel.ok(shareBills), HttpStatus.OK);
  }
  
  @PostMapping("/user/{id}")
  public ResponseEntity<ResultModel> getShareBillsByUserId(@PathVariable("id") long userId) {
    List<ShareBill> shareBills = shareBillService.getShareBillsByUserId(userId);
    return new ResponseEntity<>(ResultModel.ok(shareBills), HttpStatus.OK);
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
