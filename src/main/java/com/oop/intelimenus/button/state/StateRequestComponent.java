package com.oop.intelimenus.button.state;

import com.oop.intelimenus.attribute.AttributeComponent;
import com.oop.intelimenus.attribute.Attributes;
import com.oop.intelimenus.component.Component;
import com.oop.intelimenus.interfaces.MenuItemBuilder;
import com.oop.intelimenus.slot.ISlot;
import com.oop.intelimenus.util.InteliPair;
import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class StateRequestComponent implements Component<StateRequestComponent> {

    @Getter
    private Set<InteliPair<Predicate<ISlot>, Function<ISlot, MenuItemBuilder>>> requests = new HashSet<>();

    public void register(Predicate<ISlot> filter, Function<ISlot, MenuItemBuilder> function) {
        requests.add(new InteliPair<>(filter, function));
    }

    public void register(int slot, Function<ISlot, MenuItemBuilder> function) {
        register(s -> s.getIndex() == slot, function);
    }

    @Override
    public StateRequestComponent clone() {
        StateRequestComponent component = new StateRequestComponent();
        component.requests.addAll(requests);
        return component;
    }

    public Function<ISlot, MenuItemBuilder> find(ISlot slot) {
        for (InteliPair<Predicate<ISlot>, Function<ISlot, MenuItemBuilder>> request : requests) {
            if (!request.getKey().test(slot)) continue;
            return request.getValue();
        }
        return null;
    }

    @Override
    public String toString() {
        return "StateRequestComponent{" +
                "requests=size{" + requests.size() + "}" +
                '}';
    }
}
