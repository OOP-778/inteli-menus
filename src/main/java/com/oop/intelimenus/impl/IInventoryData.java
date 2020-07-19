package com.oop.intelimenus.impl;

import com.oop.intelimenus.interfaces.InventoryData;
import lombok.NonNull;
import org.bukkit.inventory.Inventory;

public class IInventoryData implements InventoryData {
    @Override
    public ISlot[] getSlots() {

    }

    @Override
    public @NonNull Inventory getInventory() {
        return null;
    }

    @Override
    public void updateTitle(String newTitle) {

    }

    @Override
    public void updateSlot(int slot) {

    }
}
