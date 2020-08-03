package com.duoduopin.manager.impl;

import com.duoduopin.config.Constants;
import com.duoduopin.manager.TokenManager;
import com.duoduopin.model.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class RedisTokenManagerImpl implements TokenManager {
  private RedisTemplate<Long, String> redis;

  @Autowired
  public void setRedis(@Qualifier("stringRedisTemplate") RedisTemplate redis) {
    this.redis = redis;
    //    更改redis序列化方案
    redis.setKeySerializer(new JdkSerializationRedisSerializer());
  }

  @Override
  public TokenModel createToken(long id) {
    String token = UUID.randomUUID().toString().replace("-", "");
    TokenModel tokenModel = new TokenModel(id, token);
    //    存储在redis中并设置时限
    redis.boundValueOps(id).set(token, Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
    return tokenModel;
  }

  @Override
  public boolean checkToken(TokenModel tokenModel) {
    if (tokenModel == null) return false;
    String token = redis.boundValueOps(tokenModel.getId()).get();
    if (token != null && token.equals(tokenModel.getToken())) {
      //      在进行过一次有效验证之后刷新时限
      redis.boundValueOps(tokenModel.getId()).expire(Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
      return true;
    }
    return false;
  }

  @Override
  public TokenModel getToken(String authentication) {
    if (authentication == null || authentication.length() == 0) return null;
    String[] params = authentication.split("_");
    if (params.length != 2) return null;
    long id = Long.parseLong(params[0]);
    String token = params[1];
    return new TokenModel(id, token);
  }

  @Override
  public void deleteToken(long id) {
    redis.delete(id);
  }
}
