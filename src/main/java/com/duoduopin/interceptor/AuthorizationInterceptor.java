package com.duoduopin.interceptor;

import com.duoduopin.annotation.Authorization;
import com.duoduopin.config.Constants;
import com.duoduopin.manager.TokenManager;
import com.duoduopin.model.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
  @Autowired private TokenManager manager;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    //    忽略非映射方法
    if (!(handler instanceof HandlerMethod)) return true;
    Method method = ((HandlerMethod) handler).getMethod();
    //    从header中得到token
    TokenModel tokenModel = manager.getToken(request.getHeader(Constants.AUTHORIZATION));
    if (manager.checkToken(tokenModel)) {
      request.setAttribute(Constants.CURRENT_USER_ID, tokenModel.getId());
      return true;
    } else if (method.getAnnotation(Authorization.class) != null) {
      //      验证token失败，并且方法标注了@Authrization，返回401
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return false;
    }
    return true;
  }
}
