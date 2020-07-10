package com.oop.intelimenus.interfaces;

import com.oop.intelimenus.events.ButtonClickEvent;
import com.oop.intelimenus.interfaces.attribute.AttributeHolder;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public interface MenuButton extends AttributeHolder, Stateable<MenuButton>, Cloneable {
    /*
    Get current item that's displayed inside inventory
    */
    ItemStack getCurrentItem();

    /*
    When player clicks this button
    */
    MenuButton onClick(Consumer<ButtonClickEvent> event);
}
