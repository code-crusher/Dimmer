package com.appyware.dimmer.service;

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

                int p = superPrefs.getInt(KEY_DIM_VALUE, 0) + 10;

                if (mView != null)
                    mView.setBackgroundColor(getColorInt(p));

                if (mView_bg != null) {
                    mView_bg.setBackgroundColor(Color.parseColor("#99000000"));
                }
            }

        } else {
            superPrefs.setBool(KEY_DIM, true);
            EventBus.getDefault().post(new ServiceEvent(EVENT_SERVICE));
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
                WindowManager.LayoutParams.FILL_PARENT,
                WindowManager.LayoutParams.FILL_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);

        params.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.addView(mView_bg, params);
        wm.addView(mView, params);

        int p = superPrefs.getInt(KEY_DIM_VALUE, 0) + 10;

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

        Intent intent = new Intent(getApplicationContext(), ScreenDimmer.class);
        intent.setAction("STOP");
        PendingIntent pIntent = PendingIntent.getService(getApplicationContext(), 0, intent, 0);

        Intent intent1 = new Intent(getApplicationContext(), ScreenDimmer.class);
        intent1.setAction("PAUSE");
        PendingIntent pIntentPause = PendingIntent.getService(getApplicationContext(), 0, intent1, 0);

        Intent intent2 = new Intent(getApplicationContext(), ScreenDimmer.class);
        intent2.setAction("START");
        PendingIntent pIntentStart = PendingIntent.getService(getApplicationContext(), 0, intent2, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext()).setOngoing(true)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Dimmer running")
                        .setContentText("Tap to stop!")
                        .setContentIntent(pIntent)
                        .addAction(android.R.drawable.ic_media_pause, "Pause", pIntentPause)
                        .addAction(android.R.drawable.ic_media_play, "Start", pIntentStart)
                        .setAutoCancel(true);


        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(2, mBuilder.build());
    }

    private int getColorInt(int value) {
        return Color.parseColor("#" + value + "000000");
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
