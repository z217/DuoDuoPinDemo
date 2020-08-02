package com.duoduopin.mapper;

import com.duoduopin.entity.User;

public interface UserMapper {
  User getUserById(long id);
  void insertUser(User user);
}
