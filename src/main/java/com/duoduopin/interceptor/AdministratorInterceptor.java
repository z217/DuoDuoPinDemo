package com.duoduopin.interceptor;

import com.duoduopin.annotation.Administrator;
import com.duoduopin.config.DuoDuoPinUtils;
import com.duoduopin.manager.TokenManager;
import com.duoduopin.model.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @description 检查是否为管理员
 * @author z217
 * @date 2021/01/25
 * @see com.duoduopin.annotation.Administrator
 */
@Component
public class AdministratorInterceptor extends HandlerInterceptorAdapter {
  @Autowired private TokenManager tokenManager;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    if (!(handler instanceof HandlerMethod)) return true;
    Method method = ((HandlerMethod) handler).getMethod();
    if (method.getAnnotation(Administrator.class) != null) {
      TokenModel tokenModel =
          tokenManager.getToken(request.getHeader(DuoDuoPinUtils.AUTHORIZATION));
      return DuoDuoPinUtils.checkIfAdmin(tokenModel.getUserId());
    }
    return false;
  }
}
