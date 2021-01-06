package com.duoduopin.service;

import com.duoduopin.bean.User;
import com.duoduopin.config.Constants;
import com.duoduopin.dao.UserMapper;
import com.duoduopin.manager.TokenManager;
import com.duoduopin.model.TokenModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户服务层
 *
 * @author z217
 * @date 2020/08/16
 */
@Service
@Slf4j
public class UserService {
  @Autowired private UserMapper userMapper;
  @Autowired private TokenManager tokenManager;

  public boolean createUser(String username, String nickname, String password) {
    log.info("User create begin, exec in UserService.createUser().");
    if (userMapper.getUserByUsername(username) != null) {
      log.info(username + "already exists, exec in UserService.createUser().");
      return false;
    }
    User user = new User(username, nickname, password);
    userMapper.insertUser(user);
    log.info("User:" + username + " create completed, exec in UserService.createUser().");
    return true;
  }

  public TokenModel userLogin(String username, String password) {
    User user = userMapper.getUserByUsername(username);
    if (user == null || !user.getPassword().equals(password)) {
      log.info(username + " login failed, exec in UserService.userLogin().");
      return null;
    }
    TokenModel token = tokenManager.createToken(user.getUserId());
    log.info(username + " login success, token is created, exec in UserService.userLogin().");
    return token;
  }
  
  public void userLogout(User user) {
    tokenManager.deleteToken(user.getUserId());
    log.info(user.getUsername() + " logout");
  }
  
  public boolean deleteUser(User user, long userId) {
    if (!Constants.checkIfAdmin(user.getUserId())) {
      log.warn(
        "An authorized delete is requested by "
          + user.getUserId()
          + ", exec in UserService.deleteUser().");
      return false;
    }
    userMapper.deleteUser(userId);
    log.info(
      userId + " was deleted by " + user.getUserId() + ", exec in UserService.deleteUser().");
    return true;
  }
}
