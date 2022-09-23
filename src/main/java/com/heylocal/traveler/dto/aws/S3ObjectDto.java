package com.heylocal.traveler.dto.aws;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class S3ObjectDto {
  private String key;
  private long size;
  private String eTag;
  private String sequencer;
}
