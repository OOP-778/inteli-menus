package com.oop.intelimenus.interfaces;

public interface MenuUtil extends ReflectionUtil {
    void ensureSync(Runnable runnable);

    void async(Runnable runnable);
}
