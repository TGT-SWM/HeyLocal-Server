/**
 * packageName    : com.heylocal.traveler.config
 * fileName       : InterceptorConfig
 * author         : 우태균
 * date           : 2022/08/18
 * description    : Spring Interceptor 등록 Configuration
 */

package com.heylocal.traveler.config;

import com.heylocal.traveler.interceptor.auth.AuthInterceptor;
import com.heylocal.traveler.interceptor.notfound.NotFoundInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {
  private final NotFoundInterceptor notFoundInterceptor;
  private final AuthInterceptor authInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    //NotFound 인터셉터
    registry.addInterceptor(notFoundInterceptor)
        .order(0)
        .addPathPatterns("/**")
        .excludePathPatterns("/swagger-ui/**",
            "/swagger-resources/**",
            "/static/**",
            "/v3/api-docs",
            "/error");

    //인가 인터셉터
    registry.addInterceptor(authInterceptor)
        .order(1)
        .addPathPatterns("/**")
        .excludePathPatterns("/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/api-docs",
            "/",
            "/home",
            "/static/**",
            "/signup/**",
            "/signin/**",
            "/auth/**",
            "/aws/**",
            "/error");
  }
}
