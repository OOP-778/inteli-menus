package com.oop.intelimenus.interfaces.animator;

import java.util.List;
import java.util.function.Consumer;

public interface Animator<T> {
    void addFrame(Consumer<T> frame);

    List<Consumer<T>> getFrames();
}
