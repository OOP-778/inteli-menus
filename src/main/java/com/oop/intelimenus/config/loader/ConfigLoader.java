package com.oop.intelimenus.config.loader;

import com.oop.intelimenus.config.wrappers.ConfigWrapper;

import java.io.File;

@FunctionalInterface
public interface ConfigLoader {
    ConfigWrapper load(File file) throws Throwable;
}
