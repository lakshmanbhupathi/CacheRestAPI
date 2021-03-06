package com.company.domain.services.cache.map;

import com.company.domain.services.cache.exceptions.CacheEmptyTimeOutException;
import com.company.domain.services.cache.exceptions.ContentNotFoundException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Implementation of {@link LRUCacheMap}, also provides  Concurrency prone
 * designed such way that it can handle high concurrency
 * <p>
 * maintains insertion order by embedding Doubly LinkedList into Map's Entry
 *
 * @param <K> key
 * @param <V> value
 *
 * @author Lakshman Bhupathi
 */
public class ConcurrentCacheMap<K, V> implements LRUCacheMap<K, V> {

    private Entry<K, V> head;
    private Entry<K, V> tail;
    private ConcurrentHashMap<K, Entry<K, V>> map;

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private Lock readLock = readWriteLock.readLock();
    private Lock writeLock = readWriteLock.writeLock();
    private Condition isEmptyCondition = writeLock.newCondition();

    /**
     * Entry inner class for maintaining map
     * holds next and prev pointer for maintaining insertion order
     *
     * @param <K> key
     * @param <V> value
     */
    private class Entry<K, V> {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(K key, V value) {
        Entry<K, V> newEntry = new Entry<>(key, value);
        if (head == null && tail == null && map.isEmpty()) {
            return makeFirstEntry(key, newEntry);
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

    private boolean makeFirstEntry(K key, Entry<K, V> newEntry) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(K key) {
        if (map.containsKey(key)) {
            try {
                writeLock.lock();

                Entry entry = map.get(key);
                modifyPrev(entry);
                modifyNext(entry);
            } finally {
                writeLock.unlock();
            }
            map.remove(key);
            return true;
        } else {
            return false;
        }
    }

    private void modifyNext(Entry entry) {
        if (entry.next != null) {
            entry.next.prev = entry.prev;
        } else {
            tail = entry.prev;
        }
    }

    private void modifyPrev(Entry entry) {
        if (entry.prev != null) {
            entry.prev.next = entry.next;
        } else {
            head = entry.next;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V peek() {
        try {
            readLock.lock();
            if(map.isEmpty()){
                throw new ContentNotFoundException("No contents available");
            }

            return tail.value;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V take(int timeOut) throws InterruptedException {
        try {
            writeLock.lock();
            if (map.isEmpty()) {
                if (!isEmptyCondition.await(timeOut, TimeUnit.SECONDS)) {
                    throw new CacheEmptyTimeOutException();
                }
            }
            return takeLastInserted();
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V take() throws InterruptedException {
        try {
            writeLock.lock();
            if (map.isEmpty()) {
                isEmptyCondition.await();
            }
            return takeLastInserted();
        } finally {
            writeLock.unlock();
        }
    }

    private V takeLastInserted() {
        V response = tail.value;
        remove(tail.key);
        return response;
    }

    public int size(){
        return map.size();
    }
}
