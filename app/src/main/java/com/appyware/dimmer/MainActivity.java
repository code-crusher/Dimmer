package com.appyware.dimmer;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.rey.material.widget.CheckBox;


public class MainActivity extends AppCompatActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private AdView adView;
    public CheckBox checkBox_start, checkBox_noti, checkBox_auto;
    public SeekBar seekBar;
    SharedPreferences preferences;
    LinearLayout infotext;
    TextView startTime, endTime;
    int i = 0, noti, auto, flag;
    int sh, sm, eh, em;
    android.support.v7.widget.AppCompatImageButton fab;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.tool);
//        setSupportActionBar(toolbar);
//        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
//
//        adView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);
//
//
//        checkBox_start = (CheckBox) findViewById(R.id.checkbox1);
//        checkBox_noti = (CheckBox) findViewById(R.id.checkbox2);
//        checkBox_auto = (CheckBox) findViewById(R.id.checkbox3);
//        seekBar = (SeekBar) findViewById(R.id.seekBar);
//        infotext = (LinearLayout) findViewById(R.id.infoText);
//        startTime = (TextView) findViewById(R.id.text_start_time);
//        endTime = (TextView) findViewById(R.id.text_end_time);
//        fab = (android.support.v7.widget.AppCompatImageButton) findViewById(R.id.fab);
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                shareUs(v);
//            }
//        });
//        infotext.setVisibility(View.VISIBLE);
//        seekBar.setMax(89);
//
//        preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        i = preferences.getInt("tick", -1);
//        noti = preferences.getInt("tickNotification", -1);
//        auto = preferences.getInt("auto", 0);
//
//        if (noti == 0)
//            checkBox_noti.setChecked(false);
//        else {
//            checkBox_noti.setChecked(true);
//        }
//
//        if (auto == 0)
//            checkBox_auto.setChecked(false);
//        else {
//            checkBox_auto.setChecked(true);
//        }
//
//
//        preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        int p = preferences.getInt("slider", 0);
//        seekBar.setProgress(p);
//
//
//        sh = preferences.getInt("SH", 22);
//        sm = preferences.getInt("SM", 0);
//        eh = preferences.getInt("EH", 7);
//        em = preferences.getInt("EM", 0);
//
//        String start = dateFormat(sh) + " : " + dateFormat(sm);
//        String end = dateFormat(eh) + " : " + dateFormat(em);
//        startTime.setText(start);
//        endTime.setText(end);
//
//        if (i == 0)
//            checkBox_start.setChecked(false);
//        else if (i == 1) {
//            stopService(new Intent(getApplicationContext(), ScreenDimmer.class));
//            checkBox_start.setChecked(true);
//            startService(new Intent(getApplicationContext(), ScreenDimmer.class));
//        }
//        checkBox_start.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                if (isChecked) {
//                    startService(new Intent(getApplicationContext(), ScreenDimmer.class));
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putInt("tick", 1);
//                    editor.commit();
//
//                } else {
//                    Intent intent = new Intent(getApplicationContext(), ScreenDimmer.class);
//                    intent.setAction("STOP");
//                    startService(intent);
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putInt("tick", 0);
//                    editor.commit();
//                }
//            }
//        });
//
//        checkBox_auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                if (isChecked) {
//
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putInt("auto", 1);
//                    editor.commit();
//
//                    int sh = preferences.getInt("SH", 22);
//                    int sm = preferences.getInt("SM", 0);
//                    int eh = preferences.getInt("EH", 7);
//                    int em = preferences.getInt("EM", 0);
//
//                    startAlarm(sh, sm);
//                    stopAlarm(eh, em);
//                } else {
//
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putInt("auto", 0);
//                    editor.commit();
//                    cancel();
//                }
//            }
//        });
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        View view = null;
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_rate) {
//            rateUs(view);
//            return true;
//        }
//        if (id == R.id.action_share) {
//            shareUs(view);
//            return true;
//        }
//        if (id == R.id.action_mail) {
//            mailUs(view);
//            return true;
//        }
//        if (id == R.id.action_account) {
//            account(view);
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    public void cancel() {
//
//        Intent intent = new Intent(getApplicationContext(), ScreenDimmer.class);
//        PendingIntent pIntent = PendingIntent.getService(getApplicationContext(), 0, intent, 0);
//
//        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        manager.cancel(pIntent);
//    }
//
//
//    private void stopAlarm(int endHour, int endMin) {
//
//        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        long interval = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
//
//        Intent intent = new Intent(getApplicationContext(), ScreenDimmer.class);
//        intent.setAction("STOP");
//        PendingIntent pIntent = PendingIntent.getService(getApplicationContext(), 0, intent, 0);
//
//
//        /* Stop the alarm at TimePicker time */
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, endHour);
//        calendar.set(Calendar.MINUTE, endMin);
//
//        /* Stopping everyday */
//        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                interval, pIntent);
//    }
//
//    private void startAlarm(int startHour, int startMin) {
//
//        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        long interval = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
//
//        Intent intent = new Intent(getApplicationContext(), ScreenDimmer.class);
//        PendingIntent pIntent = PendingIntent.getService(getApplicationContext(), 0, intent, 0);
//
//
//        /* Set the alarm to start at TimePicker time */
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, startHour);
//        calendar.set(Calendar.MINUTE, startMin);
//
//        /* Repeating on everyday */
//        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                interval, pIntent);
//
//
//    }
//
//    public String dateFormat(int time) {
//
//        return String.format("%02d", time);
//
//    }
//
//    public void startTimePicker(View view) {
//
//        flag = 1;
//        Calendar now = Calendar.getInstance();
//
//        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this,
//                now.get(Calendar.HOUR),
//                now.get(Calendar.MINUTE), true);
//
//
//        timePickerDialog.show(getFragmentManager(), "Timepickerdialog");
//
//    }
//
//    public void stopTimePicker(View view) {
//
//        flag = 0;
//        Calendar now = Calendar.getInstance();
//
//        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this,
//                now.get(Calendar.HOUR),
//                now.get(Calendar.MINUTE), true);
//
//        timePickerDialog.show(getFragmentManager(), "Timepickerdialog");
//
//    }
//
//    @Override
//    public void onTimeSet(RadialPickerLayout radialPickerLayout, int Hour, int Min) {
//
//        cancel();
//
//        SharedPreferences.Editor editor = preferences.edit();
//
//        String start = dateFormat(Hour) + " : " + dateFormat(Min);
//        if (flag == 1) {
//
//            startTime.setText(start);
//            editor.putInt("SH", Hour);
//            editor.putInt("SM", Min);
//
//        } else if (flag == 0) {
//
//            editor.putInt("EH", Hour);
//            editor.putInt("EM", Min);
//            endTime.setText(start);
//
//        }
//
//        checkBox_auto.setChecked(false);
//
//        editor.apply();
//    }
//
//    /**
//     * Called when leaving the activity
//     */
//    @Override
//    public void onPause() {
//        if (adView != null) {
//            adView.pause();
//        }
//        super.onPause();
//    }
//
//    /**
//     * Called when returning to the activity
//     */
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (adView != null) {
//            adView.resume();
//        }
//    }
//
//    /**
//     * Called before the activity is destroyed
//     */
//    @Override
//    public void onDestroy() {
//        if (adView != null) {
//            adView.destroy();
//        }
//        super.onDestroy();
//    }
//
//    public void shareUs(View view) {
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("text/plain");
//        intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.appyware.dimmer");
//        intent.putExtra(Intent.EXTRA_SUBJECT, "Check out these awesome Material Wallpapers!");
//        startActivity(Intent.createChooser(intent, "Share"));
//    }
//
//    public void rateUs(View view) {
//        Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
//        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//        try {
//            startActivity(goToMarket);
//        } catch (ActivityNotFoundException e) {
//        }
//    }
//
//    public void account(View view) {
//        Uri uri = Uri.parse("https://play.google.com/store/apps/dev?id=8147199909251049753");
//        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//        try {
//            startActivity(goToMarket);
//        } catch (ActivityNotFoundException e) {
//        }
//    }
//
//    public void mailUs(View view) {
//        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
//                "mailto", "vatsal.bajpai05@gmail.com", null));
//        startActivity(Intent.createChooser(emailIntent, "Send email..."));
//    }

}
