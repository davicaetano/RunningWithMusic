package com.davicaetano.runningwithmusic.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by davicaetano on 1/2/16.
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivityComponent();
    }

    public abstract void setupActivityComponent();
}
