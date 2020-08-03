package com.duoduopin.config;

import com.duoduopin.interceptor.AuthorizationInterceptor;
import com.duoduopin.resolver.CurrentUserMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 注册自定义的拦截器和解析器
 *
 * @see AuthorizationInterceptor
 * @see CurrentUserMethodArgumentResolver
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
  @Autowired private AuthorizationInterceptor authorizationInterceptor;
  @Autowired private CurrentUserMethodArgumentResolver currentUserMethodArgumentResolver;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(authorizationInterceptor);
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(currentUserMethodArgumentResolver);
  }
}
