package com.oop.intelimenus;

import com.oop.intelimenus.attribute.AttributeComponent;
import com.oop.intelimenus.attribute.Attributes;
import com.oop.intelimenus.button.builder.IButtonBuilder;
import com.oop.intelimenus.config.loader.BukkitConfigLoader;
import com.oop.intelimenus.menu.simple.IMenuBuilder;
import com.oop.orangeengine.item.custom.OItem;
import com.oop.orangeengine.main.Helper;
import com.oop.orangeengine.main.plugin.EnginePlugin;
import com.oop.orangeengine.main.task.ClassicTaskController;
import com.oop.orangeengine.main.task.TaskController;
import com.oop.orangeengine.material.OMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.IntStream;

public class TestPlugin extends EnginePlugin implements Listener {
    @Override
    public void enable() {
        InteliMenus.register(this, MenuItemBuilder::new);
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public TaskController provideTaskController() {
        return new ClassicTaskController(this);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        InteliMenus.getInteliMenus().initMenuLoader(ml -> ml.addConfigLoader("yml", BukkitConfigLoader.loader), new File("wgag"));

        List<Integer> ints = new ArrayList<>();
        IntStream.range(10, 15).forEach(ints::add);

        IMenuBuilder
                .pagedMenu(int.class)
                .who(event.getPlayer())
                .objectsProvider(() -> ints)
                .pagedButtonBuilder(player -> IButtonBuilder
                        .of(new OItem(OMaterial.CREEPER_HEAD).setDisplayName("&cI: " + player).getItemStack())
                        .clickTrigger(trigger -> {
                            System.out.println("Clicked on " + player);
                            trigger.setCancelled(true);
                        })
                        .toButton())
                .rows(4)
                .title("&cTest Paged Menu - &4{currentPage}&8/&c{pages}")
                .designer()
                .row(1, "@@@@A@@@@",
                        '@', new IButtonBuilder().item(new ItemStack(Material.STAINED_GLASS_PANE)).toButton(),
                        'A', IButtonBuilder
                                .of(new OItem(Material.ANVIL).setDisplayName("Text").getItemStack())
                                .addAnimation(animation -> {
                                    animation.frames(4);
                                    animation.shouldGoBack(true);
                                    animation.interval(20);
                                    animation.repeat(true);
                                    animation.onFrame((frame, anim, button) -> {
                                        int charIndex = frame - 1;
                                        if (!button.getCurrentItem().isPresent()) return;

                                        ItemStack itemStack = button.getCurrentItem().get().clone();
                                        ItemMeta itemMeta = itemStack.getItemMeta();

                                        String displayName = itemMeta.getDisplayName();
                                        String newDisplayName = "";

                                        char[] chars = displayName.toCharArray();
                                        for (int i = 0; i < chars.length; i++) {
                                            if (i != charIndex) {
                                                newDisplayName += "&5" + chars[i];
                                            } else {
                                                newDisplayName += "&b&l" + chars[i] + "&5";
                                            }
                                        }

                                        itemMeta.setDisplayName(Helper.color(newDisplayName));
                                        itemStack.setItemMeta(itemMeta);

                                        Objects.requireNonNull(button.getCurrentMenu(), "menu is null")
                                                .getInventoryData().updateItem(Objects.requireNonNull(button.getParent(), "button parent is null").getIndex(), itemStack);
                                    });
                                }).toButton()
                )
                .row(4, "@@@P@N@@@",
                        '@', new IButtonBuilder().item(new ItemStack(Material.STAINED_GLASS_PANE)).toButton(),
                        'N', IButtonBuilder.of()
                                .addAttribute(Attributes.NEXT_PAGE.get())
                                .addState("page-available", new OItem(Material.ARROW).setDisplayName("Next Page").getItemStack())
                                .addState("page-not-available", Material.STAINED_GLASS_PANE)
                                .toButton(),
                        'P', IButtonBuilder.of()
                                .addAttribute(Attributes.PREVIOUS_PAGE.get())
                                .addState("page-available", new OItem(Material.ARROW).setDisplayName("Previous Page").getItemStack())
                                .addState("page-not-available", Material.STAINED_GLASS_PANE)
                                .toButton()
                )
                .fillEmpty(() -> IButtonBuilder.of(new OItem(Material.BARRIER).setDisplayName("&c???").getItemStack()).addAttribute(Attributes.PLACEHOLDER).toButton())
                .menu()
                .applyComponent(AttributeComponent.class, comp -> comp.addAttribute(Attributes.RETURN_ON_CLOSE))
                .openAction(null);
    }

    @Override
    public void disable() {
        InteliMenus.getInteliMenus().disable();
    }

    class MenuItemBuilder implements com.oop.intelimenus.interfaces.MenuItemBuilder<MenuItemBuilder> {
        private ItemStack itemStack;

        public MenuItemBuilder(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        @Override
        public ItemStack getItem() {
            return itemStack;
        }

        @Override
        public MenuItemBuilder replace(String what, Object to) {
            return this;
        }

        @Override
        public MenuItemBuilder replace(Function<String, String> parser) {
            return this;
        }

        @Override
        public List<String> getLore() {
            return null;
        }

        @Override
        public MenuItemBuilder lore(List<String> newLore) {
            return null;
        }

        @Override
        public MenuItemBuilder appendLore(String line) {
            return null;
        }

        @Override
        public MenuItemBuilder appendLore(Collection<String> lines) {
            return this;
        }

        @Override
        public MenuItemBuilder displayName(String newDisplayName) {
            return null;
        }

        @Override
        public MenuItemBuilder clone() {
            return new MenuItemBuilder(itemStack.clone());
        }
    }
}
