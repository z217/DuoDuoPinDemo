package com.duoduopin.dao;

import com.duoduopin.bean.User;

public interface UserMapper {
  User getUserById(long id);
  
  User getUserByUsername(String username);
  
  void insertUser(User user);
}
