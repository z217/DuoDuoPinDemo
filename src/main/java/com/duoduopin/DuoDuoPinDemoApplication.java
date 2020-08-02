package com.duoduopin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DuoDuoPinDemoApplication {
  public static void main(String[] args) {
    SpringApplication.run(DuoDuoPinDemoApplication.class, args);
  }
}
