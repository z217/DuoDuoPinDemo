package com.duoduopin.controller;

import com.duoduopin.entity.User;
import com.duoduopin.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
  @Autowired
  UserMapper userMapper;
  
  @GetMapping("/user/{id}")
  public User getUser(@PathVariable("id") long id) {
    return userMapper.getUserById(id);
  }
}
