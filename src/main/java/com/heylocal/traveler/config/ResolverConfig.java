package com.heylocal.traveler.config;

import com.heylocal.traveler.controller.resolver.LoginUserResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * packageName    : com.heylocal.traveler.config
 * fileName       : ResolverConfig
 * author         : 우태균
 * date           : 2022/08/18
 * description    : Argument Resolver 사용을 위한 Configuration
 */

@Configuration
@RequiredArgsConstructor
public class ResolverConfig implements WebMvcConfigurer {
  private final LoginUserResolver loginUserResolver;

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(loginUserResolver);
  }
}
