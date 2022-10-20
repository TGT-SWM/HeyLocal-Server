/**
 * packageName    : com.heylocal.traveler.mapper
 * fileName       : AccessTokenMapper
 * author         : 우태균
 * date           : 2022/10/19
 * description    : AccessToken 레디스 엔티티 관련 Mapper
 */

package com.heylocal.traveler.mapper;

import com.heylocal.traveler.domain.token.AccessToken;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Mapper(builder = @Builder(disableBuilder = true))
public interface AccessTokenMapper {
  AccessTokenMapper INSTANCE = Mappers.getMapper(AccessTokenMapper.class);

  default Map<String, String> entityToMap(AccessToken accessToken) throws IllegalAccessException {
    Map<String, String> result = new ConcurrentHashMap<>();
    Field[] declaredFields = AccessToken.class.getDeclaredFields();

    //RefreshToken 에 선언한 필드마다 반복
    for (Field field : declaredFields) {
      Class<?> fieldType = field.getType();
      String fieldName = field.getName();
      String fieldValue = "";
      field.setAccessible(true); //private 필드 접근 허용

      //String 형으로 변환
      if (fieldType.equals(Long.class)) { //Long 타입인 경우
        fieldValue = String.valueOf(field.get(accessToken));

      } else if (fieldType.equals(LocalDateTime.class)) { //LocalDateTime 타입인 경우
        LocalDateTime tmp = (LocalDateTime) field.get(accessToken);
        fieldValue = tmp.toString();

      } else if (fieldType.equals(String.class)) { //String 타입인 경우
        fieldValue = (String) field.get(accessToken);
      }

      result.put(fieldName, fieldValue);
    }

    return result;
  }

  default AccessToken mapToEntity(Map<String, String> entries) throws IllegalAccessException {
    AccessToken result = new AccessToken();
    Field[] declaredFields = AccessToken.class.getDeclaredFields();

    //RefreshToken 에 선언한 필드마다 반복
    for (Field field : declaredFields) {
      Class<?> fieldType = field.getType();
      String fieldName = field.getName();
      field.setAccessible(true); //private 필드 접근 허용

      if (fieldType.equals(Long.class)) { //Long 타입인 경우
        Long fieldValue = Long.parseLong(entries.get(fieldName));
        field.set(result, fieldValue);

      } else if (fieldType.equals(LocalDateTime.class)) { //LocalDateTime 타입인 경우
        LocalDateTime fieldValue = LocalDateTime.parse(entries.get(fieldName));
        field.set(result, fieldValue);

      } else if (fieldType.equals(String.class)) { //String 타입인 경우
        String fieldValue = entries.get(fieldName);
        field.set(result, fieldValue);
      }

    }

    return result;
  }
}
