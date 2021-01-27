package com.duoduopin.service;

import com.duoduopin.bean.ChatMessage;
import com.duoduopin.bean.TeamMember;
import com.duoduopin.dao.ChatMessageMapper;
import com.duoduopin.dao.TeamMemberMapper;
import com.duoduopin.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

/**
 * @author z217
 * @description 聊天服务层
 * @date 2021/01/26
 * @see com.duoduopin.dao.ChatMessageMapper
 * @see com.duoduopin.dao.TeamMemberMapper
 * @see com.duoduopin.dao.UserMapper
 */
@Service
public class ChatService {
  @Autowired ChatMessageMapper chatMessageMapper;

  @Autowired TeamMemberMapper teamMemberMapper;

  @Autowired UserMapper userMapper;

  @Async
  public void createChatMessage(ChatMessage chatMessage) {
    chatMessageMapper.createChatMessage(
        chatMessage.getUserId(),
        chatMessage.getBillId(),
        chatMessage.getType(),
        chatMessage.getTime(),
        chatMessage.getContent());
  }

  public List<ChatMessage> getChatMessageByBillId(long billId) {
    List<ChatMessage> messages = chatMessageMapper.getChatMessageByBillId(billId);
    HashMap<Long, String> memberNickname = getTeamMemberNickname(billId);
    for (ChatMessage message : messages) {
      message.setNickname(
        memberNickname.getOrDefault(
          message.getUserId(), userMapper.getNickNameByUserId(message.getUserId())));
    }
    return messages;
  }

  public List<ChatMessage> getChatMessageByUserId(long userId) {
    List<ChatMessage> messages = chatMessageMapper.getChatMessageByUserId(userId);
    String nickname = userMapper.getNickNameByUserId(userId);
    for (ChatMessage message : messages) message.setNickname(nickname);
    return messages;
  }
  
  public List<ChatMessage> getChatMessageByBillIdAndUserId(long billId, long userId) {
    List<ChatMessage> messages = chatMessageMapper.getChatMessageByBillIdAndUserId(billId, userId);
    String nickname = userMapper.getNickNameByUserId(userId);
    for (ChatMessage message : messages) message.setNickname(nickname);
    return messages;
  }
  
  public List<ChatMessage> getUncheckedChatMessage(long billId, long userId) {
    Timestamp lastOnline = userMapper.getLastOnlineByUesrId(userId);
    userMapper.updateLastOnlineByUserId(userId);
    List<ChatMessage> chatMessages =
      chatMessageMapper.getChatMessageByBillIdAndLastOnline(billId, lastOnline);
    HashMap<Long, String> memberNickname = getTeamMemberNickname(billId);
    for (ChatMessage message : chatMessages) {
      message.setNickname(
        memberNickname.getOrDefault(
          message.getUserId(), userMapper.getNickNameByUserId(message.getUserId())));
    }
    return chatMessages;
  }
  
  private HashMap<Long, String> getTeamMemberNickname(long billId) {
    List<TeamMember> members = teamMemberMapper.getTeamMemberByBillId(billId);
    HashMap<Long, String> memberNickname = new HashMap<>();
    for (TeamMember member : members)
      memberNickname.put(member.getUserId(), userMapper.getNickNameByUserId(member.getUserId()));
    return memberNickname;
  }
}
