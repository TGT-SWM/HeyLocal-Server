package com.heylocal.traveler.mapper;

import com.heylocal.traveler.domain.profile.UserProfile;
import com.heylocal.traveler.domain.travelon.opinion.Opinion;
import com.heylocal.traveler.domain.user.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import static com.heylocal.traveler.dto.UserDto.UserProfileResponse;
import static com.heylocal.traveler.dto.UserDto.UserResponse;

@Mapper(uses = {RegionMapper.class}, builder = @Builder(disableBuilder = true))
public interface UserMapper {
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  @Mapping(target = "nickname", source = "userProfile.user.nickname")
  @Mapping(target = "userId", source = "userProfile.user.id")
  @Mapping(target = "acceptedOpinionCount", ignore = true)
  @Mapping(target = "profileImgDownloadUrl", ignore = true)
  UserProfileResponse toUserProfileResponseDto(UserProfile userProfile, long ranking);

  @Mapping(target = "nickname", source = "userProfile.user.nickname")
  @Mapping(target = "userId", source = "userProfile.user.id")
  @Mapping(target = "ranking", ignore = true)
  @Mapping(target = "acceptedOpinionCount", ignore = true)
  @Mapping(target = "profileImgDownloadUrl", ignore = true)
  UserProfileResponse toUserProfileResponseDto(UserProfile userProfile);

  @Mapping(target = "introduce", source = "user.userProfile.introduce")
  @Mapping(target = "knowHow", source = "user.userProfile.knowHow")
  @Mapping(target = "activityRegion", source = "user.userProfile.activityRegion")
  @Mapping(target = "userId", source = "user.id")
  @Mapping(target = "ranking", ignore = true)
  @Mapping(target = "acceptedOpinionCount", ignore = true)
  @Mapping(target = "profileImgDownloadUrl", ignore = true)
  UserProfileResponse toUserProfileResponseDto(User user);

  UserResponse toUserResponseDto(User user);

  @AfterMapping
  default void countAcceptedOpinion(User user, @MappingTarget UserProfileResponse userProfileResponse) {
    int count = user.getOpinionList().stream().mapToInt(Opinion::getCountAccept).sum();
    userProfileResponse.setAcceptedOpinionCount(count);
  }

  @AfterMapping
  default void countAcceptedOpinion(UserProfile userProfile, @MappingTarget UserProfileResponse userProfileResponse) {
    int count = userProfile.getUser().getOpinionList().stream().mapToInt(Opinion::getCountAccept).sum();
    userProfileResponse.setAcceptedOpinionCount(count);
  }
}
