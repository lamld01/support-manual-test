package com.lamld.supportmanualtest.server.configs;

import com.lamld.supportmanualtest.server.exception.GlobalExceptionHandler;
import com.lamld.supportmanualtest.server.utils.DateUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
@Log4j2
public class LoggingFilter implements Filter {

  @Autowired
  GlobalExceptionHandler globalExceptionHandler;
  @Value("${server.timeoutSlowApi:100}")
  private Integer timeoutSlowApi;

  @Override
  public void init(FilterConfig filterConfig) {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    long start = DateUtils.getNowMillisAtUtc();

    try {
      filterChain.doFilter(request, response);
    } finally {
      long duration = DateUtils.getNowMillisAtUtc() - start;
      MDC.put("duration", duration + "");
      if (duration > timeoutSlowApi) {
        log.warn("end request ==> {}  {} {}", ((HttpServletRequest) request).getMethod(), getPath((HttpServletRequest) request), duration);
      } else {
        log.debug("end request ==> {}  {} {}", ((HttpServletRequest) request).getMethod(), getPath((HttpServletRequest) request), duration);
      }
      MDC.remove("duration");
      MDC.remove("userId");
    }


  }

  public static String getPath(HttpServletRequest request) {
    StringBuilder requestURL = new StringBuilder(request.getRequestURI());
    String queryString = request.getQueryString();
    return queryString == null ? requestURL.toString() : requestURL.append('?').append(queryString).toString();
  }

}
