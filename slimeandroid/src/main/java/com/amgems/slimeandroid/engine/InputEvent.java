package com.amgems.slimeandroid.engine;

import android.graphics.Point;

/**
 * Created by shermpay on 12/12/13.
 */
public class InputEvent {

    private InputType mType;
    float x, y;

    public static enum InputType {
        MOVE, STOP, JUMP
    }

    public InputEvent(float x, float y, InputType type) {
        mType = type;
        this.x = x;
        this.y = y;
    }

    public InputType getType() {
        return mType;
    }

}
