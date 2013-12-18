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
    Bitmap mBackgroundBitmap;
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
        mPlayer = new Slime(0.5f * TARGET_WIDTH, 0.9f * TARGET_HEIGHT - BALL_SIZE , SLIME_SIZE, GRAVITY, Color.RED);
        mBall = new Ball(0.45f * TARGET_WIDTH, 0.3f * TARGET_HEIGHT, BALL_SIZE, GRAVITY, Color.YELLOW);
    }

    public void registerInputHandler(GameInputHandler gameInputHandler) {
        mGameInputHandler = gameInputHandler;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mScaleXRatio = (float)TARGET_WIDTH / MeasureSpec.getSize(widthMeasureSpec);
    }

    public float getScaledX(float x) {
        return mScaleXRatio * x;
    }

    public void consumeQueue() {
        synchronized (mGameInputHandler.eventQueue) {
            while (!mGameInputHandler.eventQueue.isEmpty()) {
                InputEvent event = mGameInputHandler.eventQueue.remove();

                switch (event.getType()) {
                    case MOVE: {
                        if (getScaledX(event.x) >= TARGET_WIDTH / 2) {
                            mPlayer.dx = Slime.HORIZONTAL_VELOCITY;
                        } else {
                            mPlayer.dx = -Slime.HORIZONTAL_VELOCITY;
                        }
                        break;
                    }
                    case STOP: {
                        mPlayer.dx = 0f;
                        break;
                    }
                    case JUMP: {
                        mPlayer.dy = -600f;
                        break;
                    }
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void draw(float deltaTime) {
        Canvas surfaceCanvas = mHolder.lockCanvas();
        // Get Input/Change Velocity
        // E.g mPlayer.dx = -200f;
        consumeQueue();
        mPlayer.checkGround(deltaTime);
        mPlayer.checkBounds();
        mPlayer.x += deltaTime * mPlayer.dx;
        mPlayer.y += deltaTime * mPlayer.dy;
        mBall.update(deltaTime, mPlayer);


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
