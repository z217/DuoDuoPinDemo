package com.duoduopin.controller;

import com.duoduopin.annotation.Administrator;
import com.duoduopin.annotation.Authorization;
import com.duoduopin.annotation.CurrentUser;
import com.duoduopin.bean.SystemMessage;
import com.duoduopin.bean.User;
import com.duoduopin.model.ResultModel;
import com.duoduopin.pojo.SystemMessagePOJO;
import com.duoduopin.service.SystemMessageService;
import com.duoduopin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author z217
 * @description 系统消息控制器
 * @date 2021/03/23
 * @see com.duoduopin.service.SystemMessageService
 * @see com.duoduopin.service.UserService
 * @see com.duoduopin.controller.SystemMessageWebSocket
 */
@RestController
@Slf4j
@RequestMapping(value = "/system", produces = "application/json;charset=UTF-8")
public class SystemMessageController {
  @Autowired private SystemMessageService systemMessageService;
  @Autowired private UserService userService;

  @Administrator
  @PutMapping("/broad")
  public ResponseEntity<ResultModel> createBroadSystemMessage(
      @RequestBody SystemMessagePOJO systemMessagePOJO) {
    SystemMessage systemMessage =
        new SystemMessage(
            systemMessagePOJO.getSenderId(),
            systemMessagePOJO.getReceiverId(),
            systemMessagePOJO.getBillId(),
            SystemMessage.MessageType.BROAD,
            Timestamp.valueOf(LocalDateTime.now()),
            systemMessagePOJO.getContent());
    systemMessageService.broadSystemMessage(systemMessage);
    return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
  }

  @Authorization
  @PostMapping("/check")
  public ResponseEntity<ResultModel> getSystemMessagesByUser(@CurrentUser User user) {
    List<SystemMessage> messages =
      systemMessageService.getSystemMessagesByUserIdAndLastOnline(
        user.getUserId(), user.getLastOnline());
    userService.updateLastOnlineByUserId(user.getUserId());
    return new ResponseEntity<>(ResultModel.ok(messages), HttpStatus.OK);
  }

  @GetMapping("/currentConnection")
  public long getCurrentConnection() {
    return SystemMessageWebSocket.getConcurrentConnection();
  }
}
