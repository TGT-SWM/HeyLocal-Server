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
   * S3에 Object 를 업로드할 수 있는 Presign URL 을 획득하는 메서드
   * @param uploadObjectKeyName 저장할 Object 의 이름 (Object의 key)
   * @return Presign Upload URL
   */
  public String getUploadUrl(String uploadObjectKeyName) {
    Date urlExpiration = new Date(new Date().getTime() + expirationMillSec);
    GeneratePresignedUrlRequest generatePresignedUrlRequest =
        new GeneratePresignedUrlRequest(bucketName, uploadObjectKeyName)
            .withMethod(HttpMethod.PUT)
            .withContentType("image/png")
            .withExpiration(urlExpiration);
    URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
    return url.toString();
  }

}
