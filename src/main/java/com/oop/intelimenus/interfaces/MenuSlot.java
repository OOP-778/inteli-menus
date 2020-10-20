package com.oop.intelimenus.interfaces;

import com.google.common.collect.Sets;
import com.oop.intelimenus.attribute.AttributeComponent;
import com.oop.intelimenus.component.Component;
import com.oop.intelimenus.component.ComponentHolder;
import com.oop.intelimenus.data.DataComponent;
import com.oop.intelimenus.trigger.TriggerComponent;

import java.util.Optional;
import java.util.Set;

public interface MenuSlot<T extends MenuSlot<T, B>, B extends MenuButton<B>> extends Cloneable, ComponentHolder<T> {

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

    /*
    Clone the slot
    */
    T clone();
}
