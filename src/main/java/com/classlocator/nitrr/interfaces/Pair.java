package com.classlocator.nitrr.interfaces;

/**
 * A generic Pair class that holds two values of different or same types.
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 */

public class Pair<K, V> {
    private final K key;
    private final V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}

