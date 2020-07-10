package com.oop.intelimenus.interfaces.actionable;

public interface Openable<T> {
    void open(T object, Runnable callback);
}
