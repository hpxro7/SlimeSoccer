package com.amgems.slimeandroid.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

/**
 * Created by JeremyTeoMBP on 12/12/13.
 */
public class Slime {
    private static final int TARGET_WIDTH = 1280;
    private static final int TARGET_HEIGHT = 720;

    public static final int HORIZONTAL_VELOCITY = 800;

    public float gravity;
    public float x;
    public float y;
    public float dx;
    public float dy;
    public int size;
    private Bitmap bm;

    public Slime(float x, float y, int slimeSize, float gravity, int color) {
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
        canvas.drawRect(0, size / 2, size , size, p);
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        canvas.drawRect(0, size / 2, size , size, p);
    }

    public void checkGround(float deltaTime) {
        if (y > 0.9f * TARGET_HEIGHT) {
            y = 0.9f * TARGET_HEIGHT;
            dy = 0;
        } else {
            dy += gravity * deltaTime;
        }
    }

    public void checkBounds() {
        if (x > TARGET_WIDTH - size / 2) {
            x = TARGET_WIDTH - size / 2;
            dx = 0;
        } else if (x < size / 2) {
            x = size / 2;
            dx = 0;
        }
    }

    public void drawBitMap(Canvas canvas) {
        canvas.drawBitmap(bm, x - size / 2, y - size / 2, null);
    }
}
