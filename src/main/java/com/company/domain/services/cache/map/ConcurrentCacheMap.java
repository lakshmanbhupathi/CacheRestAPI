package com.company.domain.services.cache.map;

import com.company.domain.services.cache.exceptions.CacheEmptyTimeOutException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.*;

public class ConcurrentCacheMap<K, V> {

    private Entry<K, V> head;

    private Entry<K, V> tail;
    private ConcurrentHashMap<K, Entry<K, V>> map;

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private Lock readLock = readWriteLock.readLock();
    private Lock writeLock = readWriteLock.writeLock();
    private Condition isEmptyCondition = writeLock.newCondition();

    class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;
        Entry<K, V> prev;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public ConcurrentCacheMap() {
        map = new ConcurrentHashMap<>();
    }

    public boolean add(K key, V value) {
        Entry<K, V> newEntry = new Entry<>(key, value);
        if (head == null && tail == null && map.isEmpty()) {
            try {
                writeLock.lock();

                map.put(key, newEntry);
                head = tail = newEntry;
                isEmptyCondition.signal();
                return true;
            } finally {
                writeLock.unlock();
            }
        }

        if (map.putIfAbsent(key, newEntry) == null) {
            try {
                writeLock.lock();
                newEntry.prev = tail;
                tail.next = newEntry;
                tail = newEntry;
            } finally {
                writeLock.unlock();
            }
            return true;
        }

        return false;
    }

    public boolean remove(K key) {
        if (map.containsKey(key)) {
            try {
                writeLock.lock();

                Entry entry = map.get(key);

                if (entry.prev != null) {
                    entry.prev.next = entry.next;
                } else {
                    head = entry.next;
                }

                if (entry.next != null) {
                    entry.next.prev = entry.prev;
                } else{
                    tail = entry.prev;
                }
            } finally {
                writeLock.unlock();
            }
            map.remove(key);
            return true;
        } else {
            return false;
        }
    }

    public V peek() {
        try {
            readLock.lock();
            return tail.value;
        } finally {
            readLock.unlock();
        }
    }

    public V take() throws InterruptedException {
        try {
            writeLock.lock();
            if (map.isEmpty()) {
                if (!isEmptyCondition.await(10, TimeUnit.SECONDS)) {
                    throw new TimeoutException();
                }
            }
            V response = tail.value;
            remove(tail.key);
            return response;
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw e;
        } catch (TimeoutException e) {
            e.printStackTrace();
            throw new CacheEmptyTimeOutException();
        } finally {
            writeLock.unlock();
        }
    }
}
