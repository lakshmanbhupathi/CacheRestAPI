package com.company.domain.services.cache.services;

import com.company.domain.services.cache.dto.CacheObject;

public interface CacheService {
    boolean add(CacheObject cacheObject);

    CacheObject peek();

    boolean delete(CacheObject cacheObject);

    CacheObject take() throws InterruptedException;
}
