package com.duoduopin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@MapperScan(value = "com.duoduopin.mapper")
public class DuoDuoPinDemoApplication {
  public static void main(String[] args) {
    SpringApplication.run(DuoDuoPinDemoApplication.class, args);
  }
}
