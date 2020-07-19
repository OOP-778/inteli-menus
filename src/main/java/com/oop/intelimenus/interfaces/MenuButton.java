package com.oop.intelimenus.interfaces;

import com.oop.intelimenus.events.ButtonClickEvent;
import com.oop.intelimenus.interfaces.attribute.AttributeHolder;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;
import java.util.function.Consumer;

public interface MenuButton<T extends MenuButton> extends AttributeHolder<T>, Stateable<T>, Cloneable, DataHolder<T> {
    /*
    Get current item that's displayed inside inventory
    Will return empty if not set
    */
    Optional<ItemStack> getCurrentItem();

    /*
    When player clicks this button
    */
    T onClick(Consumer<ButtonClickEvent> event);

    /*
    Cloning of button
    */
    T clone();
}
