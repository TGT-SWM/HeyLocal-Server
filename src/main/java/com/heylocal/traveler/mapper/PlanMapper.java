package com.heylocal.traveler.mapper;

import com.heylocal.traveler.domain.plan.DaySchedule;
import com.heylocal.traveler.domain.plan.Plan;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import static com.heylocal.traveler.dto.PlanDto.PlanPlacesResponse;
import static com.heylocal.traveler.dto.PlanDto.PlanResponse;

@Mapper(uses = {PlaceMapper.class}, builder = @Builder(disableBuilder = true))
public interface PlanMapper {
  PlanMapper INSTANCE = Mappers.getMapper(PlanMapper.class);

  @Mapping(target = "regionId", source = "plan.travelOn.region.id")
  @Mapping(target = "regionState", source = "plan.travelOn.region.state")
  @Mapping(target = "regionCity", source = "plan.travelOn.region.city")
  @Mapping(target = "startDate", source = "plan.travelOn.travelStartDate")
  @Mapping(target = "endDate", source = "plan.travelOn.travelEndDate")
  PlanResponse toPlanResponseDto(Plan plan);

  @Mapping(target = "date", source = "daySchedule.dateTime")
  @Mapping(target = "places", source = "daySchedule.placeItemList")
  PlanPlacesResponse toPlanPlacesResponseDto(DaySchedule daySchedule);
}
