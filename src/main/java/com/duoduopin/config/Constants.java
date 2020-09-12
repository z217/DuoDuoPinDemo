package com.duoduopin.config;

/**
 * 常量
 *
 * @author z217
 * @date 2020/08/07
 */
public abstract class Constants {
  //  当前登录用户id的字段名
  public static final String CURRENT_USER_ID = "CURRENT_USER_ID";
  //  token有效期
  public static final int TOKEN_EXPIRES_HOUR = 72;
  //  存放Authorization的header字段
  public static final String AUTHORIZATION = "token";
  //  管理员账户id
  public static final Long[] ADMIN_ID = new Long[1];
  
  static {
    ADMIN_ID[0] = 1L;
  }
}
