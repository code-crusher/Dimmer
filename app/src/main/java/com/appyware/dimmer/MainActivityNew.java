package com.appyware.dimmer;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.appyware.dimmer.helper.Constants;
import com.appyware.dimmer.helper.SuperPrefs;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by
 * --Vatsal Bajpai on
 * --21/08/16 at
 * --2:33 PM
 */
public class MainActivityNew extends AppCompatActivity implements Constants {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @BindView(R.id.seekBar)
    AppCompatSeekBar seekBar;
    @BindView(R.id.text_start_time)
    TextView textStartTime;
    @BindView(R.id.text_end_time)
    TextView textEndTime;
    @BindView(R.id.cv_main)
    CardView cvMain;
    @BindView(R.id.cb_noti)
    AppCompatCheckBox cbNoti;
    @BindView(R.id.cb_auto)
    AppCompatCheckBox cbAuto;
    @BindView(R.id.fab_dim)
    FloatingActionButton fabDim;

    private SuperPrefs superPrefs;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        superPrefs = new SuperPrefs(this);

        setupCheckBoxes();
        setupSeekBar();
        setupFab();
    }

    private void setupCheckBoxes() {
        cbNoti.setChecked(superPrefs.getBool(KEY_NOTI));
        cbAuto.setChecked(superPrefs.getBool(KEY_AUTO));
    }

    private void setupSeekBar() {
        seekBar.setMax(MAX_SEEK_VALUE);
    }

    private void setupFab() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            fabDim.setImageResource(R.drawable.anim_tick_cross);
            fabDim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    animate(view);
                }
            });

        } else {
            fabDim.setImageResource(R.drawable.tick);
        }
    }

    private void animate(View view) {
        Drawable drawable = ((FloatingActionButton) view).getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
    }

    public void rate(View view) {
        Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.cb_noti, R.id.cb_auto})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cb_noti:
                onCheckClick(KEY_NOTI);
                break;
            case R.id.cb_auto:
                onCheckClick(KEY_AUTO);
                break;
        }
    }

    private void onCheckClick(String KEY) {
        superPrefs.setBool(KEY, !superPrefs.getBool(KEY));
        setupCheckBoxes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }
}
