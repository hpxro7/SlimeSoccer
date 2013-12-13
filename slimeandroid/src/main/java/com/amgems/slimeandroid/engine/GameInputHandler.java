package com.amgems.slimeandroid.engine;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by shermpay on 12/12/13.
 */
public class GameInputHandler implements View.OnTouchListener{
    public Queue<TouchEvent> eventQueue;
    private ScheduledExecutorService mExecutor;

    protected GameInputHandler() {
        eventQueue = new LinkedList<TouchEvent>();
    }

    public static GameInputHandler getInstance(View view) {
        view.setOnTouchListener(InstanceHolder.INSTANCE);
        return InstanceHolder.INSTANCE;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        synchronized (eventQueue) {

            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    beginLongHold(event);
                    return true;
                case MotionEvent.ACTION_UP:
                    endLongHold();
                    return true;
                default:
                    return false;
            }
        }
    }

    public void beginLongHold(MotionEvent event) {
        mExecutor = Executors.newSingleThreadScheduledExecutor();
        AddLongHoldTask task = new AddLongHoldTask(new TouchEvent(event.getX(), event.getY()));
        mExecutor.scheduleWithFixedDelay(task, 0l, 100l, TimeUnit.MILLISECONDS);
    }

    public void endLongHold() {
        mExecutor.shutdownNow();
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

    private class AddLongHoldTask implements Runnable {
        private TouchEvent touchEvent;

        public AddLongHoldTask(TouchEvent touchEvent) {
            this.touchEvent = touchEvent;
        }

        @Override
        public void run() {
            Log.v(GameInputHandler.class.getSimpleName(), "Added to the queue!");
            eventQueue.add(touchEvent);
        }


    }
}
