package com.company.domain.services.cache.services;

import com.company.domain.services.cache.dto.CacheDto;
import com.company.domain.services.cache.exceptions.InternalException;
import com.company.domain.services.cache.map.LRUCacheMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;

/**
 * Implementation of {@link CacheService}
 * @author Lakshman Bhupathi
 */
@Service
public class CacheServiceImpl implements CacheService {

    @Autowired
    private LRUCacheMap<String, Object> map;

    // reads property from properties
    @Value("${cache.timeout}")
    private String timeOutProperty;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(CacheDto cacheDto) {
        return map.add(cacheDto.getKey(), cacheDto.getValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object peek() {
        return map.peek();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(CacheDto cacheDto) {
        return map.remove(cacheDto.getKey());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object take() {
        try {
            int timeOut = Integer.parseInt(timeOutProperty);

            return timeOut == 0 ? map.take() : map.take(timeOut);
        } catch (InterruptedException | NumberFormatException e) {
            e.printStackTrace();
            throw new InternalException(e.getMessage());
        }
    }
}
