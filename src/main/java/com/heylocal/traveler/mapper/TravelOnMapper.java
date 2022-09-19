package com.heylocal.traveler.mapper;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.travelon.TravelOn;
import com.heylocal.traveler.domain.travelon.list.AccommodationType;
import com.heylocal.traveler.domain.travelon.list.DrinkType;
import com.heylocal.traveler.domain.travelon.list.FoodType;
import com.heylocal.traveler.domain.travelon.list.MemberType;
import com.heylocal.traveler.domain.user.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Set;

import static com.heylocal.traveler.dto.TravelOnDto.*;
import static com.heylocal.traveler.dto.TravelTypeGroupDto.TravelTypeGroupRequest;

@Mapper(uses = {RegionMapper.class, UserMapper.class, TravelMemberMapper.class, HopeAccommodationMapper.class, HopeDrinkMapper.class, HopeFoodMapper.class, TravelTypeGroupMapper.class},
    builder = @Builder(disableBuilder = true)
)
public interface TravelOnMapper {
  TravelOnMapper INSTANCE = Mappers.getMapper(TravelOnMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "region", source = "region")
  @Mapping(target = "author", source = "author")
  @Mapping(target = "views", constant = "0")
  @Mapping(target = "opinionList", ignore = true)
  @Mapping(target = "plan", ignore = true)
  @Mapping(target = "travelMemberSet", ignore = true)
  @Mapping(target = "hopeAccommodationSet", ignore = true)
  @Mapping(target = "hopeFoodSet", ignore = true)
  @Mapping(target = "hopeDrinkSet", ignore = true)
  @Mapping(target = "travelTypeGroup", ignore = true)
  TravelOn toEntity(TravelOnRequest travelOnRequest, User author, Region region);

  @Mapping(target = "createdDateTime", source = "createdDate")
  @Mapping(target = "modifiedDate", source = "modifiedDate")
  TravelOnResponse toTravelOnResponseDto(TravelOn travelOn);

  @Mapping(target = "createdDateTime", source = "createdDate")
  @Mapping(target = "opinionQuantity", expression = "java(travelOn.getOpinionList().size())")
  TravelOnSimpleResponse toTravelOnSimpleResponseDto(TravelOn travelOn);

  @AfterMapping
  default void updateEntityHopeDrinkSet(TravelOnRequest travelOnRequest, @MappingTarget TravelOn travelOn) {
    Set<DrinkType> drinkTypeSet = travelOnRequest.getDrinkTypeSet();

    drinkTypeSet.stream().forEach(
        (item) -> HopeDrinkMapper.INSTANCE.toEntity(travelOn, item)
    );
  }

  @AfterMapping
  default void updateEntityHopeFoodSet(TravelOnRequest travelOnRequest, @MappingTarget TravelOn travelOn) {
    Set<FoodType> foodTypeSet = travelOnRequest.getFoodTypeSet();

    foodTypeSet.stream().forEach(
        (item) -> HopeFoodMapper.INSTANCE.toEntity(travelOn, item)
    );
  }

  @AfterMapping
  default void updateEntityHopeAccommodationSet(TravelOnRequest travelOnRequest, @MappingTarget TravelOn travelOn) {
    Set<AccommodationType> accommodationTypeSet = travelOnRequest.getAccommodationTypeSet();

    accommodationTypeSet.stream().forEach(
        (item) -> HopeAccommodationMapper.INSTANCE.toEntity(travelOn, item)
    );
  }

  @AfterMapping
  default void updateEntityTravelMemberSet(TravelOnRequest travelOnRequest, @MappingTarget TravelOn travelOn) {
    Set<MemberType> memberTypeSet = travelOnRequest.getMemberTypeSet();

    memberTypeSet.stream().forEach(
        (item) -> TravelMemberMapper.INSTANCE.toEntity(travelOn, item)
    );
  }

  @AfterMapping
  default void updateEntityTravelTypeGroup(TravelOnRequest travelOnRequest, @MappingTarget TravelOn travelOn) {
    TravelTypeGroupRequest travelTypeGroupRequest = travelOnRequest.getTravelTypeGroup();
    TravelTypeGroupMapper.INSTANCE.toEntity(travelTypeGroupRequest, travelOn);
  }
}
