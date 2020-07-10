package com.oop.intelimenus;

import com.oop.intelimenus.impl.IButton;
import com.oop.intelimenus.impl.IButtons;
import com.oop.intelimenus.impl.IMenu;
import com.oop.intelimenus.impl.ISlot;
import com.oop.intelimenus.interfaces.MenuItemBuilder;
import com.oop.intelimenus.interfaces.MenuUtil;
import com.oop.intelimenus.interfaces.attribute.Attributes;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Function;

public class InteliMenus {
    private static InteliMenus instance;

    static {
        new InteliMenus();
    }

    @Getter
    private JavaPlugin owningPlugin;
    @Getter
    private Function<ItemStack, MenuItemBuilder> menuItemBuilderFunction;
    @Getter
    private MenuUtil util;

    private InteliMenus() {
        instance = this;

        IMenu menu = new IMenu(null, 4, "&cGay shit")
                .setSlot(
                        ISlot
                                .builder()
                                .addAttribute(Attributes.ACCEPTS_ITEM)
                                .build()
                )
                .setSlot(
                        ISlot
                                .builder()
                                .holder(
                                        IButtons.RETURN_BUTTON.get()
                                )
                                .build()
                );
    }

    public static InteliMenus getInteliMenus() {
        return instance;
    }

    public void registerMenuItemBuilder(Function<ItemStack, MenuItemBuilder> func) {
        this.menuItemBuilderFunction = func;
    }
}
