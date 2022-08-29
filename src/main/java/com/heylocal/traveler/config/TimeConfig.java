package com.heylocal.traveler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class TimeConfig {
	/**
	 * <pre>
	 * Clock 객체를 Bean으로 등록합니다.
	 * 현재 시간에 의존하는 기능의 경우
	 * Clock 객체를 주입받을 수 있도록 합니다.
	 * @return Clock 객체
	 * </pre>
	 */
	@Bean
	public Clock clock() {
		return Clock.systemDefaultZone();
	}
}
