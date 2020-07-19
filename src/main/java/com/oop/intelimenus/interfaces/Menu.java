package com.oop.intelimenus.interfaces;

import com.oop.intelimenus.interfaces.actionable.Actionable;
import com.oop.intelimenus.interfaces.attribute.AttributeHolder;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.function.Consumer;

public interface Menu<M extends Menu, S extends MenuSlot, B extends MenuButton> extends InventoryHolder, Actionable<Menu>, Comparable<Menu>, AttributeHolder<M> {
    /*
    Get viewer of the menu
    */
    Player getViewer();

    /*
    Get slots of menu
    */
    S[] getSlots();

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
    M setSlot(S slot);

    /*
    Set menu slot to button
    */
    M setSlot(int slot, B button);

    /*
    Edit slots from starting slot and ending
    */
    default M applyToSlots(int start, int end, @NonNull Consumer<S> consumer) {
        for (int slot = start; slot < end; slot++) {
            if (getSlots().length < slot) break;
            S menuSlot = getSlots()[slot];
            consumer.accept(menuSlot);
        }

        return (M) this;
    }
}
