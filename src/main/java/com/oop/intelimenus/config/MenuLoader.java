package com.oop.intelimenus.config;

import com.oop.intelimenus.config.loader.BukkitConfigLoader;
import com.oop.intelimenus.config.loader.ConfigLoader;
import com.oop.intelimenus.config.loader.JsonConfigLoader;
import com.oop.intelimenus.config.modifiers.ModifierHolder;
import com.oop.intelimenus.config.modifiers.def.ActionModifier;
import com.oop.intelimenus.config.wrappers.ConfigWrapper;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Getter
public class MenuLoader extends ModifierHolder {
    private Map<String, MenuConfiguration> loadedMenus = new HashMap<>();
    private Map<String, ConfigLoader> configLoaders = new HashMap<>();

    private Function<String, ItemStack> itemProvider;

    public MenuLoader() {
        addConfigLoader("yml", BukkitConfigLoader.loader);
        addConfigLoader("json", JsonConfigLoader.loader);

        registerModifier(new ActionModifier());
    }

    public void addConfigLoader(String extension, ConfigLoader loader) {
        configLoaders.put(extension.replace("\\.", ""), loader);
    }

    public void removeConfigLoader(String extension) {
        configLoaders.remove(extension.replace("\\.", ""));
    }

    public void load(File... menusDirectories) {
        try {
            for (File menusDirectory : menusDirectories) {
                if (menusDirectory == null || !menusDirectory.exists()) continue;

                File[] menuFiles = menusDirectory.listFiles();
                for (File menuFile : menuFiles) {
                    try {
                        String extension = menuFile.getName().split("\\.")[1];
                        ConfigLoader ff = configLoaders.get(extension);
                        Objects.requireNonNull(ff, "Config loader for extension '" + extension + "' not found!");

                        ConfigWrapper menuConfig = ff.load(menuFile);
                        loadedMenus.put(menuConfig.getFile().getName().split("\\.")[0], new MenuConfiguration(menuConfig, this));

                    } catch (Throwable menuError) {
                        throw new IllegalStateException("Failed to load menu by name " + menuFile.getName().split("\\.")[0] + " at path: " + menuFile.getPath());
                    }
                }
            }
        } catch (Throwable loadThrw) {
            throwError("Failed to load menus cause", loadThrw);
        }
    }

    protected void throwError(String message, Throwable inerhit) throws IllegalStateException {
        throw new IllegalStateException(message, inerhit);
    }
}
