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

    private static final long LONG_HOLD_DELAY = 125l;

    float[] mGravity;
    float[] mLinearAcceleration;

    private static final float JUMP_THRESHOLD = 2.35f;
    private static final float mAlpha = 0.8f;
    private static final int DIMENSION = 3;
    private static final int X = 0;
    private static final int Y = 1;
    private static final int Z = 2;

    protected GameInputHandler(SensorManager sensorManager) {
        eventQueue = new LinkedList<InputEvent>();
        mSensorManager = sensorManager;
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mGravity = new float[DIMENSION];
        mLinearAcceleration = new float[DIMENSION];
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
    public void onAccuracyChanged(Sensor sensor, int accuracy) {   }

    @Override
    public void onSensorChanged(SensorEvent event) {
        applyLowPassFilter(event.values, mLinearAcceleration);
        if (mLinearAcceleration[X] > 1f) {
            Log.d(GameSurfaceView.class.getSimpleName(), "Acceleration: " + mLinearAcceleration[X] + ", threshold: " + JUMP_THRESHOLD);
        }
        if (mLinearAcceleration[X] > JUMP_THRESHOLD) {
            //Log.d(GameSurfaceView.class.getSimpleName(), "x: " + (mLinearAcceleration[X]) + ", y: " + (mLinearAcceleration[Y]) +
            //      ", z: " + (mLinearAcceleration[Z]));
            synchronized (eventQueue) {
                eventQueue.add(new InputEvent(mLinearAcceleration[X], mLinearAcceleration[Z], InputEvent.InputType.JUMP));
            }
        }
    }

    private void applyLowPassFilter(float[] accelerationValues, float[] linearAcceleration) {
        mGravity[X] = mGravity[X] * mAlpha + (1 - mAlpha) * (accelerationValues[X]);
        mGravity[Y] = mGravity[Y] * mAlpha + (1 - mAlpha) * (accelerationValues[Y]);
        mGravity[Z] = mGravity[Z] * mAlpha + (1 - mAlpha) * (accelerationValues[Z]);

        linearAcceleration[X] = accelerationValues[X] - mGravity[X];
        linearAcceleration[Y] = accelerationValues[Y] - mGravity[Y];
        linearAcceleration[Z] = accelerationValues[Z] - mGravity[Z];
    }

    public void beginLongHold(MotionEvent event) {
        mExecutor = Executors.newSingleThreadScheduledExecutor();
        AddLongHoldTask task = new AddLongHoldTask(new InputEvent(event.getX(), event.getY(), InputEvent.InputType.MOVE));
        mExecutor.scheduleWithFixedDelay(task, 0, LONG_HOLD_DELAY, TimeUnit.MILLISECONDS);
    }

    public void endLongHold() {
        mExecutor.shutdownNow();
    }

    public void resume() {
        mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);
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
