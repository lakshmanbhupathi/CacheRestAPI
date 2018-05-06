package com.company.domain.services.cache.map;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
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
    private Lock lock = new ReentrantLock();
    Condition isEmptyCondition = lock.newCondition();
    Condition isFullCondition = lock.newCondition();

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
            lock.lock();
            newEntry.prev = tail;
            tail.next = newEntry;
            tail = newEntry;
        } finally {
            lock.unlock();
        }
        if (map.putIfAbsent(key, newEntry) == null) {
            return true;
        }

        return false;
    }

    public boolean remove(K key) {
        if (map.contains(key)) {
            try {
                lock.lock();

                Entry entry = map.get(key);
                entry.prev.next = entry.next;
                entry.next.prev = entry.prev;
            } finally {
                lock.unlock();
            }
            map.remove(key);
            return true;
        } else {
            return false;
        }
    }

}
