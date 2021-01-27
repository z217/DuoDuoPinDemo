package com.duoduopin.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SystemMessagePOJO {
  private Long senderId;
  private Long receiverId;
  private Long billId;
  private String content;
}
