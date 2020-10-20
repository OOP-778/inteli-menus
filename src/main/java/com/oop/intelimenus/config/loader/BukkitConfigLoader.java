package com.oop.intelimenus.config.loader;

import com.oop.intelimenus.config.wrappers.ConfigWrapper;
import org.bukkit.configuration.file.YamlConfiguration;

public class BukkitConfigLoader {
    /**
     * Bukkit Configuration Converter between bukkit and our wrapper
     */
    public static final ConfigLoader loader = file -> {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        return new ConfigWrapper(file, config.getValues(true));
    };
}
