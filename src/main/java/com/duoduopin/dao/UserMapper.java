package com.duoduopin.dao;

import com.duoduopin.bean.User;

import java.sql.Timestamp;

/**
 * 用户映射类
 *
 * @author z217
 * @date 2021/01/06
 */
public interface UserMapper {
  User getUserById(long userId);
  
  User getUserByUsername(String username);
  
  String getNickNameByUserId(long userId);
  
  Timestamp getLastOnlineByUesrId(long userId);
  
  int updateLastOnlineByUserId(long userId);
  
  int insertUser(User user);
  
  void deleteUser(long userId);
}
