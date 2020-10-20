package com.oop.intelimenus.button.builder;

import com.oop.intelimenus.animation.Animation;
import com.oop.intelimenus.animation.AnimationComponent;
import com.oop.intelimenus.attribute.Attribute;
import com.oop.intelimenus.attribute.AttributeComponent;
import com.oop.intelimenus.button.IButton;
import com.oop.intelimenus.button.state.StateComponent;
import com.oop.intelimenus.interfaces.MenuItemBuilder;
import com.oop.intelimenus.menu.simple.IMenu;
import com.oop.intelimenus.trigger.TriggerComponent;
import com.oop.intelimenus.trigger.types.ButtonClickTrigger;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class IButtonBuilder {
    private IButton button = new IButton();

    public IButtonBuilder item(ItemStack itemStack) {
        button.setCurrentItem(itemStack);
        return this;
    }

    public IButtonBuilder addAttribute(Attribute attribute) {
        button.applyComponent(AttributeComponent.class, c -> c.addAttribute(attribute));
        return this;
    }

    public IButtonBuilder clickTrigger(Consumer<ButtonClickTrigger> onClick) {
        button.applyComponent(TriggerComponent.class, c -> c.addTrigger(ButtonClickTrigger.class, trigger -> trigger.onTrigger(onClick)));
        return this;
    }

    public IButtonBuilder addState(String stateId, MenuItemBuilder builder) {
        button.applyComponent(StateComponent.class, stateComponent -> {
            stateComponent.addState(stateId, builder);
        });
        return this;
    }

    public IButtonBuilder addState(String stateId, ItemStack itemStack) {
        return addState(stateId, MenuItemBuilder.of(itemStack));
    }

    public IButtonBuilder addState(String stateId, Material material) {
        return addState(stateId, new ItemStack(material));
    }

    public IButtonBuilder addAnimation(Consumer<Animation<IButton>> animation) {
        Animation<IButton> animation1 = new Animation<>();
        animation.accept(animation1);
        return addAnimation(animation1);
    }

    public IButtonBuilder addAnimation(Animation<IButton> animation) {
        button.applyComponent(AnimationComponent.class, comp -> comp.add(animation));
        return this;
    }

    public IButton toButton() {
        return button;
    }

    public static IButtonBuilder of(ItemStack itemStack) {
        return new IButtonBuilder().item(itemStack);
    }

    public static IButtonBuilder of() {
        return new IButtonBuilder();
    }

    public IButtonBuilder of(IButton button) {
        IButtonBuilder buttonBuilder = new IButtonBuilder();
        buttonBuilder.button = button;
        return buttonBuilder;
    }

    public void clickBuilder(Supplier<IMenu> menu) {

    }
}
