package com.oop.intelimenus.attribute;


import com.oop.intelimenus.button.ButtonModifier;
import com.oop.intelimenus.button.IButton;
import com.oop.intelimenus.menu.paged.IPagedMenu;
import com.oop.intelimenus.trigger.TriggerComponent;
import com.oop.intelimenus.trigger.types.ButtonClickTrigger;
import com.oop.intelimenus.util.LimitedMutableMap;

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

    public static final Attribute RETURN_ON_CLOSE = new Attribute() {
        @Override
        public String getId() {
            return "returnonclose";
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

    /**
     * Sets the state of the object to be pickable.
     * if {@link Attributes.LOCKED} is applied, this attribute will have no effect
     */
    public static final Attribute CAN_BE_PICKED_UP = new Attribute() {
        @Override
        public String getId() {
            return "canbepickedup";
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
            return true;
        }
    };

    /*
    Sets the state of the object to locked.
    Which means that the events won't fire on them and will be cancelled
    */
    public static final Attribute LOCKED = new Attribute() {
        @Override
        public String getId() {
            return "locked";
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
            return true;
        }
    };

    /*
    Sets the state of button to be a placeholder
    It's mostly used in paged menu building
    When you want to have a placeholders for slots that haven't been occupied
    */
    public static final Attribute PLACEHOLDER = new Attribute() {
        @Override
        public String getId() {
            return "placeholder";
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

    /*
    Applyable to buttons on paged menus
    Will be used to move to next page
    */
    public static class NEXT_PAGE implements Attribute, ButtonModifier {
        @Override
        public String getId() {
            return "nextPage";
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

        @Override
        public void onAdd(IButton object) {
            object
                    .applyComponent(TriggerComponent.class, comp -> {
                        System.out.println("Adding next page");

                        System.out.println("== Below STE ==");
                        for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace())
                            System.out.println(" - " + stackTraceElement.toString());

                        comp.addTrigger(ButtonClickTrigger.class, trigger -> {
                            trigger.onTrigger(event -> {

                                System.out.println(event.getButton().toString());

                                System.out.println("on trigger");
                                event.setCancelled(true);
                                if (!(event.getMenu() instanceof IPagedMenu)) return;

                                int currentPage = ((IPagedMenu<?>) event.getMenu()).getCurrentPage();
                                int pages = ((IPagedMenu<?>) event.getMenu()).getInventoryData().getPages();

                                if (currentPage == pages) return;
                                ((IPagedMenu<?>) event.getMenu()).nextPage();
                            });
                        });

                        System.out.println("Triggers size: " + comp.getTriggers().size());
                    });
        }

        public static NEXT_PAGE get() {
            return (NEXT_PAGE) getByName("nextPage").orElseThrow(() -> new IllegalStateException("NextPage attribute is not registered!"));
        }
    }

    /*
    Applyable to buttons on paged menus
    Will be used to move to previous page
    */
    public static class PREVIOUS_PAGE implements Attribute, ButtonModifier {

        @Override
        public String getId() {
            return "previousPage";
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

        @Override
        public void onAdd(IButton object) {
            object
                    .applyComponent(TriggerComponent.class, comp -> {
                        comp.addTrigger(ButtonClickTrigger.class, trigger -> {
                            trigger.onTrigger(event -> {
                                event.setCancelled(true);
                                if (!(event.getMenu() instanceof IPagedMenu)) return;

                                int currentPage = ((IPagedMenu<?>) event.getMenu()).getCurrentPage();
                                if (currentPage == 1) return;

                                ((IPagedMenu<?>) event.getMenu()).previousPage();
                            });
                        });
                    });
        }

        public static PREVIOUS_PAGE get() {
            return (PREVIOUS_PAGE) getByName("previousPage").orElse(null);
        }
    }

    private static final Map<String, Attribute> byName =
            new LimitedMutableMap<>(new ConcurrentHashMap<String, Attribute>() {{
                put(transform(ACCEPTS_ITEM.getId()), ACCEPTS_ITEM);
                put(transform(FILLER.getId()), FILLER);
                put(transform(REBUILD_ON_OPEN.getId()), REBUILD_ON_OPEN);
                put(transform(CAN_BE_PICKED_UP.getId()), CAN_BE_PICKED_UP);
                put(transform(RETURN_ON_CLOSE.getId()), RETURN_ON_CLOSE);
                put(transform(LOCKED.getId()), LOCKED);
                put(transform(LOCKED.getId()), LOCKED);
                put(transform(new NEXT_PAGE().getId()), new NEXT_PAGE());
                put(transform(new PREVIOUS_PAGE().getId()), new PREVIOUS_PAGE());
            }}, false, true);

    public static void register(Attribute attribute) {
        byName.put(transform(attribute.getId()), attribute);
    }

    public static Optional<Attribute> getByName(String name) {
        Attribute attribute = byName.get(transform(name));
        return Optional.ofNullable(attribute);
    }

    private static String transform(String id) {
        id = id.toLowerCase();
        id = id.replace("_", "");
        id = id.replaceAll("\\s+", "");
        return id;
    }
}
