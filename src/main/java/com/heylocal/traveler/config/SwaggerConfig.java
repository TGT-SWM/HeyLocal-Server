/**
 * packageName    : com.heylocal.traveler.config
 * fileName       : SwaggerConfig
 * author         : 우태균
 * date           : 2022/08/12
 * description    : Swagger 문서 생성을 위한 Configuration
 */

package com.heylocal.traveler.config;

import com.fasterxml.classmate.TypeResolver;
import com.heylocal.traveler.dto.ErrorMessageResponse;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-08-12T04:12:44.357Z[GMT]")
@Configuration
public class SwaggerConfig {

  @Bean
  public Docket customImplementation(TypeResolver typeResolver){
    return new Docket(DocumentationType.OAS_30)
        .additionalModels(
            typeResolver.resolve(ErrorMessageResponse.class)
        )
        .securityContexts(Arrays.asList(securityContext()))
        .securitySchemes(Arrays.asList(httpAuthenticationScheme()))
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.heylocal.traveler.controller"))
        .build()
        .directModelSubstitute(LocalDate.class, java.sql.Date.class)
        .directModelSubstitute(LocalDateTime.class, java.util.Date.class)
        .apiInfo(apiInfo())
        .tags(
            new Tag("Signup", "회원가입", 1),
            new Tag("Signin", "로그인", 2),
            new Tag("Auth", "인가(Authorization)", 3),
            new Tag("Users", "사용자", 4),
            new Tag("TravelOns", "여행On", 5),
            new Tag("Plans", "스케줄", 6),
            new Tag("ChatRooms", "채팅방", 7),
            new Tag("Places", "장소", 8),
            new Tag("Regions", "지역", 9),
            new Tag("Aws", "AWS용 Callback URL", 10)
        );
  }

  ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("현지야")
        .description("현지야 서버 Application API Document")
        .license("Apache 2.0")
        .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
        .termsOfServiceUrl("")
        .version("1.0.0")
        .contact(new Contact("","", "dnxprbs@gmail.com"))
        .build();
  }

  @Bean
  public OpenAPI openApi() {
    return new OpenAPI()
        .info(new Info()
            .title("현지야")
            .description("현지야 서버 Application API Document")
            .termsOfService("")
            .version("1.0.0")
            .license(new License()
                .name("Apache 2.0")
                .url("http://www.apache.org/licenses/LICENSE-2.0.html"))
            .contact(new io.swagger.v3.oas.models.info.Contact()
                .email("dnxprbs@gmail.com")))
            .components(new Components());
  }

  private SecurityContext securityContext() {
    return SecurityContext.builder()
        .securityReferences(defaultAuth())
        .build();
  }

  private List<SecurityReference> defaultAuth() {
    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
  }

  @Bean
  public HttpAuthenticationScheme httpAuthenticationScheme() {
    return new HttpAuthenticationScheme("Authorization", "인가", "http", "bearer", "JWT", new ArrayList<>());
  }

}
