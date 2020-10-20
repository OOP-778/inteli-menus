package com.oop.intelimenus.config.wrappers;

import com.google.gson.internal.Primitives;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class ValuableWrapper {
    @Getter(AccessLevel.PROTECTED)
    private Map<String, Object> values = new HashMap<>();

    @Getter
    private Map<String, SectionWrapper> sections = new HashMap<>();

    /**
     * Creates a valuable wrapper
     *
     * @param allValues should be all values of the wrapper hierarchy
     */
    public ValuableWrapper(Map<String, Object> allValues) {
        for (Map.Entry<String, Object> entry : new HashSet<>(allValues.entrySet())) {
            // Means it's under a new section
            if (entry.getKey().contains(".")) {
                String[] split = entry.getKey().split("\\.", 2);
                _parseSection(split, entry);

            } else
                values.put(entry.getKey(), entry.getValue());
        }
    }

    private void _parseSection(String[] value, Map.Entry<String, Object> entry) {
        // If passed from parent
        if (value.length == 1) {
            values.put(value[0], entry.getValue());
            return;
        }

        ConfigWrapper config = this instanceof ConfigWrapper ? (ConfigWrapper) this : ((SectionWrapper) this).getConfig();
        SectionWrapper parent = this instanceof ConfigWrapper ? null : ((SectionWrapper) this);

        String sectionName = value[0];
        SectionWrapper section = sections.computeIfAbsent(sectionName, key -> new SectionWrapper(key, config, parent, new LinkedHashMap<>()));
        ((ValuableWrapper) section)._parseSection(value[1].contains(".") ? value[1].split("\\.", 2) : new String[]{value[1]}, entry);
    }

    /**
     * Throws an error at the current path of the wrapper
     *
     * @param message the error message
     */
    protected abstract void throwError(String message) throws IllegalStateException;

    public <T> Collection<T> getAsCollOf(String path, Class<T> type) {
        return getAs(path, Collection.class);
    }

    public <T> T getAs(String path, Supplier<T> supplier) {
        Optional<T> ocv = get(path);
        if (supplier == null && !ocv.isPresent())
            throwError("Value is not present: `" + path + "`");

        return ocv.isPresent() ? ocv.get() : supplier == null ? null : supplier.get();
    }

    public <T> T getAs(String path) {
        return (T) getAs(path, (Supplier)null);
    }

    public <T> T getAs(String path, Class<T> type) {
        Object object = getAs(path, (Supplier<T>) null);
        if (type == null)
            return (T) object;

        type = Primitives.wrap(type);
        if (type.isAssignableFrom(Primitives.unwrap(object.getClass())))
            return (T) object;

        if (type != Primitives.unwrap(object.getClass()))
            return (T) doConversion(object, type);

        return (T) object;
    }

    public <T> Object doConversion(Object parsed, Class<T> clazz) {
        String value = parsed.toString();
        if (clazz == String.class) return value;

        if ((parsed.getClass() == Double.class || parsed.getClass() == Float.class && clazz == Integer.class)) {
            if (value.contains(".")) {
                String[] splitValue = value.split("\\.");
                int original = Integer.parseInt(splitValue[0]);
                int secondPart = splitValue[1].toCharArray()[0];

                if (secondPart > 4)
                    original += 1;

                value = Integer.toString(original);
            }
        }

        if (clazz == Integer.class)
            return Integer.valueOf(value);

        else if (clazz == Long.class)
            return Long.valueOf(value);

        else if (clazz == Float.class)
            return Float.valueOf(value);

        else if (clazz == Double.class)
            return Double.valueOf(value);

        else
            throw new IllegalStateException("Incorrect object type required: " + clazz.getSimpleName() + " found: " + parsed.getClass().getSimpleName());
    }

    public <T> void valuePresent(String path, Consumer<T> ifPresent) {
        valuePresent(path, null, ifPresent);
    }

    public boolean valueIsPresent(String path) {
        return get(path).isPresent();
    }

    public <T> void valuePresent(String path, Class<T> type, Consumer<T> ifPresent) {
        get(path, type).ifPresent(ifPresent);
    }

    public <T> Optional<T> get(String path, Class<T> type) {
        return Optional.ofNullable(getAs(path, type));
    }

    public <T> Optional<T> get(String path) {
        return Optional.ofNullable((T) values.get(path));
    }

    public SectionWrapper getSection(String path) {
        return sections.get(path);
    }
}
