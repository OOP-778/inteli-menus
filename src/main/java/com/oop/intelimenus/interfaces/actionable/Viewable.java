package com.oop.intelimenus.interfaces.actionable;

import org.bukkit.entity.Player;

public interface Viewable<T extends Viewable> {
    Player getViewer();
}
