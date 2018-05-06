package com.company.domain.services.cache.map;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentCacheMap<K, V> {

    public static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;
        Entry<K, V> prev;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

    }

    private Entry<K, V> head;
    private Entry<K, V> tail;

    private ConcurrentHashMap<K, Entry<K, V>> map;
    private Lock lock;

    ConcurrentCacheMap() {
        map = new ConcurrentHashMap<>();
        lock = new ReentrantLock();
    }

    public boolean add(K key, V value) {
        Entry<K, V> newEntry = new Entry<>(key, value);
        if (head == null && tail == null && map.isEmpty()) {
            map.put(key, newEntry);
            head = tail = newEntry;
            return true;
        }

        try {
            lock.lock();
            newEntry.prev = tail;
            tail.next = newEntry;
            tail = newEntry;
        } finally {
            lock.unlock();
        }
        if(map.putIfAbsent(key,newEntry) == null){
            return true;
        }

        return false;
    }

}
