package com.amgems.slimeandroid.engine;

import android.graphics.Canvas;

/**
 * Created by shermpay on 12/12/13.
 */
public interface Renderer {

    public void draw(float deltaTime);

    public boolean isSurfaceReady();
}
