package com.amgems.slimeandroid.engine;

import android.graphics.Point;

/**
 * Created by shermpay on 12/12/13.
 */
public abstract class InputEvent {

    float x, y;

    public InputEvent(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static enum InputType {
        MOVE, STOP, JUMP
    }

    public abstract InputType getType();

}
