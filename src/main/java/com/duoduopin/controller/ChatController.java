package com.duoduopin.controller;

import com.duoduopin.annotation.Authorization;
import com.duoduopin.annotation.CurrentUser;
import com.duoduopin.bean.User;
import com.duoduopin.config.ResultStatus;
import com.duoduopin.model.ResultModel;
import com.duoduopin.service.ChatService;
import com.duoduopin.service.ShareBillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description 聊天控制器
 * @author z217
 * @date 2021/01/18
 * @see com.duoduopin.service.ChatService
 * @see com.duoduopin.service.ShareBillService
 */
@RestController
@Slf4j
@RequestMapping(value = "/chat", produces = "application/json;charset=UTF-8")
public class ChatController {
  @Autowired private ChatService chatService;
  @Autowired private ShareBillService shareBillService;

  @Authorization
  @PostMapping("/{id}")
  public ResponseEntity<ResultModel> getChatMessageByBillId(
      @CurrentUser User user, @PathVariable("id") long billId) {
    if (!shareBillService.isTeamMember(user.getUserId(), billId)) {
      return new ResponseEntity<>(
          ResultModel.error(ResultStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
    }
    return new ResponseEntity<>(
        ResultModel.ok(chatService.getChatMessageByBillId(billId)), HttpStatus.OK);
  }

  @Authorization
  @PostMapping("/{billId}/{userId}")
  public ResponseEntity<ResultModel> getChatMessageByUserId(
      @CurrentUser User user,
      @PathVariable("billId") long billId,
      @PathVariable("userId") long userId) {
    if (!shareBillService.isTeamMember(user.getUserId(), billId)) {
      return new ResponseEntity<>(
          ResultModel.error(ResultStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
    }
    return new ResponseEntity<>(
        ResultModel.ok(chatService.getChatMessageByBillIdAndUserId(billId, userId)), HttpStatus.OK);
  }
}
