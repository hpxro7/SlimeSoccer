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
        if (y > 0.9f * TARGET_HEIGHT - size / 2) {
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
        float diff = Math.abs(slime.x - x) * Math.abs(slime.x - x) + Math.abs(slime.y - y) * Math.abs(slime.y - y);
        Log.d("WHERE IS THE BOALL", "y: " + y + "x: " + x);
        if ( diff < (slime.size / 2 + size / 2) * (slime.size / 2 + size / 2) && slime.y > y) {
            //if (Math.sqrt(diff) > 0.4 * slime.size) {
                // You dont want to know
                double slimeAngle = 0;
                if (slime.dy == 0) {
                    if (slime.dx == 0) {
                        slimeAngle = 0;
                    }
                    else slimeAngle = Math.atan(slime.dx > 0 ? 1 / Float.POSITIVE_INFINITY : 1 / Float.NEGATIVE_INFINITY);
                } else if (slime.dx == 0) {
                    slimeAngle = Math.atan(slime.dy > 0 ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY);
                } else {
                    slimeAngle = Math.atan(slime.dy / slime.dx);
                }
                double ballAngle = 0;
                if (dy == 0) {
                    if (dx == 0) {
                        ballAngle = 0;
                    }
                    else ballAngle = Math.atan(dx > 0 ? 1 / Float.POSITIVE_INFINITY : 1 / Float.NEGATIVE_INFINITY);
                } else if (dx == 0) {
                    ballAngle = Math.atan(dy > 0 ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY);
                } else {
                    ballAngle = Math.atan(dy / dx);
                }
                double slimeSpeed = Math.sqrt(slime.dy * slime.dy + slime.dx * slime.dx);
                double ballSpeed = Math.sqrt(dy * dy + dx * dx);
                double contactAngle = Math.atan((slime.y - y) / (slime.x - x));
                dx = (float) ((2 * slimeSpeed * Math.cos(slimeAngle - contactAngle) - ballSpeed * Math.cos(ballAngle - contactAngle)) * Math.cos(contactAngle) +
                        ballSpeed * Math.sin(ballAngle - contactAngle) * Math.cos(contactAngle + Math.PI / 2)) * 0.9f;
                dy = (float) ((2 * slimeSpeed * Math.cos(slimeAngle - contactAngle) - ballSpeed * Math.cos(ballAngle - contactAngle)) * Math.sin(contactAngle) +
                        ballSpeed * Math.sin(ballAngle - contactAngle) * Math.sin(contactAngle + Math.PI / 2)) * 0.9f;
            //} else {
            //    dy *= -0.9;
            //}
        }
    }

    public void update(float deltaTime, Slime slime) {
        checkCollision(slime);
        checkBounds();
        checkGround(deltaTime);
    }

    public void drawBitMap(Canvas canvas) {
        canvas.drawBitmap(bm, x - size / 2, y - size / 2, null);
    }
}
