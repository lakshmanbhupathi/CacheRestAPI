package com.company.domain.services.cache;

import com.company.domain.services.cache.controllers.CacheController;
import com.company.domain.services.cache.map.ConcurrentCacheMap;
import com.company.domain.services.cache.map.LRUCacheMap;
import com.company.domain.services.cache.services.CacheService;
import com.company.domain.services.cache.services.CacheServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheRestApplicationTests {

	@Autowired
	private CacheController cacheController;

	@Autowired
	private CacheService cacheService;

	@Autowired
	private LRUCacheMap cacheMap;

	@Test
	public void contextLoadControllerTests() {
		assertThat(cacheController).isNotNull();
	}

	@Test
	public void contextLoadServiceTests() {
		assertThat(cacheService).isNotNull();
		assertThat(cacheService instanceof CacheServiceImpl).isTrue();
	}

	@Test
	public void contextLoadMapTests() {
		assertThat(cacheMap).isNotNull();
		assertThat(cacheMap instanceof ConcurrentCacheMap).isTrue();
	}
}
