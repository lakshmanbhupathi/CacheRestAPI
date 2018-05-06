package com.company.domain.services.cache.services;

import com.company.domain.services.cache.dto.CacheDto;
import com.company.domain.services.cache.exceptions.InternalException;
import com.company.domain.services.cache.map.LRUCacheMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CacheServiceImpl implements CacheService {

    @Autowired
    private LRUCacheMap<String, Object> map;

    @Override
    public boolean add(CacheDto cacheDto) {
        return map.add(cacheDto.getKey(), cacheDto.getValue());
    }

    @Override
    public Object peek() {
        return map.peek();
    }

    @Override
    public boolean delete(CacheDto cacheDto) {
        return map.remove(cacheDto.getKey());
    }

    @Override
    public Object take() {
        try {
            return map.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new InternalException(e.getMessage());
        }
    }
}
