package com.heylocal.traveler.mapper;

import com.heylocal.traveler.domain.profile.UserProfile;
import com.heylocal.traveler.domain.user.User;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import static com.heylocal.traveler.dto.UserDto.UserProfileResponse;
import static com.heylocal.traveler.dto.UserDto.UserResponse;

@Mapper(uses = {RegionMapper.class}, builder = @Builder(disableBuilder = true))
public interface UserMapper {
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  @Mapping(target = "nickname", source = "entity.user.nickname")
  UserProfileResponse toUserProfileResponseDto(UserProfile entity, long ranking);

  @Mapping(target = "imageUrl", source = "user.userProfile.imageUrl")
  @Mapping(target = "knowHow", source = "user.userProfile.knowHow")
  UserResponse toUserResponseDto(User user);
}
