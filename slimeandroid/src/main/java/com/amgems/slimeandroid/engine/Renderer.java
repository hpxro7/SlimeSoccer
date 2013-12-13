package com.amgems.slimeandroid.engine;

import android.graphics.Canvas;

/**
 * Created by shermpay on 12/12/13.
 */
public interface Renderer {

    public void draw(long deltaTime);

    public boolean isSurfaceReady();
}
