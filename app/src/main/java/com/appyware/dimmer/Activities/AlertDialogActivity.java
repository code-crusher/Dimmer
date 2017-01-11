package com.appyware.dimmer.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.appyware.dimmer.R;
import com.appyware.dimmer.helper.Constants;
import com.appyware.dimmer.service.ScreenDimmer;

/**
 * Created by
 * --Vatsal Bajpai on
 * --18/09/16 at
 * --7:31 PM
 */
public class AlertDialogActivity extends AppCompatActivity implements Constants {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AlertDialogActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setFinishOnTouchOutside(false);

        setContentView(R.layout.activity_alert);

        setupAlert();
    }

    private void setupAlert() {
        new AlertDialog.Builder(this)
                .setTitle("Luminance Detected")
                .setMessage("High ambient light is detected. Do you wish to continue using Dimmer?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startServiceIntent();
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                })
                .show();
    }

    private void startServiceIntent() {
        Intent intent = new Intent(this, ScreenDimmer.class);
        intent.setAction(TAG_START);
        startService(intent);
    }
}
