package com.oop.intelimenus.trigger.types;

import com.oop.intelimenus.button.IButton;
import com.oop.intelimenus.menu.simple.IMenu;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;

public class ButtonClickTrigger implements MenuTrigger {

    @Getter
    private ClickType click;

    @Getter
    private InventoryAction action;

    @Getter
    private int slot;

    @Getter
    private IMenu menu;

    @Getter
    @Setter
    private boolean cancelled;

    @Getter
    private IButton button;

    @Getter
    private Player player;

    public ButtonClickTrigger(IMenu menu, IButton button, int slot, ClickType click, InventoryAction action, Player player) {
        this.menu = menu;
        this.slot = slot;
        this.click = click;
        this.action = action;
        this.button = button;
        this.player = player;
    }

    public boolean isRightClick() {
        return this.click.isRightClick();
    }

    public boolean isLeftClick() {
        return this.click.isLeftClick();
    }

    public boolean isShiftClick() {
        return this.click.isShiftClick();
    }
}
