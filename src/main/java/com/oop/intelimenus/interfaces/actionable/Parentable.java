package com.oop.intelimenus.interfaces.actionable;

public interface Parentable<T extends Parentable> {
    T getParent();
}
