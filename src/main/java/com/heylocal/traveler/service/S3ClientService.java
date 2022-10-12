/**
 * packageName    : com.heylocal.traveler.service
 * fileName       : S3ClientService
 * author         : 우태균
 * date           : 2022/09/26
 * description    : 서버에서 직접 S3 를 조작하는 서비스
 *                  S3를 직접 조작해야 하는 경우 이 클래스를 사용하면 된다.
 *                  Presigned URL 을 제공해야 하는 경우,
 *                  util.aws.S3PresignUrlProvider 를 사용하면 된다.
 */

package com.heylocal.traveler.service;

import com.amazonaws.services.s3.AmazonS3Client;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class S3ClientService {
  @Value("${cloud.aws.s3.bucket}")
  private String bucketName;
  private final AmazonS3Client amazonS3Client;

  /**
   * 해당 Key 를 갖는 오브젝트를 S3에서 제거하는 메서드
   * @param objectKey
   */
  public void removeObject(String objectKey) {
    amazonS3Client.deleteObject(bucketName, objectKey);
  }
}
