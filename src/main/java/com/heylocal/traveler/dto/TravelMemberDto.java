/**
 * packageName    : com.heylocal.traveler.dto
 * fileName       : TravelMemberDto
 * author         : 우태균
 * date           : 2022/09/14
 * description    : 여행On 질문 항목 - 동행인 관련 DTO
 */

package com.heylocal.traveler.dto;

import com.heylocal.traveler.domain.travelon.list.MemberType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class TravelMemberDto {
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Schema(description = "여행 On의 여행인원 정보 응답 DTO")
  public static class TravelMemberResponse {
    private long id;
    private MemberType type;
  }
}
