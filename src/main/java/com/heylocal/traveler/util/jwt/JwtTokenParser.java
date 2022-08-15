package com.heylocal.traveler.util.jwt;

import com.heylocal.traveler.util.date.LocalDateTimeTransformer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

/**
 * Jwt Token 파싱 및 유효성 검증 클래스
 */
@Component
public class JwtTokenParser {

  private final String secretKey;
  private final LocalDateTimeTransformer localDateTimeTransformer;

  /**
   * 생성자를 통해, auth-secret.properties 에 정의된 값을 final 필드에 바인딩
   * @param secretKey 비밀키
   */
  public JwtTokenParser(@Value("${jwt.secret}") String secretKey, LocalDateTimeTransformer localDateTimeTransformer) {
    this.secretKey = secretKey;
    this.localDateTimeTransformer = localDateTimeTransformer;
  }

  /**
   * 유효한 JWT 토큰인지 확인하고, 클레임을 파싱하는 메서드
   * 반환된 Optional이 null이라면, 유효하지 않은 토큰
   * @param authHttpHeader 헤더 중 Authorization 헤더 값 (Access Token)
   * @return 토큰이 정상적인 형태라면, 토큰의 클레임들을 Optional로 Wrapping하여 반환
   */
  public Optional<Claims> parseJwtToken(String authHttpHeader) throws JwtException {
    String token;

    validateAuthorizationHeader(authHttpHeader); //Authorization 헤더 값을 전달하여, 올바른 형식인지 확인
    token = extractToken(authHttpHeader); //실제 토큰값 가져오기

    return Optional.ofNullable(
            Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody());
  }

  /**
   * 토큰 만료기간 추출 메서드
   * @param tokenValue 토큰 값
   * @return 만료기간을 LocalDateTime 으로 반환
   */
  public LocalDateTime extractExpiration(String tokenValue) {
    Date expiration = Jwts.parser()
        .setSigningKey(secretKey)
        .parseClaimsJws(tokenValue)
        .getBody()
        .getExpiration();
    return localDateTimeTransformer.dateToLocalDateTime(expiration);
  }


  /**
   * authorization 헤더가 올바른 형식을 가지고 있는지 확인하는 메서드
   * @param authorizationHeader 요청 메시지의 헤더 중 Token이 담겨있는 Authorization 헤더의 값
   */
  private void validateAuthorizationHeader(String authorizationHeader) {
    if (authorizationHeader == null) {
      throw new JwtException("Authorization HTTP 헤더에 값이 없습니다.");
    }
    if (!authorizationHeader.startsWith("Bearer ")) {
      throw new JwtException("Authorization HTTP 헤더의 값이 Bearer 로 시작하지 않습니다.");
    }
  }

  /**
   * 검증된 토큰의 실제 토큰부분을 추출하는 메서드
   * @param authorizationHeader 검증된 JwtToken
   * @return "Bearer"를 제외한 실제 토큰부분 반환함
   */
  private String extractToken(String authorizationHeader) {
    return authorizationHeader.substring("Bearer ".length());
  }
}
