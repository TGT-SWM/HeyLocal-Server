package com.heylocal.traveler.util.aws;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Date;

/**
 * AWS 의 Presign URL 을 제공
 */
@Component
@RequiredArgsConstructor
public class S3PresignUrlProvider {
  @Value("${cloud.aws.s3.bucket}")
  private String bucketName;
  @Value("${aws.presign.url-valid-millisecond}")
  private long expirationMillSec;
  private final AmazonS3Client amazonS3Client;

  /**
   * S3에 접근할 수 있는 Presign URL 을 획득하는 메서드
   * @param objectKeyName 작업할 Object 의 이름 (Object의 key)
   * @param httpMethod Presigned URL 의 기능 설정 (GET:다운로드, PUT: 업로드, DELETE: 삭제)
   * @return Presign Upload URL
   */
  public String getPresignedUrl(String objectKeyName, HttpMethod httpMethod) {
    if (isInvalidHttpMethod(httpMethod)) {
      throw new IllegalArgumentException("GET, PUT, HEAD, DELETE HTTP 메서드만 허용합니다.");
    }

    Date urlExpiration = new Date(new Date().getTime() + expirationMillSec);
    GeneratePresignedUrlRequest generatePresignedUrlRequest =
        new GeneratePresignedUrlRequest(bucketName, objectKeyName)
            .withMethod(httpMethod)
            .withExpiration(urlExpiration);
    if (httpMethod == HttpMethod.PUT) generatePresignedUrlRequest.withContentType("image/png");

    URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
    return url.toString();
  }

  private boolean isInvalidHttpMethod(HttpMethod method) {
    if (method == HttpMethod.GET) return false;
    if (method == HttpMethod.PUT) return false;
    if (method == HttpMethod.HEAD) return false;
    if (method == HttpMethod.DELETE) return false;
    return true;
  }

}
