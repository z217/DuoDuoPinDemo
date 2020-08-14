package com.duoduopin.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * User实体类
 */
@Getter
@Setter
public class User {
  private long userId;
  
  private String username;
  
  private String nickname;
  
  private String password;
  
  public User(String username, String nickname, String password) {
    this.username = username;
    this.nickname = nickname;
    this.password = password;
  }
}
