package com.company.domain.services.cache.map;

public interface LRUCacheMap<K, V> {
    boolean add(K key, V value);

    boolean remove(K key);

    V peek();

    V take() throws InterruptedException;
}
