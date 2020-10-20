package com.oop.intelimenus.button;

import com.oop.intelimenus.trigger.TriggerComponent;
import com.oop.intelimenus.trigger.types.ButtonClickTrigger;

import java.util.function.Supplier;

public class IButtons {
    public static final Supplier<IButton> RETURN_BUTTON = () -> new IButton()
            .applyComponent(TriggerComponent.class, component -> component.addTrigger(
                    ButtonClickTrigger.class,
                    trigger -> {
                        trigger.addFilter(t -> t.getMenu().getParent().isPresent());
                        trigger.onTrigger(t -> t.getMenu().moveAction(t.getMenu().getParent().get(), null));
                    }
            ));

    public static final Supplier<IButton> CLOSE_BUTTON = () -> new IButton()
            .applyComponent(TriggerComponent.class, component -> component.addTrigger(
                    ButtonClickTrigger.class,
                    trigger -> trigger.onTrigger(t -> t.getMenu().closeAction(null))
            ));
}
