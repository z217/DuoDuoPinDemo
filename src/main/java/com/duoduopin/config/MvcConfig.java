package com.duoduopin.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.duoduopin.interceptor.AdministratorInterceptor;
import com.duoduopin.interceptor.AuthorizationInterceptor;
import com.duoduopin.resolver.CurrentUserMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 配置类，增加自定义拦截器和解析器
 *
 * @author z217
 * @date 2021/03/28
 * @see com.duoduopin.resolver.CurrentUserMethodArgumentResolver
 * @see com.duoduopin.interceptor.AuthorizationInterceptor
 * @see com.duoduopin.interceptor.AdministratorInterceptor
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
  
  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
    FastJsonConfig fastJsonConfig = new FastJsonConfig();
    fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
    converter.setFastJsonConfig(fastJsonConfig);
    converters.add(converter);
  }
}
