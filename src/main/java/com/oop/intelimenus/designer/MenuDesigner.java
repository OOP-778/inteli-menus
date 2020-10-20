package com.oop.intelimenus.designer;

import com.google.common.base.Preconditions;
import com.oop.intelimenus.button.IButton;
import com.oop.intelimenus.button.builder.IButtonBuilder;
import com.oop.intelimenus.menu.simple.IMenu;
import com.oop.intelimenus.slot.ISlot;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.IntStream;

@Accessors(fluent = true)
public class MenuDesigner<T extends IMenu, P> {

    @Getter
    private T menu;

    @Getter
    private P parent;

    public MenuDesigner(T menu, P parent) {
        this.menu = menu;
        this.parent = parent;
    }

    public MenuDesigner(T menu) {
        this.menu = menu;
    }

    public MenuDesigner<T, P> button(int slot) {
        return this;
    }

    public MenuDesigner<T, P> button(int row, int column) {
        return this;
    }

    public MenuDesigner<T, P> rows(@NonNull String design, @NonNull Map<Character, IButton> charDesign, int ...rows) {
        for (int row : rows)
            row(row, design, charDesign);

        return this;
    }

    public MenuDesigner<T, P> rows(int from, int to, @NonNull String design, @NonNull Map<Character, IButton> charDesign) {
        IntStream.range(from, to).forEach(row -> row(row, design, charDesign));
        return this;
    }

    public MenuDesigner<T, P> rows(int[] rows, @NonNull String design, @NonNull Object ...charDesign) {
        Map<Character, IButton> characterIButtonMap = mapOfArray(charDesign);
        for (int row : rows)
            row(row, design, characterIButtonMap);
        return this;
    }

    public MenuDesigner<T, P> fillEmpty(Supplier<IButton> buttonSupplier) {
        for (ISlot slot : menu.getSlots()) {
            if (slot.getHolder().isPresent()) continue;

            slot.setHolder(buttonSupplier.get());
        }
        return this;
    }

    public MenuDesigner<T, P> fillEmpty(ItemStack itemStack) {
        return fillEmpty(() -> IButtonBuilder.of(itemStack).toButton());
    }

    public MenuDesigner<T, P> row(int row, @NonNull String design, @NonNull Object ...charDesign) {
        return row(row, design, mapOfArray(charDesign));
    }

    public MenuDesigner<T, P> row(int row, @NonNull String design, @NonNull Map<Character, IButton> charDesign) {
        design = design.replaceAll("\\s+", "");
        Preconditions.checkArgument(design.length() == 9, "Incorrect design passed. The length should be 9! (" + design.length() + "/9) = " + design);

        int slot = row == 1 ? 0 : (row * 9) - 9;
        for (char c : design.toCharArray()) {
            IButton button = charDesign.get(c);
            if (button != null)
                menu.setSlot(slot, button);
            slot++;
        }

        return this;
    }

    private static Map<Character, IButton> mapOfArray(Object... objects) {
        if (objects.length % 2 != 0)
            throw new IllegalStateException("Failed to convert objects to map, because the size is not even!");

        Map<Character, IButton> map = new HashMap<>();

        int len = objects.length;
        int i = 0;

        boolean inside = true;
        while (inside) {
            Object key = Objects.requireNonNull(objects[i++], "Key cannot be null!");
            Object value = Objects.requireNonNull(objects[i++], "Value cannot be null!");
            if (key instanceof String)
                key = ((String) key).toCharArray()[0];

            Preconditions.checkArgument(key instanceof Character, "Key is not a character! (" + key + ") at" + i);
            Preconditions.checkArgument(value instanceof IButton, "Value is not a button! (" + value + ") at " + i);

            map.put(
                    (Character) key,
                    (IButton) value
            );
            if (i == len)
                inside = false;
        }

        return map;
    }

}
