package com.duoduopin.resolver;

import com.duoduopin.annotation.CurrentUser;
import com.duoduopin.bean.User;
import com.duoduopin.config.Constants;
import com.duoduopin.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

/**
 * 将当前用户注入标有@CurrentUser的参数
 *
 * @see CurrentUser
 */
@Component
public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {
  @Autowired private UserMapper userMapper;

  @Override
  public boolean supportsParameter(MethodParameter methodParameter) {
    //    如果参数类型是User并且标注@CurrentUser则使用该解析器
    return methodParameter.getParameterType().isAssignableFrom(User.class)
        && methodParameter.hasParameterAnnotation(CurrentUser.class);
  }

  @Override
  public Object resolveArgument(
      MethodParameter methodParameter,
      ModelAndViewContainer modelAndViewContainer,
      NativeWebRequest nativeWebRequest,
      WebDataBinderFactory webDataBinderFactory)
      throws Exception {
    //    取出当前用户id
    Long currentUserId =
        (Long)
            nativeWebRequest.getAttribute(
                Constants.CURRENT_USER_ID, RequestAttributes.SCOPE_REQUEST);
    if (currentUserId == null)
      throw new MissingServletRequestPartException(Constants.CURRENT_USER_ID);
    return userMapper.getUserById(currentUserId);
  }
}
