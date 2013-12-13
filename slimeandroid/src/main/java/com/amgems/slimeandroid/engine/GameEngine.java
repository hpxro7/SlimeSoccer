package com.amgems.slimeandroid.engine;

import android.util.Log;

/**
 * Created by zac on 12/12/13.
 */
public class GameEngine implements Runnable {

    private Thread mThread;
    private volatile boolean mIsRunning;

    public GameEngine() {

    }

    @Override
    public void run() {
        while (mIsRunning) {

        }

    }

    public void pause() {
        mIsRunning = false;
        while (true) {
            try {
                mThread.join();
                break;
            } catch (InterruptedException interruptException) {
                Log.d(GameEngine.class.getSimpleName(), "Interrupted onPause");
            }
        }
    }

    public void resume() {
        mThread = new Thread(this);
        mIsRunning = true;
        mThread.start();
    }

}
