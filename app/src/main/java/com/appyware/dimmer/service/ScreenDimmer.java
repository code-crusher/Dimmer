package com.appyware.dimmer.service;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.appyware.dimmer.MainActivity;
import com.appyware.dimmer.R;
import com.appyware.dimmer.helper.Constants;
import com.appyware.dimmer.helper.SuperPrefs;
import com.appyware.dimmer.models.ActivityEvent;
import com.appyware.dimmer.models.ServiceEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by
 * --Vatsal Bajpai on
 * --21/08/16 at
 * --2:33 PM
 */

public class ScreenDimmer extends Service implements Constants {

    HUDView mView, mView_bg;
    NotificationManager mNotificationManager;
    SuperPrefs superPrefs;

    @Subscribe
    public void OnActivityEvent(ActivityEvent event) {
        if (mView != null)
            mView.setBackgroundColor(getColorInt(event.dimValue));
        setupNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals("STOP")) {

                superPrefs.setBool(KEY_DIM, false);
                EventBus.getDefault().post(new ServiceEvent(EVENT_SERVICE));

                if (mNotificationManager != null) {
                    mNotificationManager.cancelAll();
                }
                stopService(new Intent(getApplicationContext(), ScreenDimmer.class));

            } else if (intent.getAction().equals("PAUSE")) {

                if (mView_bg != null) {
                    mView_bg.setBackgroundColor(Color.TRANSPARENT);
                }
                if (mView != null)
                    mView.setBackgroundColor(Color.TRANSPARENT);

            } else if (intent.getAction().equals("START")) {

                int p = superPrefs.getInt(KEY_DIM_VALUE, 0);

                if (mView != null)
                    mView.setBackgroundColor(getColorInt(p));

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

        superPrefs = new SuperPrefs(getApplicationContext());

        // EventBus
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        mView = new HUDView(this);
        mView_bg = new HUDView(this);
        mView_bg.setBackgroundColor(Color.parseColor("#99000000"));

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

        int p = superPrefs.getInt(KEY_DIM_VALUE, 0);

        if (mView != null)
            mView.setBackgroundColor(getColorInt(p));

        setupNotification();
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
        intentStop.setAction("STOP");
        PendingIntent pIntentStop = PendingIntent.getService(context, 0, intentStop, 0);

        Intent intentPause = new Intent(context, ScreenDimmer.class);
        intentPause.setAction("PAUSE");
        PendingIntent pIntentPause = PendingIntent.getService(context, 0, intentPause, 0);

        Intent intentStart = new Intent(context, ScreenDimmer.class);
        intentStart.setAction("START");
        PendingIntent pIntentStart = PendingIntent.getService(context, 0, intentStart, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context).setOngoing(true)
                        .setSmallIcon(R.drawable.ic_eye)
                        .setContentTitle("Dimmer running")
                        .setContentText("Tap to open")
                        .setContentIntent(pIntentActivity)
                        .addAction(R.drawable.ic_pause, "Pause", pIntentPause)
                        .addAction(R.drawable.ic_stop, "Stop", pIntentStop)
                        .addAction(R.drawable.ic_play_arrow, "Start", pIntentStart)
                        .setAutoCancel(true);

        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(2, mBuilder.build());
    }

    @SuppressLint("DefaultLocale")
    public String dateFormat(int time) {
        return String.format("%02d", time);
    }

    private int getColorInt(int value) {
        return Color.parseColor("#" + dateFormat(value) + "000000");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

    class HUDView extends ViewGroup {

        public HUDView(Context context) {
            super(context);
            setBackgroundColor(Color.parseColor("#10000000"));
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

        }

        @Override
        protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
        }

    }
}
