package com.oop.intelimenus.menu.paged;

import com.oop.intelimenus.menu.simple.IInventoryData;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.Inventory;

public class IPagedInventoryData<T> extends IInventoryData {
    @Setter(AccessLevel.PROTECTED)
    @Getter
    private T[] currentData;

    @Setter(AccessLevel.PROTECTED)
    @Getter
    private int pages;

    public IPagedInventoryData(IPagedMenu<T> menu, int size, Inventory inventory) {
        super(menu, size, inventory);
    }

    @Override
    public String toString() {
        return super.toString() + " ~ IPagedInventoryData{" +
                "currentData=size{" + currentData.length + "}" +
                ", pages=" + pages +
                '}';
    }
}
