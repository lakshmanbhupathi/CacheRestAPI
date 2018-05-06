package com.company.domain.services.cache.services;

import com.company.domain.services.cache.dto.CacheDto;
import com.company.domain.services.cache.exceptions.InternalException;
import com.company.domain.services.cache.map.LRUCacheMap;
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
public class CacheServiceImplTest {
    private CacheService cacheService;

    @Mock
    private LRUCacheMap cacheMap;

    @Before
    public void setup() {
        initMocks(this);

        cacheService = new CacheServiceImpl();
        ((CacheServiceImpl) cacheService).setMap(cacheMap);
    }

    @Test
    public void testAdd() {
        CacheDto cacheDto = new CacheDto();
        cacheDto.setKey("key");
        cacheDto.setValue("value");

        //expectations
        when(cacheMap.add(isA(String.class), isA(String.class))).thenReturn(true);

        //execute
        boolean actualResponse = cacheService.add(cacheDto);

        //assertions
        Assert.assertTrue(actualResponse);
    }

    @Test
    public void testRemove() {
        CacheDto cacheDto = new CacheDto();
        cacheDto.setKey("key");

        //expectations
        when(cacheMap.remove(isA(String.class))).thenReturn(true);

        //execute
        boolean actualResponse = cacheService.delete(cacheDto);

        //assertions
        Assert.assertTrue(actualResponse);
    }

    @Test
    public void testTake() throws InterruptedException {
        String expected = "value";
        ((CacheServiceImpl) cacheService).setTimeOutProperty("10");

        //expectations
        when(cacheMap.take(10)).thenReturn(expected);

        //execute
        Object actualResponse = cacheService.take();

        //assertions
        Assert.assertEquals(expected, actualResponse);
    }

    @Test
    public void testTakeWhenNoTimeOut() throws InterruptedException {
        String expected = "value";
        ((CacheServiceImpl) cacheService).setTimeOutProperty("0");

        //expectations
        when(cacheMap.take()).thenReturn(expected);

        //execute
        Object actualResponse = cacheService.take();

        //assertions
        Assert.assertEquals(expected, actualResponse);
    }

    @Test(expected = InternalException.class)
    public void testTakeWhenParseException() throws Exception {
        ((CacheServiceImpl) cacheService).setTimeOutProperty("0abc");

        //execute
        cacheService.take();
    }

    @Test(expected = InternalException.class)
    public void testTakeWhenInterruptedException() throws Exception {
        String expected = "value";
        ((CacheServiceImpl) cacheService).setTimeOutProperty("0");

        //expectations
        when(cacheMap.take()).thenThrow(new InterruptedException());

        //execute
        cacheService.take();
    }

    @Test
    public void testPeek() {
        String expected = "value";

        //expectations
        when(cacheMap.peek()).thenReturn(expected);

        //execute
        Object actualResponse = cacheService.peek();

        //assertions
        Assert.assertEquals(expected, actualResponse);
    }
}