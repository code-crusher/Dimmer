package com.appyware.dimmer.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.appyware.dimmer.Activities.AlertDialogActivity;
import com.appyware.dimmer.Activities.MainActivity;
import com.appyware.dimmer.R;
import com.appyware.dimmer.helper.Constants;
import com.appyware.dimmer.helper.Helper;
import com.appyware.dimmer.helper.SuperPrefs;
import com.appyware.dimmer.models.ActivityEvent;
import com.appyware.dimmer.models.ServiceEvent;
import com.crashlytics.android.answers.Answers;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.fabric.sdk.android.Fabric;

/**
 * Created by
 * --Vatsal Bajpai on
 * --21/08/16 at
 * --2:33 PM
 */

public class ScreenDimmer extends Service implements Constants, SensorEventListener {

    SurfaceView mView, mView_bg;
    NotificationManager mNotificationManager;
    SuperPrefs superPrefs;

    // hardware sensor
    SensorManager sensorManager;
    Sensor sensor;

    @Subscribe
    public void OnActivityEvent(ActivityEvent event) {
        if (mView != null)
            mView.setBackgroundColor(Helper.getColorInt(event.dimValue));
        setupNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setupSensor();

        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals(TAG_STOP)) {
                if (sensorManager != null)
                    sensorManager.unregisterListener(this);
                superPrefs.setBool(KEY_DIM, false);
                EventBus.getDefault().post(new ServiceEvent(EVENT_SERVICE));

                if (mNotificationManager != null) {
                    mNotificationManager.cancelAll();
                }
                stopService(new Intent(getApplicationContext(), ScreenDimmer.class));

            } else if (intent.getAction().equals(TAG_PAUSE)) {
                if (sensorManager != null)
                    sensorManager.unregisterListener(this);
                if (mView_bg != null) {
                    mView_bg.setBackgroundColor(Color.TRANSPARENT);
                }
                if (mView != null)
                    mView.setBackgroundColor(Color.TRANSPARENT);

            } else if (intent.getAction().equals(TAG_START)) {

                int p = superPrefs.getInt(KEY_DIM_VALUE, 0);

                if (mView != null)
                    mView.setBackgroundColor(Helper.getColorInt(p));

                if (mView_bg != null) {
                    mView_bg.setBackgroundColor(Color.parseColor("#99000000"));
                }
            }

        } else {
            //superPrefs.setBool(KEY_DIM, true);
            //  EventBus.getDefault().post(new ServiceEvent(EVENT_SERVICE));
        }

        return START_STICKY;
        //return super.onStartCommand(intent, flags, startId);
    }

    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Answers());

        init();
    }

    private void init() {
        superPrefs = new SuperPrefs(getApplicationContext());

        // EventBus
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        mView = new SurfaceView(this);
        mView_bg = new SurfaceView(this);
        mView_bg.setBackgroundColor(Color.parseColor("#99000000"));

        // making service view fullscreen
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        params.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.addView(mView_bg, params);
        wm.addView(mView, params);

        setInitialState();
        setupNotification();
    }

    private void setupSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(this, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void setInitialState() {
        int p = superPrefs.getInt(KEY_DIM_VALUE, 0);

        if (mView != null)
            mView.setBackgroundColor(Helper.getColorInt(p));
    }

    private void setupNotification() {
        if (superPrefs.getBool(KEY_NOTI))
            fireNotification();

        if (!superPrefs.getBool(KEY_NOTI) && mNotificationManager != null)
            mNotificationManager.cancelAll();
    }

    private void fireNotification() {

        Context context = getApplicationContext();

        Intent intentActivity = new Intent(context, MainActivity.class);
        PendingIntent pIntentActivity = PendingIntent.getActivity(context, 0, intentActivity, 0);

        Intent intentStop = new Intent(context, ScreenDimmer.class);
        intentStop.setAction(TAG_STOP);
        PendingIntent pIntentStop = PendingIntent.getService(context, 0, intentStop, 0);

        Intent intentPause = new Intent(context, ScreenDimmer.class);
        intentPause.setAction(TAG_PAUSE);
        PendingIntent pIntentPause = PendingIntent.getService(context, 0, intentPause, 0);

        Intent intentStart = new Intent(context, ScreenDimmer.class);
        intentStart.setAction(TAG_START);
        PendingIntent pIntentStart = PendingIntent.getService(context, 0, intentStart, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context).setOngoing(true)
                        .setSmallIcon(R.drawable.ic_eye)
                        .setContentTitle(getString(R.string.dimmer_running))
                        .setContentText(getString(R.string.tap_open))
                        .setContentIntent(pIntentActivity)
                        .addAction(R.drawable.ic_pause, TAG_PAUSE, pIntentPause)
                        .addAction(R.drawable.ic_stop, TAG_STOP, pIntentStop)
                        .addAction(R.drawable.ic_play_arrow, TAG_START, pIntentStart)
                        .setAutoCancel(true);

        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(2, mBuilder.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sensorManager != null)
            sensorManager.unregisterListener(this);
        EventBus.getDefault().unregister(this);
        if (mView != null && mView_bg != null) {
            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mView);
            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mView_bg);
            mView = null;
            mView_bg = null;
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        float x = sensorEvent.values[0];
        if (x > OPTIMAL_ABMIENT) {
            pauseServiceIntent();
            AlertDialogActivity.startActivity(this);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    /**
     * service helper methods
     */
    private void pauseServiceIntent() {
        Intent intent = new Intent(getApplicationContext(), ScreenDimmer.class);
        intent.setAction(TAG_PAUSE);
        startService(intent);
    }

    private void stopServiceIntent() {
        Intent intent = new Intent(getApplicationContext(), ScreenDimmer.class);
        intent.setAction(TAG_STOP);
        startService(intent);
    }

    private void startServiceIntent() {
        Intent intent = new Intent(getApplicationContext(), ScreenDimmer.class);
        intent.setAction(TAG_START);
        startService(intent);
    }
}
