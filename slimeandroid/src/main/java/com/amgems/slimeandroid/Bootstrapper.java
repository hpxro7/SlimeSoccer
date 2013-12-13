package com.amgems.slimeandroid;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.*;
import android.os.Build;
import com.amgems.slimeandroid.engine.GameEngine;
import com.amgems.slimeandroid.engine.GameSurfaceView;
import com.amgems.slimeandroid.engine.Renderer;

import java.util.Queue;

public class Bootstrapper extends Activity {

    private GameEngine mEngine;
    private GameSurfaceView mGameSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        mGameSurfaceView = new GameSurfaceView(this);
        mEngine = new GameEngine(mGameSurfaceView);
        setContentView(mGameSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEngine.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mEngine.pause();
    }

}
