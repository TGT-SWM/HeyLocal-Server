/**
 * packageName    : com.heylocal.traveler.mapper
 * fileName       : AccessTokenMapper
 * author         : 우태균
 * date           : 2022/10/18
 * description    : AccessToken 레디스 엔티티 관련 Mapper
 */

package com.heylocal.traveler.mapper;

import com.heylocal.traveler.domain.redis.AccessToken;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

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
}
