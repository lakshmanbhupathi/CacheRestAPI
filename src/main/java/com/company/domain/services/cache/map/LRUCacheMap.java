package com.company.domain.services.cache.map;

import com.company.domain.services.cache.exceptions.ContentNotFoundException;

/**
 * LRUCacheMap LeastRecentlyUsed Cache Map stores data in key and value pairs.
 * <p>
 * which uses hash table which facilitates operations like lookup, add, remove on constant times.
 * don't accepts duplication of key
 *
 * @param <K> Key
 * @param <V> Value
 * @author Lakshman Bhupathi
 */
public interface LRUCacheMap<K, V> {
    /**
     * Adds key and value into in-memory cache
     * ignores if already present
     *
     * @param key   key
     * @param value value
     * @return true if inserted
     */
    boolean add(K key, V value);

    /**
     * Removes element from in-memory cache
     *
     * @param key key
     * @return true if removed successfully
     */
    boolean remove(K key);

    /**
     * returns recently added element's value
     *
     * @return value of recently added
     * @throws {@link ContentNotFoundException} runtimeException if no elements are found
     */
    V peek();

    /**
     * returns recently added element's value
     * and removes from in-memory cache.
     * <p>
     * if data not present wait's until data available, wait until passed timeOut passed
     * if not then throws {@link com.company.domain.services.cache.exceptions.CacheEmptyTimeOutException}
     *
     * @param timeOut no. of seconds to wait if no data available
     * @return value of recently added element
     * @throws InterruptedException thread is interrupted while in wait state
     */
    V take(int timeOut) throws InterruptedException;

    /**
     * returns recently added element's value
     * and removes from in-memory cache.
     * <p>
     * if not present wait's until data available.
     *
     * @return value of recently added element
     * @throws InterruptedException thread is interrupted while in wait state
     */
    V take() throws InterruptedException;
}
