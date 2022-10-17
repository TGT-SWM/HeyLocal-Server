package com.heylocal.traveler.repository.redis;

import com.heylocal.traveler.domain.redis.AccessToken;
import org.springframework.data.repository.CrudRepository;

public interface AccessTokenRedisRepository extends CrudRepository<AccessToken, String> {
}
