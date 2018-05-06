package com.company.domain.services.cache.map;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.*;

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

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Lock readLock = readWriteLock.readLock();
    private Lock writeLock = readWriteLock.writeLock();

    Condition isEmptyCondition = writeLock.newCondition();

    ConcurrentCacheMap() {
        map = new ConcurrentHashMap<>();
    }

    public boolean add(K key, V value) {
        Entry<K, V> newEntry = new Entry<>(key, value);
        if (head == null && tail == null && map.isEmpty()) {
            map.put(key, newEntry);
            head = tail = newEntry;
            isEmptyCondition.signal();
            return true;
        }

        try {
            writeLock.lock();
            newEntry.prev = tail;
            tail.next = newEntry;
            tail = newEntry;
        } finally {
            writeLock.unlock();
        }
        if (map.putIfAbsent(key, newEntry) == null) {
            return true;
        }

        return false;
    }

    public boolean remove(K key) {
        if (map.contains(key)) {
            try {
                writeLock.lock();

                Entry entry = map.get(key);
                entry.prev.next = entry.next;
                entry.next.prev = entry.prev;
            } finally {
                writeLock.unlock();
            }
            map.remove(key);
            return true;
        } else {
            return false;
        }
    }

    public V peek(){
        try {
            readLock.lock();
            return tail.value;
        } finally {
            readLock.unlock();
        }
    }

}
