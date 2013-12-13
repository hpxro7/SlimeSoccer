package com.amgems.slimeandroid.engine;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

/**
 * Created by shermpay on 12/12/13.
 */
public class GameSurfaceView extends SurfaceView implements Renderer {
    private SurfaceHolder mHolder;


    public GameSurfaceView(Context context) {
        super(context);
        mHolder = getHolder();
    }

    @Override
    public void draw(long deltaTime) {
        Canvas surfaceCanvas = mHolder.lockCanvas();

        // Draw stuff
        mHolder.unlockCanvasAndPost(surfaceCanvas);
    }

    @Override
    public boolean isSurfaceReady() {
        return mHolder.getSurface().isValid();
    }
}
