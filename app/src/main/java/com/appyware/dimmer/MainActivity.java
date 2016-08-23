package com.appyware.dimmer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.appyware.dimmer.helper.Constants;
import com.appyware.dimmer.helper.PermissionChecker;
import com.appyware.dimmer.helper.SuperPrefs;
import com.appyware.dimmer.models.ActivityEvent;
import com.appyware.dimmer.models.ServiceEvent;
import com.appyware.dimmer.service.ScreenDimmer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by
 * --Vatsal Bajpai on
 * --21/08/16 at
 * --2:33 PM
 */
public class MainActivity extends AppCompatActivity implements Constants {

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

    @Subscribe
    public void OnServiceEvent(ServiceEvent event) {
        setupCheckBoxes();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        superPrefs = new SuperPrefs(this);
        permissionCheck(this);
        setupCheckBoxes();
        setupTime();
        setupSeekBar();
        setupFab(this);
    }

    private void permissionCheck(final Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionChecker.checkDrawOverlayPermission(activity)) {
                new AlertDialog.Builder(activity)
                        .setTitle("Permission Request")
                        .setMessage("To use this application Draw Overlay Permission is required")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                PermissionChecker.requestDrawOverlayPermission(activity);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PermissionChecker.REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void setupTime() {
        textStartTime.setText(dateFormat(superPrefs.getInt(KEY_START_HOUR, 22)) + ":" + dateFormat(superPrefs.getInt(KEY_START_MIN, 0)));
        textEndTime.setText(dateFormat(superPrefs.getInt(KEY_STOP_HOUR, 7)) + ":" + dateFormat(superPrefs.getInt(KEY_STOP_MIN, 0)));
    }

    private void setupCheckBoxes() {
        if (cbAuto != null && cbNoti != null) {
            try {
                cbNoti.setChecked(superPrefs.getBool(KEY_NOTI));
                cbAuto.setChecked(superPrefs.getBool(KEY_AUTO));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setupSeekBar() {
        seekBar.setMax(MAX_SEEK_VALUE);
        seekBar.setProgress(superPrefs.getInt(KEY_DIM_VALUE, 0));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                superPrefs.setInt(KEY_DIM_VALUE, progress);
                EventBus.getDefault().post(new ActivityEvent(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setupFab(final Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            fabDim.setImageResource(R.drawable.anim_tick_cross);
            fabDim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    animate(view);
                    superPrefs.setBoolNot(KEY_DIM);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!PermissionChecker.checkDrawOverlayPermission(activity))
                            permissionCheck(activity);
                        else
                            startService(new Intent(getApplicationContext(), ScreenDimmer.class));
                    } else
                        startService(new Intent(getApplicationContext(), ScreenDimmer.class));
                }
            });

        } else {
            fabDim.setImageResource(R.drawable.tick);
            fabDim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    superPrefs.setBoolNot(KEY_DIM);
                    startService(new Intent(getApplicationContext(), ScreenDimmer.class));
                }
            });
        }
    }

    @OnClick({R.id.cb_noti, R.id.cb_auto})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cb_noti:
                onCheckClick(KEY_NOTI);
                if (seekBar != null)
                    EventBus.getDefault().post(new ActivityEvent(seekBar.getProgress()));
                break;
            case R.id.cb_auto:
                onCheckClick(KEY_AUTO);
                break;
        }
    }

    private void onCheckClick(String KEY) {
        superPrefs.setBoolNot(KEY);
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

    @SuppressLint("DefaultLocale")
    public String dateFormat(int time) {
        return String.format("%02d", time);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // init();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}
