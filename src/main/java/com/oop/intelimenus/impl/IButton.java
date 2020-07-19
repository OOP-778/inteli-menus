package com.oop.intelimenus.impl;

import com.oop.intelimenus.events.ButtonClickEvent;
import com.oop.intelimenus.interfaces.MenuButton;
import com.oop.intelimenus.interfaces.MenuItemBuilder;
import com.oop.intelimenus.interfaces.attribute.Attribute;
import io.netty.util.internal.ConcurrentSet;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class IButton implements MenuButton<IButton> {

    private ItemStack currentItem;
    private Consumer<ButtonClickEvent> onClick;
    private Map<String, MenuItemBuilder> states = new HashMap<>();
    private Set<Attribute> attributeSet = new ConcurrentSet<>();
    private Map<String, Object> dataMap = new ConcurrentHashMap<>();

    @Override
    public Optional<ItemStack> getCurrentItem() {
        return Optional.ofNullable(currentItem);
    }

    @Override
    public IButton onClick(Consumer<ButtonClickEvent> event) {
        this.onClick = event;
        return this;
    }

    @Override
    public Map<String, MenuItemBuilder> getStates() {
        return states;
    }

    @Override
    public Collection<Attribute> getAttributes() {
        return attributeSet;
    }

    @Override
    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    @Override
    public IButton clone() {
        IButton button = new IButton();
        button.currentItem = currentItem.clone();
        button.onClick = onClick;

        states.forEach((key, state) -> button.states.put(key, state.clone()));
        button.attributeSet.addAll(attributeSet);
        button.dataMap.putAll(dataMap);

        return button;
    }
}
