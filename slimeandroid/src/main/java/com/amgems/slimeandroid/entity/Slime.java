package com.amgems.slimeandroid.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by JeremyTeoMBP on 12/12/13.
 */
public class Slime {

    float x;
    float y;
    Bitmap bm;

    public Slime(float x, float y, int color) {
        this.x = x;
        this.y = y;
        Paint p = new Paint();
        p.setColor(color);
        bm = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        canvas.drawCircle(50, 50, 50, p);
        p.setAlpha(0);
        canvas.drawRect(0, 50, 100, 100, p);
    }

    public Rect getRectBounds() {
        return new Rect((int)x, (int)y, (int)x + 100, (int)y + 50);
    }

    public void drawBitMap(Canvas canvas) {
        canvas.drawBitmap(bm, x, y, null);
    }
}
