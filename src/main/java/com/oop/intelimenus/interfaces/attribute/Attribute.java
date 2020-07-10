package com.oop.intelimenus.interfaces.attribute;

import com.oop.intelimenus.interfaces.Menu;
import com.oop.intelimenus.interfaces.MenuButton;
import com.oop.intelimenus.interfaces.MenuSlot;

public interface Attribute {

    String getId();

    boolean applyableToMenus();

    boolean applyableToSlots();

    boolean applyableToButtons();

    default boolean accepts(AttributeHolder holder) {
        if (holder instanceof MenuButton)
            return applyableToButtons();
        else if (holder instanceof Menu)
            return applyableToMenus();
        else if (holder instanceof MenuSlot)
            return applyableToSlots();

        throw new IllegalStateException("Attribute is not yet implemented on " + holder.getClass().getSimpleName());
    }
}
