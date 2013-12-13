package com.amgems.slimeandroid.engine;

import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by shermpay on 12/12/13.
 */
public class GameInputHandler implements View.OnTouchListener{
    public Queue<TouchEvent> mEventQueue;

    protected GameInputHandler() {
        mEventQueue = new LinkedList<TouchEvent>();
    }

    public static GameInputHandler getInstance(View view) {
        view.setOnTouchListener(InstanceHolder.INSTANCE);
        return InstanceHolder.INSTANCE;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        mEventQueue.add(new TouchEvent(event.getX(), event.getY()));
        return false;
    }

    private static class InstanceHolder {
        final static GameInputHandler INSTANCE = new GameInputHandler();
    }

    public static class TouchEvent {
        float x;
        float y;

        public TouchEvent(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
