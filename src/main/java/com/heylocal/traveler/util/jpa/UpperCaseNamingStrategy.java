/**
 * packageName    : com.heylocal.traveler.util.jpa
 * fileName       : UpperCaseNamingStrategy
 * author         : 우태균
 * date           : 2022/08/12
 * description    : JPA DDL AUTO 시, 네이밍 전략
 *                  모두 대문자 + 언더바 조합으로 생성함
 */

package com.heylocal.traveler.util.jpa;

import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import java.util.Locale;

public class UpperCaseNamingStrategy extends CamelCaseToUnderscoresNamingStrategy {
  @Override
  protected Identifier getIdentifier(String name, boolean quoted, JdbcEnvironment jdbcEnvironment) {
    return new Identifier(name.toUpperCase(Locale.ROOT), quoted );
  }
}
