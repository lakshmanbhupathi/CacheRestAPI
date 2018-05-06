package com.company.domain.services.cache.services;

import com.company.domain.services.cache.dto.CacheDto;

/**
 * Service class for REST Cache API
 * @author Lakshman Bhupathi
 */
public interface CacheService {
    /**
     * Adds CacheDto into in-memory cache
     * ignores if already present
     *
     * @param cacheDto
     * @return true if inserted
     */
    boolean add(CacheDto cacheDto);

    /**
     * returns recently added element's value
     *
     * @return value of recently added
     */
    Object peek();

    /**
     * Removes element from in-memory cache
     *
     * @param cacheDto
     * @return true if removed successfully
     */
    boolean delete(CacheDto cacheDto);

    /**
     * returns recently added element's value
     * and removes from in memory cache.
     * <p>
     * if not present wait's until data available.
     * <p>
     * cache timeout is optional can be flexible to change in runtime from properties
     *
     * @return value of recently added element
     */
    Object take();
}
