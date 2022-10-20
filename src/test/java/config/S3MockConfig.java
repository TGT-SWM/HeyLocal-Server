package config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.findify.s3mock.S3Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class S3MockConfig {
    @Value("${cloud.aws.region.static}")
    String region;
    @Value("${cloud.aws.s3.bucket}")
    String bucket;

  /**
   * S3Mock 객체를 생성하여 빈으로 등록
   * @return
   */
  @Bean
  public S3Mock s3Mock() {
    return new S3Mock.Builder().withPort(8001).withInMemoryBackend().build();
  }

  /**
   *
   * @param s3Mock
   * @return
   */
  @Bean
  @Primary /* 실제 비즈니스 코드와 충돌할 것을 대비하여, 이 Bean 객체를 우선적으로 가져오도록 설정 */
  public AmazonS3 amazonS3(S3Mock s3Mock){
    s3Mock.start(); //Mocking한 S3를 로컬에서 실행
    AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration("http://localhost:8001", region);
    AmazonS3 client = AmazonS3ClientBuilder
        .standard()
        .withPathStyleAccessEnabled(true)
        .withEndpointConfiguration(endpoint)
        .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
        .build();
    client.createBucket(bucket);

    return client;
  }

}
