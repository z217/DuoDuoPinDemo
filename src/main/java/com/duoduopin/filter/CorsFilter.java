package com.duoduopin.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "corsFilter", urlPatterns = "/*")
@Component
@Slf4j
public class CorsFilter implements Filter {
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {}

  @Override
  public void destroy() {}

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
    // 表明它允许"http://xxx"发起跨域请求
    httpResponse.setHeader("Access-Control-Allow-Origin", "*");
    // 表明在xxx秒内，不需要再发送预检验请求，可以缓存该结果
    httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");
    // 表明它允许xxx的外域请求
    httpResponse.setHeader("Access-Control-Max-Age", "3600");
    // 表明它允许跨域请求包含xxx头
    httpResponse.setHeader("Access-Control-Allow-Headers", "*");
    filterChain.doFilter(servletRequest, servletResponse);
  }
}
