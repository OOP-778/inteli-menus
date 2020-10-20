package com.oop.intelimenus.button.builder;

import com.google.common.base.Preconditions;
import com.oop.intelimenus.button.IButton;
import com.oop.intelimenus.trigger.TriggerComponent;
import com.oop.intelimenus.trigger.types.MenuTrigger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class TriggerBuilder<T extends MenuTrigger> {
    private List<Predicate<T>> conditions = new ArrayList<>();
    private Consumer<T> consumer;
    private Class<T> clazz;

    public TriggerBuilder(Class<T> clazz) {
        this.clazz = clazz;
    }

    public static <T extends MenuTrigger> TriggerBuilder<T> of(Class<T> clazz) {
        return new TriggerBuilder<>(clazz);
    }

    public TriggerBuilder<T> condition(Predicate<T> condition) {
        conditions.add(condition);
        return this;
    }

    public TriggerBuilder<T> onTrigger(Consumer<T> consumer) {
        this.consumer = consumer;
        return this;
    }

    public void apply(IButton button) {
        Preconditions.checkArgument(consumer != null, "On trigger was not set");
        button.applyComponent(
                TriggerComponent.class,
                triggerComponent -> triggerComponent
                        .addTrigger(clazz, trigger -> trigger.onTrigger(t -> {
                            if (!conditions.stream().allMatch(p -> p.test(t))) return;

                            consumer.accept(t);
                        }))
        );
    }
}
