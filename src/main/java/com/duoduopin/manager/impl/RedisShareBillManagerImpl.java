package com.duoduopin.manager.impl;

import com.duoduopin.bean.ShareBill;
import com.duoduopin.config.BillType;
import com.duoduopin.manager.ShareBillManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Component
public class RedisShareBillManagerImpl implements ShareBillManager {
  private RedisTemplate<String, String> redisTemplate;
  
  @Autowired
  public void setRedis(@Qualifier("stringRedisTemplate") RedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
    redisTemplate.setKeySerializer(new Jackson2JsonRedisSerializer<>(Object.class));
  }
  
  @Override
  public ShareBill createShareBill(long userId, BillType type, String description, String address, Timestamp time, BigDecimal price, double longitude, double latitude) {
    ShareBill shareBill = new ShareBill(userId, type, description, address, time, price, latitude, longitude);
    if (redisTemplate.opsForGeo().add("locations", new Point(longitude, latitude), "gw01") == null) return null;
    shareBill.setGeohash((String)redisTemplate.opsForGeo().hash("locations", "gw01").get(0));
    redisTemplate.opsForGeo().remove("locations", "gw01");
    return shareBill;
  }
}
