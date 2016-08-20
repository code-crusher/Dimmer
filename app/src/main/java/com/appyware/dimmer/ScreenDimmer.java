package com.appyware.dimmer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.SeekBar;

/**
 * Created by appyware on 10/07/15.
 */
public class ScreenDimmer extends Service {

    HUDView mView, mView_bg;
    String bright;
    SharedPreferences preferences;
    NotificationManager mNotificationManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (intent.getAction() != null) {
                if (intent.getAction().equals("STOP")) {
                    stopService(new Intent(getApplicationContext(), ScreenDimmer.class));
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("tick", 0);
                    editor.commit();

                    if (mNotificationManager != null) {
                        mNotificationManager.cancelAll();
                    }

                    if (MainActivity.checkBox_start != null)
                        MainActivity.checkBox_start.setChecked(false);
                } else if (intent.getAction().equals("PAUSE")) {

                    if (mView_bg != null) {
                        mView_bg.setBackgroundColor(Color.TRANSPARENT);
                    }
                    if (mView != null)
                        mView.setBackgroundColor(Color.TRANSPARENT);

                } else if (intent.getAction().equals("START")) {

                    preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    int p = preferences.getInt("slider", 0);
                    p = p + 10;
                    if (mView != null)
                        mView.setBackgroundColor(Color.parseColor("#" + p + "000000"));

                    if (mView_bg != null) {
                        mView_bg.setBackgroundColor(Color.parseColor("#99000000"));
                    }

                }

            }

        } else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("tick", 1);
            editor.commit();

            if (MainActivity.checkBox_start != null)
                MainActivity.checkBox_start.setChecked(false);

        }

        return START_STICKY;
        //return super.onStartCommand(intent, flags, startId);
    }

    public void onCreate() {
        super.onCreate();
        mView = new HUDView(this);
        mView_bg = new HUDView(this);
        mView_bg.setBackgroundColor(Color.parseColor("#99000000"));

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        bright = preferences.getString("bright", "null");

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

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int p = preferences.getInt("slider", 0);
        p = p + 10;
        if (mView != null)
            mView.setBackgroundColor(Color.parseColor("#" + p + "000000"));

        if (MainActivity.seekBar != null) {
            MainActivity.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("slider", progress);
                    editor.commit();

                    progress = 10 + progress;

                    if (mView != null)
                        mView.setBackgroundColor(Color.parseColor("#" + progress + "000000"));

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }

        int noti = preferences.getInt("tickNotification", 1);

        if (noti == 1) {

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
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle("Dimmer running")
                            .setContentText("Tap to stop!")
                            .setContentIntent(pIntent)
                            .addAction(R.drawable.ic_av_play_arrow, "Pause", pIntentPause)
                            .addAction(R.drawable.ic_av_pause, "Start", pIntentStart)
                            .setAutoCancel(true);


            mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(2, mBuilder.build());

        }
        if (noti == 0 && mNotificationManager != null) {
            mNotificationManager.cancelAll();
        }

        final int i = preferences.getInt("tick", -1);


        if (MainActivity.checkBox_noti != null) {
            MainActivity.checkBox_noti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked && i == 1) {

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("tickNotification", 1);
                        editor.commit();

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
                                        .setSmallIcon(R.drawable.ic_launcher)
                                        .setContentTitle("Dimmer running")
                                        .setContentText("Tap to stop!")
                                        .setContentIntent(pIntent)
                                        .addAction(android.R.drawable.ic_media_pause, "Pause", pIntentPause)
                                        .addAction(android.R.drawable.ic_media_play, "Start", pIntentStart)
                                        .setAutoCancel(true);


                        mNotificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(2, mBuilder.build());

                    } else {

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("tickNotification", 0);
                        editor.commit();

                        mNotificationManager.cancelAll();
                    }
                }
            });

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mView != null && mView_bg != null) {
            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mView);
            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mView_bg);
            mView = null;
            mView_bg = null;
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // stopService(new Intent(getApplicationContext(), ScreenDimmer.class));
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
