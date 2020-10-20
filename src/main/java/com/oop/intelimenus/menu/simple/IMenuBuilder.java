package com.oop.intelimenus.menu.simple;

import com.google.common.base.Preconditions;
import com.oop.intelimenus.button.IButton;
import com.oop.intelimenus.designer.MenuDesigner;
import com.oop.intelimenus.menu.paged.IPagedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class IMenuBuilder<T extends IMenu, B extends IMenuBuilder<T, B>> {
    private T menu;
    public IMenuBuilder(T menu) {
        this.menu = menu;
    }

    public T menu() {
        return menu;
    }

    public B apply(Consumer<T> consumer) {
        consumer.accept(menu);
        return (B) this;
    }

    public B rows(int rows) {
        Preconditions.checkArgument(rows < 7, "The maximum size of an inventory is 54");
        menu.setSize(rows * 9);
        return (B) this;
    }

    public B size(int size) {
        Preconditions.checkArgument(size < 55 && size % 9 == 0, "The maximum size of an inventory is 54 or the size is not dividable by 9");
        menu.setSize(size);
        return (B) this;
    }

    public B title(Function<T, String> titleSupplier) {
        menu.setTitleSupplier((Function<IMenu, String>) titleSupplier);
        return (B) this;
    }

    public B title(Supplier<String> titleSupplier) {
        menu.setTitleSupplier(menu -> titleSupplier.get());
        return (B) this;
    }

    public B title(String title) {
        menu.setTitleSupplier(menu -> title);
        return (B) this;
    }

    public MenuDesigner<T, B> designer() {
        return new MenuDesigner<T, B>(menu, (B) this);
    }

    public static PlayerPasser<ISimpleMenuBuilder<IMenu>> simpleMenu() {
        return new PlayerPasser<>(player -> new ISimpleMenuBuilder<>(new IMenu(player, 4, "&cDefault Title")));
    }

    public static <O> PlayerPasser<IPagedMenuBuilder<IPagedMenu<O>, O>> pagedMenu(Class<O> objectClass) {
        return new PlayerPasser<>(player -> new IPagedMenuBuilder<>(new IPagedMenu<>(player, 4, "&cDefault Title")));
    }

    public static <T extends IMenu> PlayerPasser<ISimpleMenuBuilder<T>> customSimpleMenu(Function<Player, T> constructor) {
        return new PlayerPasser<>(player -> new ISimpleMenuBuilder<>(constructor.apply(player)));
    }

    public static <T extends IPagedMenu<O>, O> PlayerPasser<IPagedMenuBuilder<T, O>> customPagedMenu(Class<O> clazz, Function<Player, T> constructor) {
        return new PlayerPasser<>(player -> new IPagedMenuBuilder<>(constructor.apply(player)));
    }

    public static class IPagedMenuBuilder<T extends IPagedMenu<O>, O> extends IMenuBuilder<T, IPagedMenuBuilder<T, O>> {
        public IPagedMenuBuilder(T menu) {
            super(menu);
        }

        public IPagedMenuBuilder<T, O> objectsProvider(Supplier<Collection<O>> objectProvider) {
            menu().setObjectsProvider(objectProvider);
            return this;
        }

        public IPagedMenuBuilder<T, O> pagedButtonBuilder(Function<O, IButton> buttonFunction) {
            menu().setPagedButtonBuilder(buttonFunction);
            return this;
        }
    }

    public static class ISimpleMenuBuilder<T extends IMenu> extends IMenuBuilder<T, ISimpleMenuBuilder<T>> {
        public ISimpleMenuBuilder(T menu) {
            super(menu);
        }
    }

    @AllArgsConstructor
    public static class PlayerPasser<T> {
        private final Function<Player, T> whenPassed;
        public T who(Player player) {
            return whenPassed.apply(player);
        }
    }
}
