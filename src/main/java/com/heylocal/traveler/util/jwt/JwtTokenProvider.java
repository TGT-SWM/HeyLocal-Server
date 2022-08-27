package com.heylocal.traveler.util.jwt;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Access·Refresh Token 생성 클래스
 */
@Component
public class JwtTokenProvider {
  private final String secretKey;
  private final long accessTokenValidMilliSec;
  private final long refreshTokenValidMilliSec;

  /**
   * 생성자를 통해, auth-secret.properties 에 정의된 값을 final 필드에 바인딩
   * @param secretKey 서버에 숨겨둔 비밀키 값
   * @param accessTokenValidMilliSec access token 유효 시간 (밀리초)
   * @param refreshTokenValidMilliSec refresh token 유효 시간 (밀리초)
   */
  public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
      @Value("${jwt.token-valid-millisecond.access-token}") long accessTokenValidMilliSec,
      @Value("${jwt.token-valid-millisecond.refresh-token}") long refreshTokenValidMilliSec) {
    this.secretKey = secretKey;
    this.accessTokenValidMilliSec = accessTokenValidMilliSec;
    this.refreshTokenValidMilliSec = refreshTokenValidMilliSec;
  }

  /**
   * Access Token 발급 메서드
   * @param userPk 사용자 계정 pk값
   * @return jwt Access Token
   */
  public String createAccessToken(long userPk) {
    Date now = new Date();

    return Jwts.builder()
        .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // jwt를 사용한다고 헤더에 명시
        .setIssuer("http://traveler.heylocal.p-e.kr") //토큰 발급자(iss) 설정
        .setIssuedAt(now) //토큰 발급 시간(iat) 설정
        .setExpiration(new Date(now.getTime() + accessTokenValidMilliSec)) // 만료시간 설정
        .claim("userPk", userPk) //토큰을 받을 사용자 pk를 비공개 클레임으로 설정
        .signWith(SignatureAlgorithm.HS512, secretKey) //해싱 알고리즘으로 HS512를 사용하기 때문에, secretKey가 512비트 이상이어야 함
        .compact();
  }

  /**
   * Refresh Token 발급 메서드
   * @return jwt Refresh Token
   */
  public String createRefreshToken(long userPk) {
    Date now = new Date();

    return Jwts.builder()
        .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // jwt를 사용한다고 헤더에 명시
        .setIssuer("http://traveler.heylocal.p-e.kr") //토큰 발급자(iss) 설정
        .setIssuedAt(now) //토큰 발급 시간(iat) 설정
        .setExpiration(new Date(now.getTime() + refreshTokenValidMilliSec)) // 만료시간 설정
        .claim("userPk", userPk) //토큰을 받을 사용자 pk를 비공개 클레임으로 설정
        .signWith(SignatureAlgorithm.HS512, secretKey) //해싱 알고리즘으로 HS512를 사용하기 때문에, secretKey가 512비트 이상이어야 함
        .compact();
  }

}
