package com.duoduopin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@MapperScan(value = "com.duoduopin.dao")
public class DuoDuoPinDemoApplication extends SpringBootServletInitializer {
  public static void main(String[] args) {
    SpringApplication.run(DuoDuoPinDemoApplication.class, args);
  }
  
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(this.getClass());
  }
}
