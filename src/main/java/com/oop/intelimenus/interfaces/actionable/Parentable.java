package com.oop.intelimenus.interfaces.actionable;

import java.util.Optional;

public interface Parentable<T extends Parentable> {
    Optional<T> getParent();

    void setParent(T parent);
}
