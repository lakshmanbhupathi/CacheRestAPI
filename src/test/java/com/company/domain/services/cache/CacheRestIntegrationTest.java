package com.company.domain.services.cache;


import com.company.domain.services.cache.dto.CacheDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CacheRestIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testAddController() {
        CacheDto cacheDto = new CacheDto();
        cacheDto.setKey("key");
        cacheDto.setValue("value");

        ResponseEntity<Boolean> responseEntity =
                restTemplate.postForEntity("/cache/add", cacheDto, Boolean.class);
        Boolean response = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(response);

    }

    @Test
    public void testPeek() {
        String expectedValue = "value";

        CacheDto cacheDto = new CacheDto();
        cacheDto.setKey("key");
        cacheDto.setValue(expectedValue);

        restTemplate.postForEntity("/cache/add", cacheDto, Boolean.class);

        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/cache/peek", String.class);
        String actualResponse = responseEntity.getBody();

        assertEquals(expectedValue, actualResponse);
    }

    @Test
    public void testAddControllerDuplicationTest() {
        CacheDto cacheDto = new CacheDto();
        cacheDto.setKey("key");
        cacheDto.setValue("value");

        restTemplate.postForEntity("/cache/add", cacheDto, Boolean.class);

        ResponseEntity<Boolean> responseEntity =
                restTemplate.postForEntity("/cache/add", cacheDto, Boolean.class);
        Boolean response = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertFalse(response);
    }

    @Test
    public void testTakeController() {
        String expectedValue = "value";

        CacheDto cacheDto = new CacheDto();
        cacheDto.setKey("key");
        cacheDto.setValue(expectedValue);

        restTemplate.postForEntity("/cache/add", cacheDto, Boolean.class);

        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity("/cache/take", String.class);
        String response = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedValue, response);
    }

    @Test
    public void testRemoveController() {
        CacheDto cacheDto = new CacheDto();
        cacheDto.setKey("key");
        cacheDto.setValue("someOtherValue");

        restTemplate.postForEntity("/cache/add", cacheDto, Boolean.class);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        HttpEntity<CacheDto> cacheDtoHttpEntity = new HttpEntity<>(cacheDto, httpHeaders);

        ResponseEntity<Boolean> responseEntity =
                restTemplate.exchange("/cache/remove", HttpMethod.DELETE, cacheDtoHttpEntity, Boolean.class);
        Boolean response = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(response);
    }
    @Test
    public void testRemoveControllerWhenMethodNotAllowed() {
        CacheDto cacheDto = new CacheDto();
        cacheDto.setKey("key");
        cacheDto.setValue("someOtherValue");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        HttpEntity<CacheDto> cacheDtoHttpEntity = new HttpEntity<>(cacheDto, httpHeaders);

        ResponseEntity<String> responseEntity =
                restTemplate.exchange("/cache/remove", HttpMethod.POST, cacheDtoHttpEntity, String.class);
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, responseEntity.getStatusCode());
    }

}
