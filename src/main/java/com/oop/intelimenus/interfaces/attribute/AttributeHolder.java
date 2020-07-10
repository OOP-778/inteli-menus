package com.oop.intelimenus.interfaces.attribute;

import lombok.NonNull;
import java.util.Collection;

public interface AttributeHolder extends Cloneable {
    Collection<Attribute> getAttributes();

    default void addAttribute(@NonNull Attribute attribute) {
        if (!attribute.accepts(this))
            throw new IllegalStateException("Failed to add attribute " + attribute.getClass().getSimpleName() + " because attribute doesn't support " + getClass().getSimpleName());
        removeAttribute(attribute);
        getAttributes().add(attribute);
    }

    default void removeAttribute(@NonNull Attribute attribute) {
        getAttributes().remove(attribute);
    }

    default boolean hasAttribute(@NonNull Attribute attribute) {
        return getAttributes().contains(attribute);
    }
}
