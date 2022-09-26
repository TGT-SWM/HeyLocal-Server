package com.heylocal.traveler.dto.aws;

import com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent.*;

public class S3PresignedUrlDto {
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Schema(description = "답변 이미지 수정용 Presigned URL DTO")
  public static class OpinionImgUpdateUrl {
    private ImageContentType imgType;
    private List<String> putUrls = new ArrayList<>();
    private List<String> deleteUrls = new ArrayList<>();
  }
}
