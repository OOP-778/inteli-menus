package com.oop.intelimenus.listener;

import com.oop.intelimenus.menu.simple.IMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class MenuListener implements Listener {
    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        if (!(event.getInventory().getHolder() instanceof IMenu)) return;

        IMenu menu = (IMenu) event.getInventory().getHolder();
        menu.onOpen(event);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getInventory().getHolder() instanceof IMenu)) return;

        IMenu menu = (IMenu) event.getInventory().getHolder();
        menu.onClose(event);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getWhoClicked().getOpenInventory().getTopInventory() == null) return;
        if (!(event.getWhoClicked().getOpenInventory().getTopInventory().getHolder() instanceof IMenu)) return;
        if (event.getSlot() < 0) return;

        IMenu menu = (IMenu) event.getWhoClicked().getOpenInventory().getTopInventory().getHolder();
        if (event.getClickedInventory().getHolder() == menu)
            menu.onClick(event);
        else
            menu.onBottomClick(event);
    }
}
