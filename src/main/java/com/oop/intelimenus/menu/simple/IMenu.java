package com.oop.intelimenus.menu.simple;

import com.google.common.base.Preconditions;
import com.oop.intelimenus.InteliMenus;
import com.oop.intelimenus.actionable.MenuAction;
import com.oop.intelimenus.animation.AnimationComponent;
import com.oop.intelimenus.attribute.AttributeComponent;
import com.oop.intelimenus.attribute.Attributes;
import com.oop.intelimenus.button.IButton;
import com.oop.intelimenus.component.Component;
import com.oop.intelimenus.slot.ISlot;
import com.oop.intelimenus.interfaces.Menu;
import com.oop.intelimenus.interfaces.MenuItemBuilder;
import com.oop.intelimenus.trigger.Trigger;
import com.oop.intelimenus.trigger.TriggerComponent;
import com.oop.intelimenus.trigger.types.ButtonClickTrigger;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.oop.intelimenus.InteliMenus.getInteliMenus;

public class IMenu implements Menu<IMenu, ISlot, IButton> {
    @Getter
    private ISlot[] slots;

    private Player viewer;

    @Getter
    @Setter
    private MenuAction currentAction = MenuAction.NONE;

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private IInventoryData inventoryData;

    @Getter
    private Map<Class, Component> componentMap = new HashMap<>();

    @Getter
    private int rows;

    @Getter
    @Setter
    private Function<IMenu, String> titleSupplier;

    private Menu parent;
    private Menu moving;

    private ScheduledFuture<?> animationTask;

    public IMenu(Player viewer, int rows, String title) {
        this.viewer = viewer;
        this.rows = rows;
        this.titleSupplier = menu -> title;

        setSize(rows * 9);
    }

    @Override
    public Optional<Player> getViewer() {
        return Optional.ofNullable(viewer);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        ISlot slot = getInventoryData().getSlots()[event.getSlot()];

        // Check if slot is locked
        boolean locked = slot.getComponent(AttributeComponent.class).map(c -> c.hasAttribute(Attributes.LOCKED)).orElse(false);
        if (locked) {
            event.setCancelled(true);
            return;
        }

        if (slot.getComponent(AttributeComponent.class).map(c -> c.hasAttribute(Attributes.CAN_BE_PICKED_UP)).orElse(false)) {
            handlePickup(event, slot);
            return;
        }

        IButton holder = slot.getHolder().orElse(null);
        if (holder != null) {
            // Check if button is locked
            if (holder.getComponent(AttributeComponent.class).map(c -> c.hasAttribute(Attributes.LOCKED)).orElse(false)) {
                event.setCancelled(true);
                return;
            }

            // Check if slot holder can be picked up
            if (
                    holder.getComponent(AttributeComponent.class).map(c -> c.hasAttribute(Attributes.CAN_BE_PICKED_UP)).orElse(false)
            ) {
                handlePickup(event, slot);
                return;
            }

            // Check for triggers
            holder.getComponent(TriggerComponent.class).ifPresent(triggerComponent -> {
                ButtonClickTrigger trigger = new ButtonClickTrigger(this, holder, slot.getIndex(), event.getClick(), event.getAction(), (Player) event.getWhoClicked());
                List<Trigger<ButtonClickTrigger>> triggers = triggerComponent.triggers(trigger);
                if (triggers.isEmpty()) {
                    event.setCancelled(true);
                    return;
                }

                triggers.forEach(t -> t.getFinalExecutor().accept(trigger));

                if (trigger.isCancelled())
                    event.setCancelled(true);
            })
                    .elseIf(() -> event.setCancelled(true));
        }
    }

    public void handlePickup(InventoryClickEvent event, ISlot slot) {
        // TODO: Implement pickuping of items
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        if (animationTask != null && !animationTask.isCancelled())
            animationTask.cancel(true);
    }

    @Override
    public void onDrag(InventoryDragEvent event) {

    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        if (animationTask != null && !animationTask.isCancelled())
            animationTask.cancel(true);

        animationTask = InteliMenus.getInteliMenus().getScheduler().scheduleAtFixedRate(() -> {
            getComponent(AnimationComponent.class).ifPresent(AnimationComponent::execute);
            for (ISlot slot : getInventoryData().findSlots(slot -> slot.getHolder().isPresent() && slot.getHolder().get().getComponent(AnimationComponent.class).isPresent())) {
                slot.getHolder().get().getComponent(AnimationComponent.class).get().execute();
            }
        }, 2, 2, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onBottomClick(InventoryClickEvent event) {

    }

    @Override
    public void setMoving(Menu where) {
        this.moving = where;
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
        ISlot iSlot = slots[slot];
        iSlot.setHolder(button);
        return this;
    }

    @Override
    public IMenu clone() {
        return null;
    }

    @Override
    public Inventory buildMenu() {
        Inventory inventory = Bukkit.createInventory(this, rows * 9, ChatColor.translateAlternateColorCodes('&', titleSupplier.apply(this)));
        inventoryData = new IInventoryData(this, rows * 9, inventory);

        for (ISlot slot : slots) {
            if (slot.getIndex() >= inventory.getSize()) continue;
            requestItem(slot.getIndex()).ifPresent(menuItemBuilder -> {
                preSetItem(menuItemBuilder);

                inventory.setItem(slot.getIndex(), menuItemBuilder.getItem());
                ISlot slotClone = slot.clone();

                slotClone.getHolder().ifPresent(holder -> {
                    holder.setCurrentItem(menuItemBuilder.getItem());
                    holder.setCurrentMenu(this);
                    holder.setParent(slotClone);
                });

                inventoryData.getSlots()[slot.getIndex()] = slotClone;
            });
        }

        return inventory;
    }

    @Override
    public void open(Menu object, Runnable callback) {
        Preconditions.checkArgument(getViewer().isPresent(), "Failed to open a menu, cause there's no assigned player!");

        if (Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTaskAsynchronously(getInteliMenus().getOwningPlugin(), () -> open(object, callback));
            return;
        }

        Inventory inventory = object.getInventory(getComponent(AttributeComponent.class).map(comp -> comp.hasAttribute(Attributes.REBUILD_ON_OPEN)).isPresent());
        Player player = viewer.getPlayer();
        if (player == null)
            return;

        Bukkit.getScheduler().runTask(getInteliMenus().getOwningPlugin(), () -> {
            player.openInventory(inventory);

            if (callback != null)
                callback.run();
        });
    }

    protected void setSize(int size) {
        rows = size / 9;
        slots = new ISlot[size];
        for (int i = 0; i < slots.length; i++)
            slots[i] = new ISlot(i);
    }

    protected void preSetItem(MenuItemBuilder builder) {}

    @Override
    public String toString() {
        return "IMenu{" +
                "slots=" + Arrays.toString(slots) +
                ", viewer=" + viewer +
                ", currentAction=" + currentAction +
                ", inventoryData=" + inventoryData +
                ", componentMap=" + Arrays.toString(componentMap.values().stream().map(Objects::toString).toArray()) +
                ", rows=" + rows +
                ", parent=" + parent +
                ", moving=" + moving +
                '}';
    }

    public boolean isCurrentlyOpen() {
        return
                inventoryData != null
                && getViewer().get().getOpenInventory().getTopInventory().equals(inventoryData.getInventory());
    }
}
