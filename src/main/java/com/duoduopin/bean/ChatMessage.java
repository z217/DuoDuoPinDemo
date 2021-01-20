package com.duoduopin.bean;

import com.duoduopin.config.MessageType;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @description 聊天信息
 * @author z217
 * @date 2021/01/17
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
}
