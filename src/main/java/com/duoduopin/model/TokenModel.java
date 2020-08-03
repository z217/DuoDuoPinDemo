package com.duoduopin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TokenModel {
  //  用户id
  private long id;
  //  随机生成uuid
  private String token;
}
