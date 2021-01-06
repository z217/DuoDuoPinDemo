package com.duoduopin.dao;

import com.duoduopin.bean.User;

/**
 * 用户映射类
 *
 * @author z217
 * @date 2020/08/03
 */
public interface UserMapper {
  User getUserById(long userId);
  
  User getUserByUsername(String username);
  
  void insertUser(User user);
  
  void deleteUser(long userId);
}
