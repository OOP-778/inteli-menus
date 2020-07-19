package com.oop.intelimenus.impl;

import com.google.common.base.Preconditions;
import com.oop.intelimenus.interfaces.MenuButton;
import com.oop.intelimenus.interfaces.MenuSlot;
import com.oop.intelimenus.interfaces.attribute.Attribute;
import io.netty.util.internal.ConcurrentSet;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ISlot implements MenuSlot<ISlot, IButton> {
    @Getter
    @Setter
    private int index;

    @Getter
    private IButton holder;

    @Getter
    private Map<String, Object> dataMap = new ConcurrentHashMap<>();

    @Getter
    private Set<Attribute> attributes = new ConcurrentSet<>();

    @Override
    public Optional<IButton> getHolder() {
        return Optional.empty();
    }

    @Override
    public void setHolder(IButton button) {
        this.holder = button;
    }

    public static SlotBuilder builder() {
        return new SlotBuilder();
    }

    @Accessors(chain = true, fluent = true)
    public static class SlotBuilder {

        @Setter
        private IButton holder;

        @Setter
        private int index;

        private Set<Attribute> attributes = new HashSet<>();
        private Map<String, Object> dataMap = new ConcurrentHashMap<>();

        private SlotBuilder() {}

        public SlotBuilder addAttribute(Attribute attribute) {
            Preconditions.checkArgument(attribute.applyableToSlots(), "Attribute by id: " + attribute.getId() + " is not applyable to slots!");
            attributes.add(attribute);
            return this;
        }

        public SlotBuilder addData(@NonNull String key, @NonNull Object value) {
            dataMap.put(key, value);
            return this;
        }

        public ISlot build() {
            ISlot slot = new ISlot();
            slot.getAttributes().addAll(attributes);
            slot.setHolder(holder);
            slot.setIndex(index);
            slot.dataMap = dataMap;
            return slot;
        }
    }

}
