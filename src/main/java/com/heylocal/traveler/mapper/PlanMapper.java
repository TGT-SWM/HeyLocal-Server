package com.heylocal.traveler.mapper;

import com.heylocal.traveler.domain.plan.DaySchedule;
import com.heylocal.traveler.domain.plan.Plan;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import static com.heylocal.traveler.dto.PlanDto.PlanPlacesResponse;
import static com.heylocal.traveler.dto.PlanDto.PlanResponse;

@Mapper(uses = {PlaceItemMapper.class}, builder = @Builder(disableBuilder = true))
public interface PlanMapper {
  PlanMapper INSTANCE = Mappers.getMapper(PlanMapper.class);

  @Mapping(target = "regionId", source = "plan.region.id")
  @Mapping(target = "regionState", source = "plan.region.state")
  @Mapping(target = "regionCity", source = "plan.region.city")
  @Mapping(target = "startDate", source = "plan.travelStartDate")
  @Mapping(target = "endDate", source = "plan.travelEndDate")
  PlanResponse toPlanResponseDto(Plan plan);

  @Mapping(target = "date", source = "daySchedule.dateTime")
  @Mapping(target = "places", source = "daySchedule.placeItemList")
  PlanPlacesResponse toPlanPlacesResponseDto(DaySchedule daySchedule);
}
