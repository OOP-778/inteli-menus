package com.oop.intelimenus.interfaces;

import com.oop.intelimenus.interfaces.attribute.AttributeHolder;

import java.util.Optional;

public interface MenuSlot extends AttributeHolder {
    /*
    Button that holds this slot
    */
    Optional<MenuButton> getHolder();

    /*
    Slot of the inventory
    */
    int getIndex();

    /*
    Set slot holder
    */
    void setHolder(MenuButton button);

    /*
    Set slot index
    */
    void setIndex(int index);
}
