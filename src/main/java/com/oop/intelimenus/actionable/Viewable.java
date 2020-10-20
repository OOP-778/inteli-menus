package com.oop.intelimenus.actionable;

import org.bukkit.entity.Player;

import java.util.Optional;

public interface Viewable {
    Optional<Player> getViewer();
}
