package com.appyware.dimmer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.appyware.dimmer.helper.AlarmHelper;
import com.appyware.dimmer.helper.Constants;
import com.appyware.dimmer.helper.SuperPrefs;

/**
 * Created by
 * --Vatsal Bajpai on
 * --06/09/16 at
 * --1:14 AM
 */
public class ServiceBootReceiver extends BroadcastReceiver implements Constants {

    @Override
    public void onReceive(Context context, Intent intent) {
        SuperPrefs superPrefs = SuperPrefs.newInstance(context);

        if (superPrefs.getBool(KEY_AUTO)) {
            AlarmHelper alarmHelper = new AlarmHelper(context);
            alarmHelper.cancel();
            alarmHelper.startAlarm(superPrefs.getInt(KEY_START_HOUR, 22), superPrefs.getInt(KEY_START_MIN, 0));
            alarmHelper.stopAlarm(superPrefs.getInt(KEY_STOP_HOUR, 6), superPrefs.getInt(KEY_STOP_MIN, 0));
        }
    }


}
