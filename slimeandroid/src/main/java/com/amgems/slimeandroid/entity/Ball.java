package com.amgems.slimeandroid.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by JeremyTeoMBP on 13/12/13.
 */
public class Ball {
    private static final int TARGET_WIDTH = 1280;
    private static final int TARGET_HEIGHT = 720;

    public float gravity;
    public float x;
    public float y;
    public float dx;
    public float dy;
    private int size;
    private Bitmap bm;

    public Ball(float x, float y, int slimeSize, float gravity, int color) {
        size = slimeSize;
        this.x = x;
        this.y = y;
        this.dx = 0f;
        this.dy = 0f;
        this.gravity = gravity;
        Paint p = new Paint();
        p.setColor(color);
        bm = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        canvas.drawCircle(size / 2, size / 2, size / 2, p);
    }

    public void checkGround(float deltaTime) {
        if (y >= 0.9f * TARGET_HEIGHT - size / 2) {
            y = 0.9f * TARGET_HEIGHT - size / 2;
            dy *= -0.7;
        } else {
            dy += gravity * deltaTime;
        }
    }

    public void checkBounds() {
        if (x > TARGET_WIDTH - size / 2) {
            x = TARGET_WIDTH - size / 2;
            dx *= -0.5;
        } else if (x < size / 2) {
            x = size / 2;
            dx *= -0.5;
        }
    }

    public void checkCollision(Slime slime) {
        if (Math.abs(slime.x - x) * Math.abs(slime.x - x) +
                Math.abs(slime.y - y) * Math.abs(slime.y - y) <
                (slime.size + size) * (slime.size + size)) {

        }
    }

    public void update(float deltaTime, Slime slime) {

    }
    public void drawBitMap(Canvas canvas) {
        canvas.drawBitmap(bm, x - size / 2, y - size / 2, null);
    }
}
