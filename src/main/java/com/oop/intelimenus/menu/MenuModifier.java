package com.oop.intelimenus.menu;

import com.oop.intelimenus.interfaces.Modifier;
import com.oop.intelimenus.menu.simple.IMenu;
import com.oop.intelimenus.trigger.types.ButtonClickTrigger;

public interface MenuModifier<T extends IMenu> extends Modifier<T> {

    void preClick(ButtonClickTrigger clickTrigger);

    void postClick(ButtonClickTrigger clickTrigger);

}
