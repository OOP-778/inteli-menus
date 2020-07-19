package com.oop.intelimenus.impl;

import com.oop.intelimenus.interfaces.MenuButton;
import java.util.function.Supplier;

public class IButtons {
    public static final Supplier<IButton> RETURN_BUTTON = () -> new IButton()
            .onClick(event -> event.getMenu().returnAction(null));

    public static final Supplier<IButton> CLOSE_BUTTON = () -> new IButton()
            .onClick(event -> event.getMenu().closeAction(null));
}
