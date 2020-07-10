package com.oop.intelimenus.interfaces;

import com.oop.intelimenus.interfaces.actionable.Actionable;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public interface Menu<T extends Menu> extends InventoryHolder, Actionable<Menu> {
    /*
    Get viewer of the menu
    */
    Player getViewer();

    /*
    Get current inventory data
    */
    InventoryData getInventoryData();

    /*
    Set current inventory data
    */
    InventoryData setInventoryData(@NonNull InventoryData data);

    /*
    When menu gets clicked
    */
    void onClick(InventoryClickEvent event);

    /*
    When menu gets closed
    */
    void onClose(InventoryCloseEvent event);

    /*
    When item gets dragged out & in menu
    */
    void onDrag(InventoryDragEvent event);

    /*
    When menu gets open
    */
    void onOpen(InventoryOpenEvent event);

    /*
    Build the menu
    */
    Inventory buildMenu();

    @Override
    default Inventory getInventory() {
        return getInventory(false);
    }

    /*
    Get inventory of the menu
    */
    Inventory getInventory(boolean rebuild);

    /*
    Set menu slot
    */
    T setSlot(MenuSlot slot);

    /*
    Set menu slot to button
    */
    T setSlot(int slot, MenuButton button);
}
