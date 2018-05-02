package com.company.domain.services.cache.services;

import com.company.domain.services.cache.dto.CacheObject;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

@Service
public class CacheServiceImpl implements CacheService {
    private static BlockingDeque<CacheObject> deque;

    static {
        deque = new LinkedBlockingDeque<>();
    }

    @Override
    public boolean add(CacheObject cacheObject) {
        return deque.add(cacheObject);
    }

    @Override
    public CacheObject peek() {
        return deque.pollLast();
    }

    @Override
    public boolean delete(CacheObject cacheObject) {
        return deque.remove(cacheObject);
    }

    @Override
    public CacheObject take() throws InterruptedException {
        return deque.take();
    }
}
