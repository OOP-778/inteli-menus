package com.oop.intelimenus.impl;

import com.oop.intelimenus.events.ButtonClickEvent;
import com.oop.intelimenus.interfaces.MenuButton;
import com.oop.intelimenus.interfaces.MenuItemBuilder;
import com.oop.intelimenus.interfaces.attribute.Attribute;
import io.netty.util.internal.ConcurrentSet;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class IButton implements MenuButton {

    private ItemStack currentItem;
    private Consumer<ButtonClickEvent> onClick;
    private Map<String, MenuItemBuilder> states = new HashMap<>();
    private Set<Attribute> attributeSet = new ConcurrentSet<>();

    @Override
    public ItemStack getCurrentItem() {
        return currentItem;
    }

    @Override
    public MenuButton onClick(Consumer<ButtonClickEvent> event) {
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
}
