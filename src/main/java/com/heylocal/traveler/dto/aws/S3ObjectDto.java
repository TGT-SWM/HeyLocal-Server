/**
 * packageName    : com.heylocal.traveler.dto.aws
 * fileName       : S3ObjectDto
 * author         : 우태균
 * date           : 2022/09/23
 * description    : AWS S3의 Object Key 관련 DTO
 */

package com.heylocal.traveler.dto.aws;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class S3ObjectDto {
  private String key;
}
