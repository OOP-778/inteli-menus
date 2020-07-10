package com.oop.intelimenus.events;

import com.oop.intelimenus.interfaces.Menu;
import com.oop.intelimenus.interfaces.MenuButton;
import lombok.Getter;
import org.bukkit.inventory.Inventory;

@Getter
public class ButtonClickEvent {

    private MenuButton button;
    private Menu menu;
    private Inventory inventory;

}
