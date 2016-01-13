package com.davicaetano.runningwithmusic.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.widget.NumberPicker;

import com.davicaetano.runningwithmusic.CustomApplication;
import com.davicaetano.runningwithmusic.R;
import com.davicaetano.runningwithmusic.ui.modules.NewSessionModule;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewSessionActivity extends BaseActivity{

    @Bind(R.id.button_start) CardView buttonStart;
    @Bind(R.id.numberPicker) NumberPicker numberPicker;
    @Bind(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_session);
        ButterKnife.bind(this);

        numberPicker.setMinValue(10);
        numberPicker.setMaxValue(100);


        toolbar.setTitle("Runnig with music");
        setSupportActionBar(toolbar);
    }

    @Override
    public void setupActivityComponent() {
        ((CustomApplication)getApplication()).getAppContextComponent()
                .plus(new NewSessionModule(this))
                .inject(this);
    }

    @OnClick(R.id.button_start)
    public void onButtonStartClick(){
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.setAction("START");
        intent.putExtra("TIME",numberPicker.getValue());
        startActivity(intent);
    }

}
