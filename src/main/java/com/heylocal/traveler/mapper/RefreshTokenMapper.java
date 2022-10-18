/**
 * packageName    : com.heylocal.traveler.mapper
 * fileName       : RefreshTokenMapper
 * author         : 우태균
 * date           : 2022/10/18
 * description    : RefreshToken 레디스 엔티티 관련 Mapper
 */

package com.heylocal.traveler.mapper;

import com.heylocal.traveler.domain.redis.RefreshToken;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Mapper(builder = @Builder(disableBuilder = true))
public interface RefreshTokenMapper {
  RefreshTokenMapper INSTANCE = Mappers.getMapper(RefreshTokenMapper.class);

  default Map<String, String> entityToMap(RefreshToken refreshToken) {
    Map<String, String> result = new ConcurrentHashMap<>();

    String userId = String.valueOf(refreshToken.getUserId());
    String tokenValue = refreshToken.getTokenValue();
    String timeoutSec = String.valueOf(refreshToken.getTimeoutSec());
    String accessTokenId = String.valueOf(refreshToken.getAccessTokenId());
    String createdDate = refreshToken.getCreatedDate().toString();

    result.put("userId", userId);
    result.put("tokenValue", tokenValue);
    result.put("timeoutSec", timeoutSec);
    result.put("accessTokenId", accessTokenId);
    result.put("createdDate", createdDate);

    return result;
  }

  default RefreshToken mapToEntity(Map<String, String> entries) {
    long userId = Long.parseLong(entries.get("userId"));
    String tokenValue = entries.get("tokenValue");
    long timeoutSec = Long.parseLong(entries.get("timeoutSec"));
    long accessTokenId = Long.parseLong(entries.get("accessTokenId"));
    LocalDateTime createdDate = LocalDateTime.parse(entries.get("createdDate"));

    return RefreshToken.builder()
        .userId(userId)
        .tokenValue(tokenValue)
        .timeoutSec(timeoutSec)
        .accessTokenId(accessTokenId)
        .createdDate(createdDate)
        .build();
  }
}
