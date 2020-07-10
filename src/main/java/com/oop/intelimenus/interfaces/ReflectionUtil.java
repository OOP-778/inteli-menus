package com.oop.intelimenus.interfaces;

import java.lang.reflect.Method;

public interface ReflectionUtil {
    Method getMethod(Class clazz, String methodName, Object[] args);

    Class getClass(String path);
}
