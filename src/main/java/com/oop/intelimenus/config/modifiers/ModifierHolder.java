package com.oop.intelimenus.config.modifiers;

import com.oop.intelimenus.config.modifiers.def.ActionModifier;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class ModifierHolder {
    @Getter
    private Map<Class, MenuModifier> modifierMap = new HashMap<>();

    public ModifierHolder() {
        registerModifier(new ActionModifier());
    }

    public void registerModifier(MenuModifier modifier) {
        modifierMap.put(modifier.getClass(), modifier);
    }

    public <T extends MenuModifier> T getModifier(Class<T> clazz) {
        return (T) modifierMap.get(clazz);
    }

    public <T extends MenuModifier> T getModifier(String name) {
        return (T) modifierMap.values().stream()
                .filter(am -> am.getIdentifier().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
