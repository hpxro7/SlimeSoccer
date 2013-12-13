package com.amgems.slimeandroid.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

/**
 * Created by JeremyTeoMBP on 12/12/13.
 */
public class Slime {

    int size;
    float x;
    float y;
    Bitmap bm;

    public Slime(float x, float y, int slimeSize, int color) {
        size = slimeSize;
        this.x = x;
        this.y = y;
        Paint p = new Paint();
        p.setColor(color);
        bm = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        canvas.drawCircle(size / 2, size / 2, size / 2, p);
        canvas.drawRect(0, size / 2, size , size, p);
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        canvas.drawRect(0, size / 2, size , size, p);
    }

    public Rect getRectBounds() {
        return new Rect((int)x, (int)y, (int) x + size, (int) y + size / 2);
    }

    public void drawBitMap(Canvas canvas) {
        canvas.drawBitmap(bm, x, y, null);
    }
}
