package com.oop.intelimenus.animation;

import lombok.Setter;
public class AnimationClock {
    public AnimationClock(long interval) {
        this.interval = interval;
    }

    @Setter
    private long interval;

    private long current;

    public boolean tick() {
        current++;
        if (current == interval) {
            current = 0;
            return true;
        }
        return false;
    }

    public void reset() {
        this.current = 0;
    }
}
