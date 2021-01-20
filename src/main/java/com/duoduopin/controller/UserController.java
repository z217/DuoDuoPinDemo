package com.duoduopin.controller;

import com.duoduopin.annotation.Authorization;
import com.duoduopin.annotation.CurrentUser;
import com.duoduopin.bean.User;
import com.duoduopin.config.ResultStatus;
import com.duoduopin.model.ResultModel;
import com.duoduopin.model.TokenModel;
import com.duoduopin.pojo.LoginPOJO;
import com.duoduopin.pojo.RegisterPOJO;
import com.duoduopin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 *
 * @author z217
 * @date 2021/01/06
 * @see com.duoduopin.dao.UserMapper
 * @see com.duoduopin.manager.TokenManager
 */
@RestController
@RequestMapping(value = "User", produces = "application/json;charset=UTF-8")
public class UserController {
  @Autowired
  private UserService userService;
  
  @PostMapping("/{id}")
  public ResponseEntity<ResultModel> getUserById(@PathVariable("id") long userId) {
    User user = userService.getUserById(userId);
    if (user == null)
      return new ResponseEntity<>(
        ResultModel.error(ResultStatus.USER_NOT_FOUND), HttpStatus.NOT_FOUND);
    user.setPassword("");
    return new ResponseEntity<>(ResultModel.ok(user), HttpStatus.OK);
  }
  
  @PutMapping("/register")
  public ResponseEntity<ResultModel> register(@RequestBody RegisterPOJO registerPOJO) {
    Long userId = userService.createUser(
      registerPOJO.getUsername(), registerPOJO.getNickname(), registerPOJO.getPassword());
    if (userId == null)
      return new ResponseEntity<>(
        ResultModel.error(ResultStatus.USERNAME_EXIST), HttpStatus.NOT_ACCEPTABLE);
    return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
  }
  
  @PostMapping("/login")
  public ResponseEntity<ResultModel> login(@RequestBody LoginPOJO loginPOJO) {
    TokenModel token = userService.userLogin(loginPOJO.getUsername(), loginPOJO.getPassword());
    if (token == null)
      return new ResponseEntity<>(
        ResultModel.error(ResultStatus.USERNAME_OR_PASSWORD_ERROR), HttpStatus.NOT_FOUND);
    return new ResponseEntity<>(ResultModel.ok(token), HttpStatus.OK);
  }

  @DeleteMapping("/logout")
  @Authorization
  public ResponseEntity<ResultModel> logout(@CurrentUser User user) {
    userService.userLogout(user);
    return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
  }
  
  @DeleteMapping("delete/{id}")
  @Authorization
  public ResponseEntity<ResultModel> deleteUser(
    @CurrentUser User user, @PathVariable("id") long userId) {
    if (!userService.deleteUser(user, userId)) {
      return new ResponseEntity<>(
        ResultModel.error(ResultStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
    }
    return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
  }
}
