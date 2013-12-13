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

    private SurfaceHolder mHolder;
    Bitmap bg;
    Slime player1;


    public GameSurfaceView(Context context) {
        super(context);
        mHolder = getHolder();
        bg = Bitmap.createBitmap(1280, 720, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bg);
        Paint p = new Paint();
        p.setColor(Color.BLUE);
        canvas.drawRect(0, 0, getWidth(), (float) 0.9 * getHeight(), p);
        Log.d("Stuff", "width: " + getWidth() + " height: " + getHeight());
        p.setColor(Color.GRAY);
        canvas.drawRect((float) 0.9 * getHeight(), 0, getWidth(), getHeight(), p);
        player1 = new Slime((float) 0.5 * getWidth(), (float) 0.9 * getHeight() + 50 , Color.GREEN);
        mHolder.setFixedSize(TARGET_WIDTH, TARGET_HEIGHT);
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
