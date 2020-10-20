package com.oop.intelimenus.menu.simple;

import com.oop.intelimenus.attribute.AttributeComponent;
import com.oop.intelimenus.attribute.Attributes;
import com.oop.intelimenus.interfaces.InventoryData;
import com.oop.intelimenus.interfaces.MenuItemBuilder;
import com.oop.intelimenus.slot.ISlot;
import com.oop.intelimenus.util.InventoryUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class IInventoryData implements InventoryData {

    @Getter
    private ISlot[] slots;

    @Getter
    private Inventory inventory;

    @Getter
    private IMenu menu;

    public IInventoryData(IMenu menu, int size, Inventory inventory) {
        slots = new ISlot[size];
        for (int i = 0; i < slots.length; i++)
            slots[i] = new ISlot(i);
        this.menu = menu;
        this.inventory = inventory;
    }

    @Override
    public void updateTitle(String newTitle) {
        if (!menu.isCurrentlyOpen()) {
            System.out.println("setting new title for menu without open: " + newTitle);
            inventory = copy(newTitle);
        }
        else
            InventoryUtil.updateTitle(inventory, menu.getViewer().orElse(null), newTitle);
    }

    @Override
    public void updateSlots(int... slots) {
        if (inventory == null) return;

        boolean hasOpened = menu.isCurrentlyOpen();
        for (int slot : slots) {
            ItemStack itemStack;
            Optional<MenuItemBuilder> optional = menu.requestItem(slot);
            if (!optional.isPresent())
                itemStack = new ItemStack(Material.AIR);
            else
                itemStack = optional.get().getItem();

            if (hasOpened)
                InventoryUtil.updateItem(menu.getViewer().orElse(null), slot, itemStack);
            else
                inventory.setItem(slot, itemStack);
        }
    }

    @Override
    public Player getViewer() {
        return menu.getViewer().orElse(null);
    }

    public void getDataFrom(IInventoryData data) {
        this.slots = data.slots;
    }

    public ISlot[] getFreeSlots() {
        List<ISlot> freeSlots = new LinkedList<>();
        for (ISlot slot : slots)
            if (!slot.getHolder().isPresent() || slot.getHolder().get().getComponent(AttributeComponent.class).map(ac -> ac.hasAttribute(Attributes.PLACEHOLDER)).orElse(false))
                freeSlots.add(slot);
        return freeSlots.toArray(new ISlot[0]);
    }

    public Optional<ISlot> findSlot(Predicate<ISlot> slotPredicate) {
        return Arrays.stream(slots).filter(slotPredicate).findFirst();
    }

    public Collection<ISlot> findSlots(Predicate<ISlot> slotPredicate) {
        return Arrays.stream(slots).filter(slotPredicate).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return "IInventoryData{" +
                "slots=" + Arrays.toString(Arrays.stream(slots).map(ISlot::toString).toArray()) +
                '}';
    }

    private Inventory copy(String title) {
        Inventory copy = Bukkit.createInventory(menu, inventory.getSize(), ChatColor.translateAlternateColorCodes('&', title));
        for (int i = 0; i < inventory.getSize(); i++) {
            copy.setItem(i, inventory.getContents()[i]);
        }
        return copy;
    }

    public void updateItem(int index, ItemStack itemStack) {
        InventoryUtil.updateItem(menu.getViewer().orElse(null), index, itemStack);
    }
}
