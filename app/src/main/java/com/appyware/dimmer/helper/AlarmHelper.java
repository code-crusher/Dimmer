package com.appyware.dimmer.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.appyware.dimmer.service.ScreenDimmer;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by
 * --Vatsal Bajpai on
 * --06/09/16 at
 * --1:19 AM
 */
public class AlarmHelper {

    Context context;

    public AlarmHelper(Context context) {
        this.context = context;
    }

    public void startAlarm(int startHour, int startMin) {

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long interval = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);

        Intent intent = new Intent(context.getApplicationContext(), ScreenDimmer.class);
        PendingIntent pIntent = PendingIntent.getService(context.getApplicationContext(), 0, intent, 0);

        /* Set the alarm to start at TimePicker time */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, startHour);
        calendar.set(Calendar.MINUTE, startMin);

        /* Repeating on everyday */
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                interval, pIntent);
        Log.d("alarm", "started");
    }

    public void stopAlarm(int stopHour, int stopMin) {

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long interval = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);

        Intent intent = new Intent(context.getApplicationContext(), ScreenDimmer.class);
        intent.setAction("STOP");
        PendingIntent pIntent = PendingIntent.getService(context.getApplicationContext(), 0, intent, 0);

        /* Stop the alarm at TimePicker time */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, stopHour);
        calendar.set(Calendar.MINUTE, stopMin);

        /* Stopping everyday */
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                interval, pIntent);
        Log.d("alarm", "stop");

    }

    public void cancel() {

        Intent intent = new Intent(context.getApplicationContext(), ScreenDimmer.class);
        PendingIntent pIntent = PendingIntent.getService(context.getApplicationContext(), 0, intent, 0);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pIntent);

        intent.setAction("STOP");
        pIntent = PendingIntent.getService(context.getApplicationContext(), 0, intent, 0);
        manager.cancel(pIntent);
    }
}
