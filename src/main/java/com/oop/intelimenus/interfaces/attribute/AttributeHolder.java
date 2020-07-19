package com.oop.intelimenus.interfaces.attribute;

import lombok.NonNull;
import java.util.Collection;

public interface AttributeHolder<T> extends Cloneable {
    Collection<Attribute> getAttributes();

    default T addAttribute(@NonNull Attribute attribute) {
        if (!attribute.accepts(this))
            throw new IllegalStateException("Failed to add attribute " + attribute.getClass().getSimpleName() + " because attribute doesn't support " + getClass().getSimpleName());
        removeAttribute(attribute);
        getAttributes().add(attribute);
        return (T) this;
    }

    default T removeAttribute(@NonNull Attribute attribute) {
        getAttributes().remove(attribute);
        return (T) this;
    }

    default boolean hasAttribute(@NonNull Attribute attribute) {
        return getAttributes().contains(attribute);
    }
}
