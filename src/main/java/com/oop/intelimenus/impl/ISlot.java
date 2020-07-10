package com.oop.intelimenus.impl;

import com.google.common.base.Preconditions;
import com.oop.intelimenus.interfaces.MenuButton;
import com.oop.intelimenus.interfaces.MenuSlot;
import com.oop.intelimenus.interfaces.attribute.Attribute;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ISlot implements MenuSlot {

    @Getter
    @Setter
    private int index;

    @Override
    public Optional<MenuButton> getHolder() {
        return Optional.empty();
    }

    @Override
    public void setHolder(MenuButton button) {

    }

    @Override
    public Collection<Attribute> getAttributes() {
        return null;
    }

    public static SlotBuilder builder() {
        return new SlotBuilder();
    }

    @Accessors(chain = true, fluent = true)
    public static class SlotBuilder {

        @Setter
        private MenuButton holder;

        @Setter
        private int index;

        private Set<Attribute> attributes = new HashSet<>();

        private SlotBuilder() {}

        public SlotBuilder addAttribute(Attribute attribute) {
            Preconditions.checkArgument(attribute.applyableToSlots(), "Attribute by id: " + attribute.getId() + " is not applyable to slots!");
            attributes.add(attribute);
            return this;
        }

        public ISlot build() {
            ISlot slot = new ISlot();
            slot.getAttributes().addAll(attributes);
            slot.setHolder(holder);
            slot.setIndex(index);
            return slot;
        }
    }

}
