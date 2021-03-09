package com.duoduopin.config;

import com.duoduopin.interceptor.AdministratorInterceptor;
import com.duoduopin.interceptor.AuthorizationInterceptor;
import com.duoduopin.resolver.CurrentUserMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 配置类，增加自定义拦截器和解析器
 *
 * @author ScienJus
 * @date 2015/7/30.
 * @see com.duoduopin.resolver.CurrentUserMethodArgumentResolver
 * @see com.duoduopin.interceptor.AuthorizationInterceptor
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
  @Autowired
  private AuthorizationInterceptor authorizationInterceptor;
  @Autowired
  private AdministratorInterceptor administratorInterceptor;
  @Autowired
  private CurrentUserMethodArgumentResolver currentUserMethodArgumentResolver;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(authorizationInterceptor);
    registry.addInterceptor(administratorInterceptor);
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(currentUserMethodArgumentResolver);
  }
}
