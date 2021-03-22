package com.duoduopin.controller;

import com.duoduopin.annotation.Authorization;
import com.duoduopin.annotation.CurrentUser;
import com.duoduopin.bean.*;
import com.duoduopin.config.ResultStatus;
import com.duoduopin.model.ResultModel;
import com.duoduopin.pojo.AddShareBillPOJO;
import com.duoduopin.pojo.SearchPOJO;
import com.duoduopin.service.ShareBillService;
import com.duoduopin.service.SystemMessageService;
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
 * @date 2021/03/22
 * @see com.duoduopin.service.ShareBillService
 * @see com.duoduopin.service.SystemMessageService
 * @see com.duoduopin.controller.SystemMessageWebSocket
 */
@Slf4j
@RestController
@RequestMapping(value = "/ShareBill", produces = "application/json;charset=UTF-8")
public class ShareBillController {
  @Autowired
  private ShareBillService shareBillService;
  @Autowired
  private SystemMessageService systemMessageService;
  
  @Authorization
  @PutMapping("/add")
  public ResponseEntity<ResultModel> addShareBill(
    @CurrentUser User user, @RequestBody AddShareBillPOJO shareBill) {
    if (shareBill.getMaxPeople() <= 1) {
      log.warn(
        user.getUsername()
          + " try to add an illegal sharebill, exec in ShareBillController.addShareBill().");
      return new ResponseEntity<>(
        ResultModel.error(ResultStatus.BILL_ILLEGAL), HttpStatus.BAD_REQUEST);
    }
    log.info(String.valueOf(shareBill));
    IdCarrier carrier = new IdCarrier();
    Long id =
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
        shareBill.getLatitude());
    if (id == null)
      return new ResponseEntity<>(
        ResultModel.error(ResultStatus.BILL_ILLEGAL), HttpStatus.BAD_REQUEST);
    carrier.setId(id);
    return new ResponseEntity<>(ResultModel.ok(carrier), HttpStatus.OK);
  }

  @PostMapping("/{id}")
  public ResponseEntity<ResultModel> getShareBill(@PathVariable("id") long billId) {
    List<ShareBill> shareBills = shareBillService.getShareBillByBillId(billId);
    return new ResponseEntity<>(ResultModel.ok(shareBills), HttpStatus.OK);
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
  
  @PostMapping("/team/{id}")
  public ResponseEntity<ResultModel> getTeamMemberByBillId(@PathVariable("id") long billId) {
    List<TeamMember> members = shareBillService.getTeamMember(billId);
    return new ResponseEntity<>(ResultModel.ok(members), HttpStatus.OK);
  }
  
  @Authorization
  @PostMapping("/apply/{id}/allow")
  public ResponseEntity<ResultModel> applyAllow(
    @CurrentUser User user, @PathVariable("id") long systemMessageId) {
    SystemMessage systemMessage = systemMessageService.getSystemMessageByMessageId(systemMessageId);
    if (systemMessage.getType() != SystemMessage.MessageType.APPLY)
      return new ResponseEntity<>(
        ResultModel.error(ResultStatus.BILL_NOT_FOUND), HttpStatus.BAD_REQUEST);
    if (systemMessage.getReceiverId() != user.getUserId())
      return new ResponseEntity<>(
        ResultModel.error(ResultStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
    List<ShareBill> shareBills = shareBillService.getShareBillByBillId(systemMessage.getBillId());
    if (shareBills.size() == 0)
      return new ResponseEntity<>(
        ResultModel.error(ResultStatus.BILL_NOT_FOUND), HttpStatus.BAD_REQUEST);
    shareBillService.joinTeam(systemMessage.getBillId(), systemMessage.getSenderId());
    systemMessageService.createSystemMessage(
      user.getUserId(),
      systemMessage.getSenderId(),
      systemMessage.getBillId(),
      SystemMessage.MessageType.ALLOW);
    systemMessageService.deleteSystemMessageByMessageId(systemMessageId);
    return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
  }
  
  @Authorization
  @PostMapping("/apply/{id}/reject")
  public ResponseEntity<ResultModel> applyReject(
    @CurrentUser User user, @PathVariable("id") long systemMessageId) {
    SystemMessage systemMessage = systemMessageService.getSystemMessageByMessageId(systemMessageId);
    if (systemMessage.getType() != SystemMessage.MessageType.APPLY)
      return new ResponseEntity<>(
        ResultModel.error(ResultStatus.BILL_NOT_FOUND), HttpStatus.BAD_REQUEST);
    if (systemMessage.getReceiverId() != user.getUserId())
      return new ResponseEntity<>(
        ResultModel.error(ResultStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
    systemMessageService.createSystemMessage(
      user.getUserId(),
      systemMessage.getSenderId(),
      systemMessage.getBillId(),
      SystemMessage.MessageType.REJEC);
    systemMessageService.deleteSystemMessageByMessageId(systemMessageId);
    return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
  }
  
  @Authorization
  @PutMapping("/join/{bill_id}")
  public ResponseEntity<ResultModel> joinTeam(
    @CurrentUser User user, @PathVariable("bill_id") long billId) {
    if (shareBillService.isTeamMember(user.getUserId(), billId)
      || systemMessageService.isDuplicateApply(user.getUserId(), billId))
      return new ResponseEntity<>(
        ResultModel.error(ResultStatus.DUPLICATE_JOIN), HttpStatus.BAD_REQUEST);
    if (!shareBillService.isApplicable(billId))
      return new ResponseEntity<>(
        ResultModel.error(ResultStatus.JOIN_FAILED), HttpStatus.BAD_REQUEST);
    systemMessageService.createSystemMessage(
      user.getUserId(),
      shareBillService.getUserIdByBillId(billId),
      billId,
      SystemMessage.MessageType.APPLY);
    return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
  }
  
  @Authorization
  @DeleteMapping("/quit/{bill_id}/{user_id}")
  public ResponseEntity<ResultModel> quitTeam(
    @CurrentUser User user,
    @PathVariable("bill_id") long billId,
    @PathVariable("user_id") long userId) {
    if (user.getUserId() != userId && !shareBillService.isLeader(billId, user.getUserId())) {
      return new ResponseEntity<>(
        ResultModel.error(ResultStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
    }
    shareBillService.quitTeam(user, billId, userId);
    return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
  }
  
  @Authorization
  @DeleteMapping("/del/{id}")
  public ResponseEntity<ResultModel> deleteShareBill(
    @PathVariable("id") long billId, @CurrentUser User user) {
    if (!shareBillService.deleteShareBill(billId, user.getUserId()))
      return new ResponseEntity<>(
        ResultModel.error(ResultStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
    return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
  }
}
