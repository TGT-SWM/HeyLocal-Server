package com.heylocal.traveler.util.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class HttpClient {
  public static final int TIMEOUT_MILLI_SEC = 30000;

  public String get(String url) throws Exception {
    String result = "";
    RequestConfig config = RequestConfig.custom()
        .setConnectTimeout(TIMEOUT_MILLI_SEC)
        .setConnectionRequestTimeout(TIMEOUT_MILLI_SEC)
        .setSocketTimeout(TIMEOUT_MILLI_SEC).build();

    CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();

    try {
      HttpGet request = new HttpGet(url); //HTTP 요청 객체 생성

      CloseableHttpResponse response = httpClient.execute(request); //HTTP 응답 객체 생성

      //응답 받기
      BufferedReader rd = new BufferedReader(
          new InputStreamReader(response.getEntity().getContent())
      );
      String line = "";
      while ((line = rd.readLine()) != null) {
        result += line;
      }

      //연결 해제
      request.releaseConnection();
    } catch (Exception ex) {
      throw new Exception("HTTP REQUEST SEND ERROR");
    } finally {
      httpClient.close(); //HTTP 클라이언트 종료
    }

    return result;
  }
}