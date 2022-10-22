/**
 * packageName    : com.heylocal.traveler.config
 * fileName       : PropertiesConfig
 * author         : 우태균
 * date           : 2022/10/22
 * description    : properties 값을 utf-8로 가져오기 위한 빈 설정
 */

package com.heylocal.traveler.config;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.nio.charset.StandardCharsets;

@Configuration
public class PropertiesConfig {

  /**
   * properties 파일의 값을 읽어올 때, 기본적으로 ISO_8859_1 을 사용한다.
   * signup-pattern.properties 파일의 값을 읽어올 때, UTF-8 을 사용하도록 수정하여 Bean으로 등록
   * @return
   */
  @Bean
  public PropertiesFactoryBean propertiesToUft8() {
    PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
    ClassPathResource classPathResource = new ClassPathResource("signup-pattern.properties");

    propertiesFactoryBean.setLocation(classPathResource);
    propertiesFactoryBean.setFileEncoding(StandardCharsets.UTF_8.toString());

    return propertiesFactoryBean;
  }
}
