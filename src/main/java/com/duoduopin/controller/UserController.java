package com.duoduopin.controller;

import com.duoduopin.annotation.Authorization;
import com.duoduopin.annotation.CurrentUser;
import com.duoduopin.bean.User;
import com.duoduopin.config.ResultStatus;
import com.duoduopin.dao.UserMapper;
import com.duoduopin.manager.TokenManager;
import com.duoduopin.model.ResultModel;
import com.duoduopin.model.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
  @Autowired
  private UserMapper userMapper;
  
  @Autowired
  private TokenManager tokenManager;
  
  @PutMapping("/register")
  public ResponseEntity<ResultModel> register(
    @RequestParam String username, @RequestParam String nickname, @RequestParam String password) {
    Assert.notNull(username, "username cannot be null");
    Assert.notNull(nickname, "nickname cannot be null");
    Assert.notNull(password, "password cannot be null");
    if (userMapper.getUserByUsername(username) != null)
      return new ResponseEntity<>(
        ResultModel.error(ResultStatus.USERNAME_EXIST), HttpStatus.NOT_ACCEPTABLE);
    User user = new User(username, nickname, password);
    userMapper.insertUser(user);
    return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
  }
  
  @PostMapping("/login")
  public ResponseEntity<ResultModel> login(
    @RequestParam String username, @RequestParam String password) {
    Assert.notNull(username, "username cannot be null");
    Assert.notNull(password, "password cannnot be null");
    User user = userMapper.getUserByUsername(username);
    if (user == null || !user.getPassword().equals(password))
      return new ResponseEntity<>(
        ResultModel.error(ResultStatus.USERNAME_OR_PASSWORD_ERROR), HttpStatus.NOT_FOUND);
    TokenModel token = tokenManager.createToken(user.getUserId());
    return new ResponseEntity<>(ResultModel.ok(token), HttpStatus.OK);
  }
  
  @DeleteMapping("/logout")
  @Authorization
  public ResponseEntity<ResultModel> logout(@CurrentUser User user) {
    tokenManager.deleteToken(user.getUserId());
    return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
  }
}
