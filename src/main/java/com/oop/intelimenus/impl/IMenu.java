package com.oop.intelimenus.impl;

import com.oop.intelimenus.interfaces.InventoryData;
import com.oop.intelimenus.interfaces.Menu;
import com.oop.intelimenus.interfaces.MenuItemBuilder;
import com.oop.intelimenus.interfaces.MenuSlot;
import com.oop.intelimenus.interfaces.actionable.MenuAction;
import com.oop.intelimenus.interfaces.attribute.Attribute;
import io.netty.util.internal.ConcurrentSet;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public class IMenu implements Menu<IMenu, ISlot, IButton> {
    @Getter
    private ISlot[] slots;

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


    private Set<Attribute> attributeSet = new ConcurrentSet<>();

    private Menu parent;
    private Menu moving;

    public IMenu(Player viewer, int rows, String title) {
        this.viewer = viewer;
        this.rows = rows;
        this.title = title;

        slots = new ISlot[rows * 9];
        for (int i = 0; i < slots.length; i++)
            slots[i] = ISlot.builder().index(i).build();
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
    public void setMoving(Menu where) {
        this.moving = moving;
    }

    @Override
    public Optional<Menu> getCurrentMoving() {
        return Optional.ofNullable(moving);
    }

    @Override
    public Optional<Menu> getParent() {
        return Optional.ofNullable(parent);
    }

    @Override
    public void setParent(Menu parent) {
        this.parent = parent;
    }

    @Override
    public Inventory getInventory(boolean rebuild) {
        if (inventoryData == null)
            return buildMenu();
        return !rebuild ? inventoryData.getInventory() : buildMenu();
    }

    @Override
    public IMenu setSlot(@NonNull ISlot slot) {
        slots[slot.getIndex()] = slot;
        return this;
    }

    @Override
    public IMenu setSlot(int slot, IButton button) {
        ISlot iSlot = new ISlot();
        iSlot.setHolder(button);

        slots[slot] = iSlot;
        return this;
    }

    @Override
    public Inventory buildMenu() {
        Inventory inventory = Bukkit.createInventory(this, rows * 9, ChatColor.translateAlternateColorCodes('&', title));
        inventoryData = new IInventoryData();
        inventoryData
        for (ISlot slot : slots) {
            if (slot.getHolder().isPresent()) {
                ItemStack requestedItem = slot.getHolder().get().getDefaultState().orElse(MenuItemBuilder.of(new ItemStack(Material.ANVIL)).displayName("&cCouldn't find state.")).getItem();
                inventory.setItem(slot.getIndex(), requestedItem);
            }
        }
    }

    @Override
    public void open(Menu object, Runnable callback) {

    }

    @Override
    public Collection<Attribute> getAttributes() {
        return attributeSet;
    }
}
