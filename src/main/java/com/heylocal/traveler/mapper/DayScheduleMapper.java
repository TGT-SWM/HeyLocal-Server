/**
 * packageName    : com.heylocal.traveler.mapper
 * fileName       : DayScheduleMapper
 * author         : 우태균
 * date           : 2022/09/21
 * description    : DaySchedule 엔티티 관련 Mapper
 */

package com.heylocal.traveler.mapper;

import com.heylocal.traveler.domain.plan.DaySchedule;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import static com.heylocal.traveler.dto.PlanDto.ScheduleRequest;

@Mapper(uses = {PlaceItemMapper.class}, builder = @Builder(disableBuilder = true))
public interface DayScheduleMapper {
  DayScheduleMapper INSTANCE = Mappers.getMapper(DayScheduleMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "dateTime", ignore = true)
  @Mapping(target = "plan", ignore = true)
  @Mapping(target = "placeItemList", source = "places")
  DaySchedule toEntity(ScheduleRequest scheduleRequest);
}
