package com.company.domain.services.cache.map;

import com.company.domain.services.cache.exceptions.CacheEmptyTimeOutException;
import com.company.domain.services.cache.exceptions.ContentNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.*;

public class ConcurrentCacheMapTest {
    private ConcurrentCacheMap<String, String> map;

    @Before
    public void setup() {
        map = new ConcurrentCacheMap<>();
    }

    @Test
    public void testAdd() {
        Assert.assertTrue(map.add("key","value"));

        // must be false as duplicate not allowed
        Assert.assertFalse(map.add("key","value"));

        // diff key same value
        Assert.assertTrue(map.add("key2","value"));

        Assert.assertTrue(map.add("key3","anotherValue"));

        Assert.assertEquals(3,map.size());
    }

    @Test
    public void testRemove() {
        // no elements so false
        Assert.assertFalse(map.remove("key"));

        map.add("key","value");
        Assert.assertTrue(map.remove("key"));

        map.add("key","value");
        map.add("key2","value");
        map.add("key3","value");

        Assert.assertTrue(map.remove("key2"));
        Assert.assertTrue(map.remove("key3"));

        Assert.assertEquals(1,map.size());
    }

    @Test
    public void testPeek(){
        map.add("key","value");

        Assert.assertEquals(map.peek(),"value");
    }

    @Test(expected = ContentNotFoundException.class)
    public void testPeekWhenNoElements(){
        map.peek();
    }

    @Test
    public void testTake() throws InterruptedException {
        map.add("key","value");

        Assert.assertEquals(map.take(),"value");
    }

    @Test
    public void testTakeWhenNoElements() throws InterruptedException, ExecutionException {
        String expected = "value";

        Callable<String> takeWaitTask = ()-> map.take();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future =  executorService.submit(takeWaitTask);
        Thread.sleep(500);

        map.add("key",expected);

        Assert.assertEquals(expected,future.get());
    }

    @Test
    public void testTakeLimitedTimeOut() throws InterruptedException {
        map.add("key","value");

        Assert.assertEquals(map.take(1),"value");
    }

    // shouldn't wait more than 1 second
    @Test(expected = CacheEmptyTimeOutException.class, timeout = 1001)
    public void testTakeLimitedTimeOutWhenNoElements() throws InterruptedException, ExecutionException {
        map.take(1);
    }
}
