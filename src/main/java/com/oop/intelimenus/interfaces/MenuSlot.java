package com.oop.intelimenus.interfaces;

import com.oop.intelimenus.interfaces.attribute.AttributeHolder;

import java.util.Optional;

public interface MenuSlot<T extends MenuSlot, B extends MenuButton> extends AttributeHolder<T>, DataHolder<T> {
    /*
    Button that holds this slot
    */
    Optional<B> getHolder();

    /*
    Slot of the inventory
    */
    int getIndex();

    /*
    Set slot holder
    */
    void setHolder(B button);

    /*
    Set slot index
    */
    void setIndex(int index);
}
