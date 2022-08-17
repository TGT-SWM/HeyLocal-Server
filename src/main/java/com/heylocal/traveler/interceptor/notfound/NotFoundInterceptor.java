package com.heylocal.traveler.interceptor.notfound;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class NotFoundInterceptor implements HandlerInterceptor {
  private final DispatcherServlet dispatcherServlet;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    if (is404NotFound(request)) {
      response.sendError(HttpStatus.NOT_FOUND.value(), "존재하지 않는 URL");
      return false;
    }

    return true;
  }

  private boolean is404NotFound(HttpServletRequest request) throws Exception {
    String uri = request.getRequestURI();

    if (Objects.isNull(dispatcherServlet.getHandlerMappings() != null)) { // [if-start]

      //DispatcherServlet 이 매핑해줄 수 있는 핸들러마다 반복
      for (HandlerMapping mapping : dispatcherServlet.getHandlerMappings()) { // [for-start]
        HandlerExecutionChain handlerExecutionChain = mapping.getHandler(request); //요청받은 request 객체를 처리할 수 있는 핸들러 get
        // ResourceHttpRequestHandler 는 정적 리소스를 응답하는 핸들러이다. 우리는 정적 리소스를 응답하지 않으므로, 제외해도 무관하다.
        if (!handlerExecutionChain.getHandler().toString().contains("ResourceHttpRequestHandler")) {
          return false; //매핑되는 핸들러를 찾은 경우
        }
      } // [for-end]

    } // [if-end]

    return true; //매핑되는 핸들러를 못찾은 경우
  }
}
