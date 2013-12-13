package com.amgems.slimeandroid.engine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.amgems.slimeandroid.entity.Ball;
import com.amgems.slimeandroid.entity.Slime;

/**
 * Created by shermpay on 12/12/13.
 */
public class GameSurfaceView extends SurfaceView implements Renderer {
    private static final int TARGET_WIDTH = 1280;
    private static final int TARGET_HEIGHT = 720;
    private static final int SLIME_SIZE = 150;
    private static final int BALL_SIZE = 40;
    private static final float GRAVITY = 1800f;

    private SurfaceHolder mHolder;
    private GameInputHandler mGameInputHandler;
    private float mScaleXRatio;
    private float mScaleYRatio;
    Bitmap mBackgroundBitmap; // Badass grandma?
    Slime mPlayer;
    Ball mBall;


    public GameSurfaceView(Context context) {
        super(context);
        mHolder = getHolder();
        mHolder.setFixedSize(TARGET_WIDTH, TARGET_HEIGHT);
        init();
    }

    public void init() {
        mBackgroundBitmap = Bitmap.createBitmap(TARGET_WIDTH, TARGET_HEIGHT, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mBackgroundBitmap);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.WHITE);
        canvas.drawRect(0f, 0f, TARGET_WIDTH, 0.9f * TARGET_HEIGHT, p);
        p.setColor(Color.GRAY);
        canvas.drawRect(0f, 0.9f * TARGET_HEIGHT, TARGET_WIDTH, TARGET_HEIGHT, p);
        mPlayer = new Slime(0.5f * TARGET_WIDTH, 0.9f * TARGET_HEIGHT - 75 , SLIME_SIZE, GRAVITY, Color.RED);
        mBall = new Ball(0.1f * TARGET_WIDTH, 0.3f * TARGET_HEIGHT, BALL_SIZE, GRAVITY, Color.YELLOW);
    }

    public void registerInputHandler(GameInputHandler gameInputHandler) {
        mGameInputHandler = gameInputHandler;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mScaleXRatio = (float)TARGET_WIDTH / MeasureSpec.getSize(widthMeasureSpec);
        mScaleYRatio = (float)TARGET_HEIGHT / MeasureSpec.getSize(heightMeasureSpec);
    }

    public float getScaledX(float x) {
        return mScaleXRatio * x;
    }

    public float getScaledY(float y) {
        return mScaleYRatio * y;
    }

    public void consumeQueue(Canvas surfaceCanvas) {
        synchronized (mGameInputHandler.eventQueue) {
            //Log.v(GameSurfaceView.class.getSimpleName(), "Queue size " + mGameInputHandler.eventQueue.size());
            while (!mGameInputHandler.eventQueue.isEmpty()) {
                GameInputHandler.TouchEvent event = mGameInputHandler.eventQueue.remove();
                Log.v(GameSurfaceView.class.getSimpleName(), "I'm being processsssssssssssssssed"); // TODO: REMOVE
                if (getScaledX(event.x) >= mPlayer.x) {
                    mPlayer.dx = 800f;
                } else {
                    mPlayer.dx = -800f;
                }
            }
        }
    }

    @Override
    public void draw(float deltaTime) {
        Canvas surfaceCanvas = mHolder.lockCanvas();
        // Get Input/Change Velocity
        // E.g mPlayer.dx = -200f;
        consumeQueue(surfaceCanvas);
        mPlayer.checkGround(deltaTime);
        mPlayer.checkBounds();
        mBall.checkGround(deltaTime);
        mBall.checkBounds();

        // Change positions

        mPlayer.x += deltaTime * mPlayer.dx;
        mPlayer.y += deltaTime * mPlayer.dy;
        mBall.x += deltaTime * mBall.dx;
        mBall.y += deltaTime * mBall.dy;


        // Check for collisions between players/ball
        //mBall.checkCollision(mPlayer);

        surfaceCanvas.drawBitmap(mBackgroundBitmap, 0, 0, null);

        // Draw stuff
        mPlayer.drawBitMap(surfaceCanvas);

        mBall.drawBitMap(surfaceCanvas);
        mHolder.unlockCanvasAndPost(surfaceCanvas);
    }

    @Override
    public boolean isSurfaceReady() {
        return mHolder.getSurface().isValid();
    }
}
