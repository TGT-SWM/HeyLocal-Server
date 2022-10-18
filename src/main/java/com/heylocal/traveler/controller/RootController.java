/**
 * packageName    : com.heylocal.traveler.controller
 * fileName       : RootController
 * author         : 우태균
 * date           : 2022/10/13
 * description    : 홈(Root) API 컨트롤러
 */

package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.RootApi;
import com.heylocal.traveler.domain.redis.RefreshToken;
import com.heylocal.traveler.repository.redis.RefreshTokenRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RootController implements RootApi {

  private final RefreshTokenRedisRepository refreshTokenRedisRepository;

  @Override
  public Map<String, String> home() {

    RefreshToken byUserId = refreshTokenRedisRepository.findByUserId(1);
    log.info("refreshToken - userId : {}", byUserId.getUserId());
    log.info("refreshToken - tokenValue : {}", byUserId.getTokenValue());
    log.info("refreshToken - timeoutSec : {}", byUserId.getTimeoutSec());
    log.info("refreshToken - accessTokenId : {}", byUserId.getAccessTokenId());
    log.info("refreshToken - createdDate : {}", byUserId.getCreatedDate());

    Map<String, String> response = new ConcurrentHashMap<>();
    response.put("message", "Hello");
    return response;
  }
}
