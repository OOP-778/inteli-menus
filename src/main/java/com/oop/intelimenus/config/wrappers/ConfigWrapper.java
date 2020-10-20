package com.oop.intelimenus.config.wrappers;

import lombok.Getter;

import java.io.File;
import java.util.Map;

@Getter
public class ConfigWrapper extends ValuableWrapper {
    private File file;

    /**
     * Creates a valuable wrapper
     * @param allValues should be all values of the wrapper hierarchy
     */
    public ConfigWrapper(File configFile, Map<String, Object> allValues) {
        super(allValues);
        this.file = configFile;
    }

    @Override
    protected void throwError(String message) throws IllegalStateException {
        throw new IllegalStateException("Error occurred with file `" + file.getName() + "' " + message);
    }

    public void dumpAll() {
        System.out.println("== Config Values ==");
        getValues().forEach((key, value) -> System.out.println("at: " + key + " value: " + value));

        System.out.println("== Sections ==");
        getSections().forEach((key, value) -> {
            System.out.println("== Section: " + key + " ==");
            value.dumpAll();
        });

    }

    public String getName() {
        return getFile().getName().split("\\.")[0];
    }
}