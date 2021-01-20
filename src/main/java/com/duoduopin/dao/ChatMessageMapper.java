package com.duoduopin.dao;

import com.duoduopin.bean.ChatMessage;
import com.duoduopin.config.MessageType;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

/**
 * @description 消息映射类
 * @author z217
 * @date 2021/01/19
 */
public interface ChatMessageMapper {
  //  查询小组聊天记录
  List<ChatMessage> getChatMessageByBillId(long billId);

  //  查询用户发言
  List<ChatMessage> getChatMessageByUserId(long userId);
  
//  查询小组内用户发言
  List<ChatMessage> getChatMessageByBillIdAndUserId(@Param("billId") long billId, @Param("userId") long userId);
  
//  创建消息
  int createChatMessage(
    @Param("userId") long userId,
      @Param("billId") long billId,
      @Param("type") MessageType type,
      @Param("time") Timestamp time,
      @Param("content") String content);
}
