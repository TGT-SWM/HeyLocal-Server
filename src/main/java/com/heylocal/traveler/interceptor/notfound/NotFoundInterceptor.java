package com.heylocal.traveler.interceptor.notfound;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heylocal.traveler.dto.ErrorMessageResponse;
import com.heylocal.traveler.exception.NotFoundException;
import com.heylocal.traveler.exception.code.NotFoundCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotFoundInterceptor implements HandlerInterceptor {
  private final ObjectMapper objectMapper;
  private final DispatcherServlet dispatcherServlet;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    if (is404NotFound(request)) {
      responseError(response, NotFoundCode.NOT_FOUND_URL);
      return false;
    }

    return true;
  }

  private boolean is404NotFound(HttpServletRequest request) throws Exception {
    String uri = request.getRequestURI();

    if (dispatcherServlet.getHandlerMappings() != null) { // [if-start]
      //DispatcherServlet 이 매핑해줄 수 있는 핸들러마다 반복
      for (HandlerMapping mapping : dispatcherServlet.getHandlerMappings()) { // [for-start]
        HandlerExecutionChain handlerExecutionChain = mapping.getHandler(request); //요청받은 request 객체를 처리할 수 있는 핸들러 get
        if (handlerExecutionChain == null) return true;
        // ResourceHttpRequestHandler 는 정적 리소스를 응답하는 핸들러이다. 우리는 정적 리소스를 응답하지 않으므로, 제외해도 무관하다.
        if (!handlerExecutionChain.getHandler().toString().contains("ResourceHttpRequestHandler"))
          return false; //매핑되는 핸들러를 찾은 경우
      } // [for-end]

    } // [if-end]

    return true; //매핑되는 핸들러를 못찾은 경우
  }

  /**
   * <pre>
   * 오류 응답 메시지를 만드는 메서드
   * </pre>
   * @param response 오류 응답 Response 객체
   * @param code 오류 종류
   * @throws IOException
   */
  private void responseError(HttpServletResponse response, NotFoundCode code) throws IOException {
    ErrorMessageResponse errorResponse;
    String responseBody;

    response.setStatus(HttpStatus.NOT_FOUND.value());
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    errorResponse = new ErrorMessageResponse(new NotFoundException(code));
    responseBody = objectMapper.writeValueAsString(errorResponse);


    //JSON 형식으로 응답
    response.getWriter().write(responseBody);
  }
}
