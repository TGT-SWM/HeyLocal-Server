/**
 * packageName    : com.heylocal.traveler.mapper
 * fileName       : AccessTokenMapper
 * author         : 우태균
 * date           : 2022/10/18
 * description    : AccessToken 레디스 엔티티 관련 Mapper
 */

package com.heylocal.traveler.mapper;

import com.heylocal.traveler.domain.redis.AccessToken;
import com.heylocal.traveler.domain.redis.RefreshToken;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Mapper(builder = @Builder(disableBuilder = true))
public interface AccessTokenMapper {
  AccessTokenMapper INSTANCE = Mappers.getMapper(AccessTokenMapper.class);

  default Map<String, String> entityToMap(AccessToken accessToken) {
    Map<String, String> result = new ConcurrentHashMap<>();

    String userId = String.valueOf(accessToken.getUserId());
    String tokenValue = accessToken.getTokenValue();
    String timeoutSec = String.valueOf(accessToken.getTimeoutSec());
    String refreshTokenId = String.valueOf(accessToken.getRefreshTokenId());
    String createdDate = accessToken.getCreatedDate().toString();

    result.put("userId", userId);
    result.put("tokenValue", tokenValue);
    result.put("timeoutSec", timeoutSec);
    result.put("refreshTokenId", refreshTokenId);
    result.put("createdDate", createdDate);

    return result;
  }

  default AccessToken mapToEntity(Map<String, String> entries) {
    long userId = Long.parseLong(entries.get("userId"));
    String tokenValue = entries.get("tokenValue");
    long timeoutSec = Long.parseLong(entries.get("timeoutSec"));
    long refreshTokenId = Long.parseLong(entries.get("refreshTokenId"));
    LocalDateTime createdDate = LocalDateTime.parse(entries.get("createdDate"));

    return AccessToken.builder()
        .userId(userId)
        .tokenValue(tokenValue)
        .timeoutSec(timeoutSec)
        .refreshTokenId(refreshTokenId)
        .createdDate(createdDate)
        .build();
  }
}
