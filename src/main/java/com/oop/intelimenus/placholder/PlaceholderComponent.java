package com.oop.intelimenus.placholder;

import com.oop.intelimenus.component.Component;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class PlaceholderComponent implements Component<PlaceholderComponent> {

    @Getter
    private Set<Function<String, String>> placeholders = new HashSet<>();

    @Override
    public PlaceholderComponent clone() {
        PlaceholderComponent component = new PlaceholderComponent();
        component.placeholders.addAll(placeholders);
        return component;
    }
}
