package com.duoduopin.model;

import com.duoduopin.bean.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * token model
 *
 * @author z217
 * @date 2021/01/06
 */
@AllArgsConstructor
@Getter
@Setter
public class TokenModel {
  //  用户id
  private long id;
  //  随机生成uuid
  private String token;
  private String username;
  private String nickname;
  
  public TokenModel(User user, String token) {
    this.id = user.getUserId();
    this.token = token;
    this.username = user.getUsername();
    this.nickname = user.getNickname();
  }
}
