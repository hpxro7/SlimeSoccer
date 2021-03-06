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
            // WHEN FINDING ANGLES, Y-axis is ***INVERTED***
            double slimeAngle;
            if (slime.dx == 0) {
                if (-slime.dy > 0) slimeAngle = Math.PI / 2;
                else slimeAngle = Math.PI / -2;
            } else if (slime.dy == 0) {
                if (slime.dx < 0) slimeAngle = Math.PI;
                else slimeAngle = 0.0;
            } else {
                slimeAngle = Math.atan(-slime.dy / slime.dx);
                if (slime.dx < 0) slimeAngle += Math.PI;
            }
            double ballAngle;
            if (dx == 0) {
                if (-dy > 0) ballAngle = Math.PI / 2;
                else ballAngle = Math.PI / -2;
            } else if (dy == 0) {
                if (dx < 0) ballAngle = Math.PI;
                else ballAngle = 0.0;
            } else {
                ballAngle = Math.atan(-dy / dx);
                if (dx < 0) ballAngle += Math.PI;
            }
            double slimeSpeed = Math.sqrt(slime.dy * slime.dy + slime.dx * slime.dx);
            double ballSpeed = Math.sqrt(dy * dy + dx * dx);
            // http://twobitcoder.blogspot.com/2010/04/circle-collision-detection.html
            double Vx = slime.dx - dx;
            double Vy = slime.dy - dy;
            double Px = slime.x - x;
            double Py = slime.y - y;
            double a = Vx * Vx + Vy * Vy;
            double b = 2 * (Vx * Px + Vy * Py);
            double c = (Px * Px + Py * Py) - (slime.size / 2 + size / 2) * (slime.size / 2 + size / 2);
            double discriminant = b * b - 4 * a * c;
            if (discriminant < 0) return false;
            double penetrationTime = (-b - Math.sqrt(discriminant)) / 2 / a;
            double collisionX = x + penetrationTime * dx;
            double collisionY = y + penetrationTime * dy;
            double slimeCollisionX = slime.x + penetrationTime * slime.dx;
            double slimeCollisionY = slime.y + penetrationTime * slime.dy;
            double contactAngle = (Math.atan((collisionY - slimeCollisionY) / (slimeCollisionX - collisionX)));
            if (slimeCollisionX - collisionX < 0) contactAngle += Math.PI;
            //Log.d("Difference", "Diff: " + Math.sqrt(diff));
            dx = (float) ((2 * slimeSpeed * Math.cos(slimeAngle - contactAngle) - ballSpeed * Math.cos(ballAngle - contactAngle)) * Math.cos(contactAngle) +
                    ballSpeed * Math.sin(ballAngle - contactAngle) * Math.cos(contactAngle + Math.PI / 2)) * 0.9f;
            dy = (float) ((2 * slimeSpeed * Math.cos(slimeAngle - contactAngle) - ballSpeed * Math.cos(ballAngle - contactAngle)) * Math.sin(contactAngle) +
                    ballSpeed * Math.sin(ballAngle - contactAngle) * Math.sin(contactAngle + Math.PI / 2)) * -0.9f;
            x = (float) (collisionX - penetrationTime * dx);
            y = (float) (collisionY - penetrationTime * dy);
            //Log.d("Bounce", "X: " + x + " Y: " + y);
            return true;
        }
        return false;
    }

    public void update(float deltaTime, Slime slime) {
        x += dx * deltaTime;
        y += dy * deltaTime;
        int count = 0;
        while (count++ < 10 && checkCollision(slime) ||  checkBounds() || checkGround()) { }
        dy += gravity * deltaTime;
    }

    public void drawBitMap(Canvas canvas) {
        canvas.drawBitmap(bm, x - size / 2, y - size / 2, null);
    }
}
