package com.oop.intelimenus.impl;

import com.oop.intelimenus.interfaces.InventoryData;
import com.oop.intelimenus.interfaces.Menu;
import com.oop.intelimenus.interfaces.MenuButton;
import com.oop.intelimenus.interfaces.MenuSlot;
import com.oop.intelimenus.interfaces.actionable.MenuAction;
import com.oop.intelimenus.interfaces.actionable.Moveable;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.Optional;

import static com.oop.intelimenus.InteliMenus.getInteliMenus;

public class IMenu implements Menu {

    private MenuSlot[] slots;

    @Getter
    private Player viewer;

    @Getter
    @Setter
    private MenuAction currentAction = MenuAction.NONE;

    @Getter
    @Setter
    private InventoryData inventoryData;

    @Getter
    private int rows;

    @Getter
    private String title;

    public IMenu(Player viewer, int rows, String title) {
        this.viewer = viewer;
        this.rows = rows;
        this.title = title;

        slots = new MenuSlot[rows * 9];
    }

    @Override
    public void onClick(InventoryClickEvent event) {

    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }

    @Override
    public void onDrag(InventoryDragEvent event) {

    }

    @Override
    public void onOpen(InventoryOpenEvent event) {

    }

    @Override
    public void setMoving(Moveable where) {

    }

    @Override
    public Optional<Menu> getCurrentMoving() {
        return Optional.empty();
    }


    @Override
    public Menu getParent() {
        return null;
    }

    @Override
    public Inventory getInventory(boolean rebuild) {
        if (inventoryData == null)
            return buildMenu();
        return !rebuild ? inventoryData.getInventory() : buildMenu();
    }

    @Override
    public IMenu setSlot(@NonNull MenuSlot slot) {
        slots[slot.getIndex()] = slot;
        return this;
    }

    @Override
    public IMenu setSlot(int slot, MenuButton button) {
        ISlot iSlot = new ISlot();
        iSlot.setHolder(button);

        slots[slot] = iSlot;
        return this;
    }

    @Override
    public Inventory buildMenu() {
        return null;
    }

    @Override
    public void open(Object object, Runnable callback) {

    }
}
