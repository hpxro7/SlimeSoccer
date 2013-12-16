package com.amgems.slimeandroid.engine;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by shermpay on 12/12/13.
 */
public class GameInputHandler implements View.OnTouchListener, SensorEventListener{

    private static GameInputHandler sInstance;

    public Queue<InputEvent> eventQueue;
    private ScheduledExecutorService mExecutor;

    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;

    protected GameInputHandler(SensorManager sensorManager) {
        eventQueue = new LinkedList<InputEvent>();
        mSensorManager = sensorManager;

        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public static synchronized GameInputHandler getInstance(View view, SensorManager sensorManager) {
        if (sInstance == null) {
            sInstance = new GameInputHandler(sensorManager);
            view.setOnTouchListener(sInstance);
        }
        return sInstance;
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
                    eventQueue.add(new InputEvent(event.getX(), event.getY(), InputEvent.InputType.STOP));
                    return true;
                default:
                    return false;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (eventQueue) {
            eventQueue.add(new InputEvent(event.values[0], event.values[1], InputEvent.InputType.JUMP));
        }
    }

    public void beginLongHold(MotionEvent event) {
        mExecutor = Executors.newSingleThreadScheduledExecutor();
        AddLongHoldTask task = new AddLongHoldTask(new InputEvent(event.getX(), event.getY(), InputEvent.InputType.MOVE));
        mExecutor.scheduleWithFixedDelay(task, 0l, 100l, TimeUnit.MILLISECONDS);
    }

    public void endLongHold() {
        mExecutor.shutdownNow();
    }

    public void resume() {
        mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void pause() {
        mSensorManager.unregisterListener(this, mAccelerometerSensor);
    }

    private class AddLongHoldTask implements Runnable {
        private InputEvent moveEvent;

        public AddLongHoldTask(InputEvent touchEvent) {
            this.moveEvent = touchEvent;
        }

        @Override
        public void run() {
            Log.v(GameInputHandler.class.getSimpleName(), "Added to the queue!");
            eventQueue.add(moveEvent);
        }

    }

}
