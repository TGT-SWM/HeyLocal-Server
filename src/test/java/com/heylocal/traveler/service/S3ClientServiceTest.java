package com.heylocal.traveler.service;

import com.amazonaws.services.s3.AmazonS3Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class S3ClientServiceTest {
  @Mock
  private AmazonS3Client amazonS3Client;
  private S3ClientService s3ClientService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    s3ClientService = new S3ClientService(amazonS3Client);
  }

  @Test
  @DisplayName("해당 Key의 오브젝트 제거")
  void removeObjectTest() {
    //GIVEN
    String objectKey = "my object key";

    //WHEN

    //THEN
    //성공 케이스 - 1 - 예외없이 잘 처리되는지
    assertDoesNotThrow(() -> s3ClientService.removeObject(objectKey));
  }
}