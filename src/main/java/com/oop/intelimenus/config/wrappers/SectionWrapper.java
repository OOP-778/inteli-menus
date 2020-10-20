package com.oop.intelimenus.config.wrappers;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class SectionWrapper extends ValuableWrapper {

    private String key;
    private ConfigWrapper config;
    private SectionWrapper parent;

    /**
     * Creates section wrapper
     *
     * @param allValues should be all values of the wrapper hierarchy
     */
    protected SectionWrapper(String key, ConfigWrapper config, SectionWrapper parent, Map<String, Object> allValues) {
        super(allValues);
        this.key = key;
        this.config = config;
        this.parent = parent;
    }

    @Override
    protected void throwError(String message) throws IllegalStateException {
        config.throwError(" at section '" + getPath() + "': " + message);
    }

    public String getPath() {
        List<String> allParents = new ArrayList<>();
        _addParent(allParents);

        Collections.reverse(allParents);
        return String.join(".", allParents);
    }

    private void _addParent(List<String> strings) {
        strings.add(key);
        if (parent != null)
            parent._addParent(strings);
    }

    public void dumpAll() {
        System.out.println("== Section Values ==");
        getValues().forEach((key, value) -> System.out.println("at: " + key + " value: " + value));

        System.out.println("== Children of " + key + "==");
        getSections().forEach((key, value) -> {
            System.out.println("== Section: " + key + " ==");
            value.dumpAll();
        });
    }
}
