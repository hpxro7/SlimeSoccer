package com.amgems.slimeandroid;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.*;
import android.os.Build;
import com.amgems.slimeandroid.engine.GameEngine;

public class Bootstrapper extends Activity {

    private GameEngine mEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEngine = new GameEngine();
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
