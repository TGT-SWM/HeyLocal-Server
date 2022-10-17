package com.heylocal.traveler.repository.redis;

import com.heylocal.traveler.domain.redis.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
}
