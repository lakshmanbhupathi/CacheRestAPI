package com.company.domain.services.cache.services;

import com.company.domain.services.cache.dto.CacheDto;

public interface CacheService {
    boolean add(CacheDto cacheDto);

    Object peek();

    boolean delete(CacheDto cacheDto);

    Object take() throws InterruptedException;
}
