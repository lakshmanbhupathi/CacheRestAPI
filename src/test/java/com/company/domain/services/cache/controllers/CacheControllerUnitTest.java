package com.company.domain.services.cache.controllers;

import com.company.domain.services.cache.dto.CacheDto;
import com.company.domain.services.cache.services.CacheService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class CacheControllerUnitTest {
    private CacheController cacheController;

    @Mock
    private CacheService cacheService;


    @Before
    public void setup() {
        initMocks(this);

        cacheController = new CacheController();
        cacheController.setCacheService(cacheService);
    }

    @Test
    public void testAdd() {
        CacheDto cacheDto = new CacheDto();
        cacheDto.setKey("key");
        cacheDto.setValue("value");

        //expectations
        when(cacheService.add(isA(CacheDto.class))).thenReturn(true);

        //execute
        boolean actualResponse = cacheController.add(cacheDto);

        //assertions
        Assert.assertTrue(actualResponse);
    }

    @Test
    public void testRemove() {
        CacheDto cacheDto = new CacheDto();
        cacheDto.setKey("key");

        //expectations
        when(cacheService.delete(isA(CacheDto.class))).thenReturn(true);

        //execute
        boolean actualResponse = cacheController.remove(cacheDto);

        //assertions
        Assert.assertTrue(actualResponse);
    }

    @Test
    public void testTake() {
        String expected = "value";

        //expectations
        when(cacheService.take()).thenReturn(expected);

        //execute
        Object actualResponse = cacheController.take();

        //assertions
        Assert.assertEquals(expected, actualResponse);
    }

    @Test
    public void testPeek() {
        String expected = "value";

        //expectations
        when(cacheService.peek()).thenReturn(expected);

        //execute
        Object actualResponse = cacheController.peek();

        //assertions
        Assert.assertEquals(expected, actualResponse);
    }


}
