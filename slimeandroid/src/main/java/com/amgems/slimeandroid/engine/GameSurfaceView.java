package com.amgems.slimeandroid.engine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.amgems.slimeandroid.entity.Slime;

/**
 * Created by shermpay on 12/12/13.
 */
public class GameSurfaceView extends SurfaceView implements Renderer {
    private static final int TARGET_WIDTH = 1280;
    private static final int TARGET_HEIGHT = 720;
    private static final int SLIME_SIZE = 150;

    private SurfaceHolder mHolder;
    Bitmap bg;
    Slime player1;


    public GameSurfaceView(Context context) {
        super(context);
        mHolder = getHolder();
        mHolder.setFixedSize(TARGET_WIDTH, TARGET_HEIGHT);
        bg = Bitmap.createBitmap(TARGET_WIDTH, TARGET_HEIGHT, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bg);
        Paint p = new Paint();
        p.setColor(Color.BLUE);
        canvas.drawRect(0, 0, TARGET_WIDTH, (float) 0.9 * TARGET_HEIGHT, p);
        Log.d("Stuff", "width: " + TARGET_WIDTH + " height: " + TARGET_HEIGHT);
        p.setColor(Color.GRAY);
        canvas.drawRect(0, (float) 0.9 * TARGET_HEIGHT, TARGET_WIDTH, TARGET_HEIGHT, p);
        player1 = new Slime((float) 0.5 * TARGET_WIDTH, (float) 0.9 * TARGET_HEIGHT - 75 , SLIME_SIZE, Color.GREEN);
    }

    @Override
    public void draw(long deltaTime) {
        Canvas surfaceCanvas = mHolder.lockCanvas();
        surfaceCanvas.drawBitmap(bg, 0, 0, null);

        // Draw stuff
        player1.drawBitMap(surfaceCanvas);
        mHolder.unlockCanvasAndPost(surfaceCanvas);
    }

    @Override
    public boolean isSurfaceReady() {
        return mHolder.getSurface().isValid();
    }
}
