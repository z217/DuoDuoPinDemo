package com.duoduopin.bean;

import lombok.*;

import java.sql.Timestamp;

/**
 * @description 系统消息
 * @author z217
 * @date 2021/01/25
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SystemMessage {
  private long messageId;
  private Long senderId;
  private Long receiverId;
  private Long billId;
  private MessageType type;
  private Timestamp time;
  private String content;

  public SystemMessage(
      Long senderId,
      Long receiverId,
      Long billId,
      MessageType type,
      Timestamp time,
      String content) {
    this.senderId = senderId;
    this.receiverId = receiverId;
    this.billId = billId;
    this.type = type;
    this.time = time;
    this.content = content;
  }

  public enum MessageType {
    BROAD,
    APPLY,
    ALLOW,
    REJEC;
  }
}
