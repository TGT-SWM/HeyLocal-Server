package com.heylocal.traveler.util;

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
