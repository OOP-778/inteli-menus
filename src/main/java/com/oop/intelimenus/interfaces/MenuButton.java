package com.oop.intelimenus.interfaces;

import com.oop.intelimenus.component.ComponentHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public interface MenuButton<T extends MenuButton<T>> extends Cloneable, ComponentHolder<T> {
    /*
    Get current item that's displayed inside inventory
    Will return empty if not set
    */
    Optional<ItemStack> getCurrentItem();

    /*
    Cloning of button
    */
    T clone();
}
