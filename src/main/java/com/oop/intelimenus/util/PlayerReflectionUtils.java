package com.oop.intelimenus.util;

import com.oop.intelimenus.InteliMenus;
import com.oop.intelimenus.interfaces.MenuUtil;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;

public class PlayerReflectionUtils {
    @SneakyThrows
    public void test() {
        MenuUtil util = InteliMenus.getInteliMenus().getUtil();

        Class packetClazz = util.getClass("{nms}.PacketPlayOutSetSlot");
        Class itemStackClass = util.getClass("{nms}.ItemStack");
        Constructor packetConstructor = packetClazz.getDeclaredConstructor(int.class, int.class, itemStackClass);

    }
}
