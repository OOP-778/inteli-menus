package com.oop.intelimenus.config.modifiers.def;

import com.google.common.base.Preconditions;
import com.oop.intelimenus.button.builder.TriggerBuilder;
import com.oop.intelimenus.config.ConfigButton;
import com.oop.intelimenus.config.MenuConfiguration;
import com.oop.intelimenus.config.modifiers.MenuModifier;
import com.oop.intelimenus.config.wrappers.SectionWrapper;
import com.oop.intelimenus.trigger.types.ButtonClickTrigger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ActionModifier extends MenuModifier {
    public ActionModifier() {
        registerAction("command", (button, section) -> {
            // Check if all values are there
            Preconditions.checkArgument(section.valueIsPresent("command"), "Command is not found");

            Function<ButtonClickTrigger, CommandSender> senderFunction = trigger -> {
                Optional<String> sender = section.get("sender", String.class);
                if (!sender.isPresent()) return trigger.getPlayer();

                return sender.get().equalsIgnoreCase("console") ? Bukkit.getConsoleSender() : trigger.getPlayer();
            };

            String command = section.getAs("command");

            TriggerBuilder
                    .of(ButtonClickTrigger.class)
                    .onTrigger(trigger -> {
                        String replace = command.replace("%player%", trigger.getPlayer().getName());
                        Bukkit.dispatchCommand(senderFunction.apply(trigger), replace);
                    });
        });
    }

    @Override
    public String getIdentifier() {
        return "actions";
    }

    private Map<String, BiConsumer<ConfigButton, SectionWrapper>> actionTypes = new HashMap<>();

    @Override
    public void handle(SectionWrapper section, MenuConfiguration configuration) {
        // Identifiers
        for (SectionWrapper actionSection : section.getSections().values()) {
            Predicate<ConfigButton> predicate;
            String key = actionSection.getPath();

            // We got a letter
            if (key.toCharArray().length == 1)
                predicate = button -> button.getLetter().equalsIgnoreCase(key);
            else
                predicate = button -> button.getIdentifier() != null && button.getIdentifier().equalsIgnoreCase(key);

            // If this contains single action
            if (actionSection.getSections().size() == 0) {
                load(predicate, actionSection, configuration);

            } else {
                for (SectionWrapper value : actionSection.getSections().values())
                    load(predicate, value, configuration);
            }
        }
    }

    private void load(Predicate<ConfigButton> predicate, SectionWrapper actionSection, MenuConfiguration configuration) {
        Preconditions.checkArgument(actionSection.valueIsPresent("type"), "The action section doesn't have a type!");
        String type = actionSection.getAs("type");

        BiConsumer<ConfigButton, SectionWrapper> actionHandler = actionTypes.get(type.toLowerCase());
        Preconditions.checkArgument(actionHandler != null, "Failed to find action by type: " + type.toLowerCase());

        configuration.addModifier(predicate, configButton -> {
            actionHandler.accept(configButton, actionSection);
        });
    }

    public void registerAction(String type, BiConsumer<ConfigButton, SectionWrapper> consumer) {
        actionTypes.put(type.toLowerCase(), consumer);
    }
}
