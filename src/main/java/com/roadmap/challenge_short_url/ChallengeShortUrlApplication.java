package com.roadmap.challenge_short_url;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ChallengeShortUrlApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChallengeShortUrlApplication.class, args);
	}

}
