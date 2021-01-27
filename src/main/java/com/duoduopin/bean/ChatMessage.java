package com.duoduopin.bean;

import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author z217
 * @description 聊天信息
 * @date 2021/01/25
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage implements Serializable {
  private long userId;
  private long billId;
  private String nickname;
  private MessageType type;
  private Timestamp time;
  private String content;
  
  public enum MessageType {
    CHAT,
    JOIN,
    QUIT
  }
}
