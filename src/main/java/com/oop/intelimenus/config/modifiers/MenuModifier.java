package com.oop.intelimenus.config.modifiers;

import com.oop.intelimenus.config.MenuConfiguration;
import com.oop.intelimenus.config.wrappers.SectionWrapper;

public abstract class MenuModifier {

    // How does it know about it's sections
    public abstract String getIdentifier();

    // Handle section
    public abstract void handle(SectionWrapper section, MenuConfiguration configuration);
}
