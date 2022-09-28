package com.heylocal.traveler.dto.aws;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent.ImageContentType;

public class S3PresignedUrlDto {
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Schema(description = "답변 이미지 수정용 Presigned URL DTO")
  public static class OpinionImgUpdateUrl {
    private ImageContentType imgType;
    @Builder.Default
    private List<String> newPutUrls = new ArrayList<>();
    @Builder.Default
    private List<String> updatePutUrls = new ArrayList<>();
    @Builder.Default
    private List<String> deleteUrls = new ArrayList<>();
  }
}
