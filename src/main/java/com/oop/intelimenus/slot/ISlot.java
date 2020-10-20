package com.oop.intelimenus.slot;

import com.oop.intelimenus.button.IButton;
import com.oop.intelimenus.component.Component;
import com.oop.intelimenus.interfaces.MenuSlot;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class ISlot implements MenuSlot<ISlot, IButton> {
    @Getter
    @Setter
    private int index;

    private IButton holder;

    @Getter
    private Map<Class, Component> componentMap = new HashMap<>();

    public ISlot(int index) {
        this.index = index;
    }

    @Override
    public Optional<IButton> getHolder() {
        return Optional.ofNullable(holder);
    }

    @Override
    public void setHolder(IButton button) {
        this.holder = button;
    }

    @Override
    public ISlot clone() {
        ISlot slot = new ISlot(index);
        slot.componentMap = cloneComponents();
        slot.componentMap.values().forEach(comp -> comp.onAdd(this));
        slot.holder = getHolder().map(IButton::clone).orElse(null);
        return slot;
    }

    @Override
    public String toString() {
        return "ISlot{" +
                "index=" + index +
                ", holder=" + holder +
                ", componentMap=" + Arrays.toString(componentMap.values().stream().map(Objects::toString).toArray()) +
                '}';
    }
}
