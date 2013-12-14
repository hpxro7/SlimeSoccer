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

    public boolean checkGround() {
        if (y > 0.9f * TARGET_HEIGHT - size / 2) {
            y = 2 * (0.9f * TARGET_HEIGHT - size / 2) - y;
            dy *= -0.8;
            dx *= 0.8;
            Log.d("Bounce", "y = " + y);
            return true;
        }
        return false;
    }

    public boolean checkBounds() {
        if (x > TARGET_WIDTH - size / 2) {
            x = 2 * (TARGET_WIDTH - size / 2) - x;
            dx *= -0.8;
            dy *= 0.8;
            return true;
        } else if (x < size / 2) {
            x = size + x;
            dx *= -0.8;
            dy *= 0.8;
            return true;
        }
        return false;
    }

    public boolean checkCollision(Slime slime) {
        float diff = Math.abs(slime.x - x) * Math.abs(slime.x - x) + Math.abs(slime.y - y) * Math.abs(slime.y - y);
        if ( diff < (slime.size / 2 + size / 2) * (slime.size / 2 + size / 2) && slime.y > y) {
            double slimeAngle;
            if (slime.dx == 0) {
                if (slime.dy > 0) slimeAngle = Math.PI / 2;
                else slimeAngle = Math.PI / -2;
            } else {
                slimeAngle = Math.atan(slime.dy / slime.dx);
                if (slime.dx < 0) {
                    slimeAngle *= -1;
                }
            }
            double ballAngle;
            if (dx == 0) {
                if (dy > 0) ballAngle = Math.PI / 2;
                else ballAngle = Math.PI / -2;
            } else {
                ballAngle = Math.atan(dy / dx);
                if (dx < 0) {
                    ballAngle *= -1;
                }
            }
            double slimeSpeed = Math.sqrt(slime.dy * slime.dy + slime.dx * slime.dx);
            double ballSpeed = Math.sqrt(dy * dy + dx * dx);
            double contactAngle = Math.atan((slime.y - y) / (slime.x - x));
            double collisionX = slime.x + (x - slime.x) * (slime.size / 2 + size / 2) / Math.sqrt(diff);
            double collisionY = slime.y + (y - slime.y) * (slime.size / 2 + size / 2) / Math.sqrt(diff);
            //Log.d("Slime", "SLIME X: " + slime.x + " Y: " + slime.y);
            //Log.d("Difference", "Diff: " + Math.sqrt(diff));
            //Log.d("Initial", "Initial X: " + x + " Y: " + y);
            //Log.d("Collision", "Collision X: " + collisionX + " Y: " + collisionY);
            double penetrationTime = (y - collisionY) / dy;
            //Log.d("Penetration", "Penetration: " + penetrationTime);
            dx = (float) ((2 * slimeSpeed * Math.cos(slimeAngle - contactAngle) - ballSpeed * Math.cos(ballAngle - contactAngle)) * Math.cos(contactAngle) +
                    ballSpeed * Math.sin(ballAngle - contactAngle) * Math.cos(contactAngle + Math.PI / 2)) * 0.9f;
            dy = (float) ((2 * slimeSpeed * Math.cos(slimeAngle - contactAngle) - ballSpeed * Math.cos(ballAngle - contactAngle)) * Math.sin(contactAngle) +
                    ballSpeed * Math.sin(ballAngle - contactAngle) * Math.sin(contactAngle + Math.PI / 2)) * 0.9f;
            x = (float) (collisionX + penetrationTime * dx);
            y = (float) (collisionY + penetrationTime * dy);
            //Log.d("Bounce", "X: " + x + " Y: " + y);
            return true;
        }
        return false;
    }

    public void update(float deltaTime, Slime slime) {
        x += dx * deltaTime;
        y += dy * deltaTime;
        Log.d("asdf", "y = " + y);
        int count = 0;
        while (count++ < 10 && checkCollision(slime) ||  checkBounds() || checkGround()) { }
        dy += gravity * deltaTime;
    }

    public void drawBitMap(Canvas canvas) {
        canvas.drawBitmap(bm, x - size / 2, y - size / 2, null);
    }
}
