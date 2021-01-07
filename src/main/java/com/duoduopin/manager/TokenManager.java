package com.duoduopin.manager;

import com.duoduopin.bean.User;
import com.duoduopin.model.TokenModel;

public interface TokenManager {
  /**
   * 创建一个token,关联指定用户
   *
   * @param user 用户
   * @return token
   */
  public TokenModel createToken(User user);

  /**
   * 检查token有效性
   *
   * @param tokenModel token
   * @return true表示有效
   */
  public boolean checkToken(TokenModel tokenModel);

  /**
   * 解析token
   *
   * @param authentication 加密字符串
   * @return token
   */
  public TokenModel getToken(String authentication);

  /**
   * 删除用户关联的token
   *
   * @param id 用户id
   */
  public void deleteToken(long id);
}
