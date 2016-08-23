package com.appyware.dimmer.helper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

/**
 * Created by
 * --Vatsal Bajpai on
 * --23/08/16 at
 * --8:33 AM
 */
public class PermissionChecker {

    public final static int REQUEST_CODE = 101;

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkDrawOverlayPermission(Activity activity) {
        if (!Settings.canDrawOverlays(activity))
            return false;
        return true;
    }

    public static void requestDrawOverlayPermission(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + activity.getPackageName()));
        activity.startActivityForResult(intent, REQUEST_CODE);
    }
}
