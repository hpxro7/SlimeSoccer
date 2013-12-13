package com.amgems.slimeandroid.engine;

import android.util.Log;

/**
 * Created by zac on 12/12/13.
 */
public class GameEngine implements Runnable {
    private static final long INIT_TIME = 0;

    private Thread mThread;
    private Renderer mRenderer;
    private long mLastCallTime;

    private volatile boolean mIsRunning;

    public GameEngine(Renderer renderer) {
        mRenderer = renderer;
        mLastCallTime = INIT_TIME;
    }

    @Override
    public void run() {
        while (mIsRunning) {
            if (mRenderer.isSurfaceReady()) {
                if (mLastCallTime == INIT_TIME) {
                    mLastCallTime = System.currentTimeMillis();
                }
                long currentTime = System.currentTimeMillis();
                long deltaTime = currentTime - mLastCallTime;
                mLastCallTime = currentTime;
                mRenderer.draw((float) deltaTime / 1000);

            }
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
