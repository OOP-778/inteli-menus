package com.oop.intelimenus.interfaces.attribute;


import com.oop.intelimenus.util.LimitedMutableMap;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Attributes {
    public static final Attribute ACCEPTS_ITEM = new Attribute() {
        @Override
        public String getId() {
            return "ACCEPTS_ITEM";
        }

        @Override
        public boolean applyableToMenus() {
            return false;
        }

        @Override
        public boolean applyableToSlots() {
            return true;
        }

        @Override
        public boolean applyableToButtons() {
            return false;
        }
    };

    public static final Attribute FILLER = new Attribute() {
        @Override
        public String getId() {
            return "filler";
        }

        @Override
        public boolean applyableToMenus() {
            return false;
        }

        @Override
        public boolean applyableToSlots() {
            return false;
        }

        @Override
        public boolean applyableToButtons() {
            return true;
        }
    };

    public static final Attribute REBUILD_ON_OPEN = new Attribute() {
        @Override
        public String getId() {
            return "rebuildOnOpen";
        }

        @Override
        public boolean applyableToMenus() {
            return true;
        }

        @Override
        public boolean applyableToSlots() {
            return false;
        }

        @Override
        public boolean applyableToButtons() {
            return false;
        }
    };

    private static final Map<String, Attribute> byName =
            new LimitedMutableMap<>(new ConcurrentHashMap<>(), false, true);

    static {
        Arrays.asList(
                ACCEPTS_ITEM,
                FILLER,
                REBUILD_ON_OPEN
        ).forEach(Attributes::register);
    }

    public static void register(Attribute attribute) {
        byName.put(attribute.getId().toLowerCase(), attribute);
    }

    public static Optional<Attribute> getByName(String name) {
        return Optional.ofNullable(byName.get(name.toLowerCase()));
    }
}
