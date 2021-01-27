package com.duoduopin.service;

import com.duoduopin.bean.SystemMessage;
import com.duoduopin.controller.SystemMessageWebSocket;
import com.duoduopin.dao.ShareBillMapper;
import com.duoduopin.dao.SystemMessageMapper;
import com.duoduopin.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @description 系统消息服务层
 * @author z217
 * @date 2021/01/25
 * @see com.duoduopin.dao.SystemMessageMapper
 * @see com.duoduopin.dao.UserMapper
 * @see com.duoduopin.controller.SystemMessageWebSocket
 */
@Service
public class SystemMessageService {
  @Autowired SystemMessageMapper systemMessageMapper;
  @Autowired UserMapper userMapper;
  @Autowired ShareBillMapper shareBillMapper;

  public List<SystemMessage> getSystemMessagesByUserIdAndLastOnline(
      long userId, Timestamp lastOnline) {
    List<SystemMessage> messages =
        systemMessageMapper.getSystemMessageByUserIdAndLastOnline(userId, lastOnline);
    userMapper.updateLastOnlineByUserId(userId);
    return messages;
  }

  public SystemMessage getSystemMessageByMessageId(long messageId) {
    return systemMessageMapper.getSystemMessageByMessageId(messageId);
  }

  public boolean isDuplicateApply(long userId, long billId) {
    return systemMessageMapper.isApplySystemMessageExistsByUserIdAndBillId(userId, billId) != null;
  }

  @Async
  public void broadSystemMessage(SystemMessage systemMessage) {
    systemMessageMapper.insertSystemMessage(systemMessage);
    SystemMessageWebSocket.broadSystemMessage(systemMessage);
  }

  @Async
  public void sendSystemMessageByUserId(SystemMessage systemMessage) {
    systemMessageMapper.insertSystemMessage(systemMessage);
    SystemMessageWebSocket.sendToUser(systemMessage);
  }

  @Async
  public void createSystemMessage(
      Long senderId, Long receiverId, Long billId, SystemMessage.MessageType type) {
    SystemMessage systemMessage = null;
    switch (type) {
      case APPLY:
        systemMessage =
            new SystemMessage(
                senderId,
                receiverId,
                billId,
                type,
                Timestamp.valueOf(LocalDateTime.now()),
                userMapper.getNickNameByUserId(senderId)
                    + " 想要加入拼单小组“"
                    + shareBillMapper.getTitleByBillId(billId)
                    + "“。");
        break;
      case REJEC:
        systemMessage =
            new SystemMessage(
                senderId,
                receiverId,
                billId,
                type,
                Timestamp.valueOf(LocalDateTime.now()),
                "您的申请加入小组“" + shareBillMapper.getTitleByBillId(billId) + "“已被拒绝。");
        break;
      case ALLOW:
        systemMessage =
            new SystemMessage(
                senderId,
                receiverId,
                billId,
                type,
                Timestamp.valueOf(LocalDateTime.now()),
                "您的申请加入小组”" + shareBillMapper.getTitleByBillId(billId) + "已通过。");
        break;
    }
    systemMessageMapper.insertSystemMessage(systemMessage);
    SystemMessageWebSocket.sendToUser(systemMessage);
  }

  @Async
  public void deleteSystemMessageByMessageId(long messageId) {
    systemMessageMapper.deleteSystemMessageByMessageId(messageId);
  }
}
