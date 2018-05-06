package com.company.domain.services.cache;

import com.company.domain.services.cache.map.ConcurrentCacheMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CacheRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(CacheRestApplication.class, args);
	}

	@Bean
	ConcurrentCacheMap getConcurrentCacheMap(){
		return new ConcurrentCacheMap<String, Object>();
	}
}
