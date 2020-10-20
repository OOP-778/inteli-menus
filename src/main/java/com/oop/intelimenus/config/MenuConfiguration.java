package com.oop.intelimenus.config;

import com.google.common.base.Preconditions;
import com.oop.intelimenus.button.IButton;
import com.oop.intelimenus.config.modifiers.MenuModifier;
import com.oop.intelimenus.config.wrappers.ConfigWrapper;
import com.oop.intelimenus.config.wrappers.SectionWrapper;
import com.oop.intelimenus.interfaces.MenuItemBuilder;
import com.oop.intelimenus.util.InteliPair;
import lombok.Getter;
import org.bukkit.Material;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
public class MenuConfiguration {
    private Set<InteliPair<Predicate<ConfigButton>, Consumer<ConfigButton>>> buttonModifiers = new HashSet<>();
    private List<String> layout;
    private String title;
    private MenuLoader loader;

    public MenuConfiguration(ConfigWrapper menuConfig, MenuLoader loader) {
        this.loader = loader;

        // Load & Validate layout
        layout = new LinkedList<>(menuConfig.getAsCollOf("layout", String.class))
                .stream()
                .map(in -> in.replaceAll("\\s+", ""))
                .collect(Collectors.toList());
        validateLayout();

        title = menuConfig.getAs("title");

        // Initialize modifiers
        for (SectionWrapper section : menuConfig.getSections().values()) {
            MenuModifier handler = loader.getModifier(section.getPath());
            if (handler != null)
                handler.handle(section, this);
        }

        // Initialize Buttons
        SectionWrapper buttons = menuConfig.getSection("buttons");
        for (SectionWrapper buttonSection : buttons.getSections().values())
            loadButton(buttonSection);
    }

    private void throwButtonLoadError(SectionWrapper section, String error) {
        throw new IllegalStateException("Failed to load a button at: " + section.getPath() + " at config: " + section.getConfig().getName() + " cause: " + error);
    }

    private void loadButton(SectionWrapper buttonSection) {
        // Check if button has multiple states
        if (!buttonSection.valueIsPresent("material") && buttonSection.getSections().size() == 0)
            throwButtonLoadError(buttonSection, "Material nor states were not found!");

        IButton button = new IButton();

        // We have no states
        if (buttonSection.valueIsPresent("material")) {
            MenuItemBuilder itemBuilder = loadState(buttonSection);
            button.setCurrentItem(itemBuilder.getItem());
        }
    }

    private MenuItemBuilder loadState(SectionWrapper stateSection) {
        MenuItemBuilder itemBuilder = MenuItemBuilder.of(loader.getItemProvider().apply(stateSection.getAs("material")));

        // Append Lore
        if (stateSection.valueIsPresent("lore"))
            itemBuilder.appendLore(stateSection.getAsCollOf("lore", String.class));


    }

    private void validateLayout() {
        for (String s : layout)
            Preconditions.checkArgument(s.length() == 9, "Invalid Layout Row Bounds (" + s.length() + "/" + 9 + ")");
    }

    public void addModifier(Predicate<ConfigButton> predicate, Consumer<ConfigButton> consumer) {
        buttonModifiers.add(new InteliPair<>(predicate, consumer));
    }
}
