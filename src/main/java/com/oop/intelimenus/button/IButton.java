package com.oop.intelimenus.button;

import com.oop.intelimenus.component.Component;
import com.oop.intelimenus.interfaces.MenuButton;
import com.oop.intelimenus.menu.simple.IMenu;
import com.oop.intelimenus.slot.ISlot;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class IButton implements MenuButton<IButton> {

    @Setter
    private ItemStack currentItem;

    @Getter
    @Setter
    private ISlot parent;

    @Getter
    @Setter
    private IMenu currentMenu;

    @Getter
    private Map<Class, Component> componentMap = new ConcurrentHashMap<>();

    @Override
    public Optional<ItemStack> getCurrentItem() {
        return Optional.ofNullable(currentItem);
    }

    @Override
    public IButton clone() {
        IButton button = new IButton();
        button.currentItem = Optional.ofNullable(currentItem)
                .map(ItemStack::clone)
                .orElse(currentItem);

        button.componentMap = cloneComponents();
        button.currentMenu = currentMenu;
        button.parent = parent;
        button.componentMap.values().forEach(comp -> comp.onAdd(button));
        return button;
    }

    @Override
    public String toString() {
        return "IButton{" +
                "currentItem=" + currentItem +
                ", components=" + Arrays.toString(componentMap.values().stream().map(Objects::toString).toArray()) +
//                ", parent" + parent == null ? "null" : "present" +
//                ", menu" + currentMenu == null ? "null" : "present" +
                '}';
    }
}
