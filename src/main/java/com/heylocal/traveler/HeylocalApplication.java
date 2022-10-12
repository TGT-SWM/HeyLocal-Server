/**
 * packageName    : com.heylocal.traveler
 * fileName       : HeylocalApplication
 * author         : 우태균
 * date           : 2022/08/06
 * description    : SpringBoot Application 실행 클래스
 */

package com.heylocal.traveler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HeylocalApplication {

	public static void main(String[] args) {
		SpringApplication.run(HeylocalApplication.class, args);
	}

}
