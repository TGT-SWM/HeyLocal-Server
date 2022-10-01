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

  @Mapping(target = "nickname", source = "userProfile.user.nickname")
  @Mapping(target = "ranking", ignore = true)
  UserProfileResponse toUserProfileResponseDto(UserProfile userProfile, int acceptedOpinionCount);

  @Mapping(target = "nickname", source = "userProfile.user.nickname")
  @Mapping(target = "ranking", constant = "0L")
  @Mapping(target = "acceptedOpinionCount", ignore = true)
  UserProfileResponse toUserProfileResponseDto(UserProfile userProfile);

  @Mapping(target = "introduce", source = "user.userProfile.introduce")
  @Mapping(target = "imageUrl", source = "user.userProfile.imageUrl")
  @Mapping(target = "knowHow", source = "user.userProfile.knowHow")
  @Mapping(target = "activityRegion", source = "user.userProfile.activityRegion")
  @Mapping(target = "ranking", ignore = true)
  @Mapping(target = "acceptedOpinionCount", ignore = true)
  UserProfileResponse toUserProfileResponseDto(User user);

  UserResponse toUserResponseDto(User user);
}
