package com.oop.intelimenus.interfaces;

import java.util.Map;

public interface DataHolder<T> {
    Map<String, Object> getDataMap();

    /*
    Store data
    */
    default T addData(String key, Object value) {
        getDataMap().put(key, value);
        return (T) this;
    }

    /*
    Remove data
    */
    default T removeData(String key) {
        getDataMap().remove(key);
        return (T) this;
    }

    /*
    Check if it contains some data
    */
    default boolean hasData(String key) {
        return getDataMap().containsKey(key);
    }
}
