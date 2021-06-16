package com.smartsense.covid;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;

public class AlarmService extends IntentService {


    public AlarmService() {
        super("AlarmService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i("AlarmService", "onHandleIntent");
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(this, MyBroadCastReceiver.class);
        Intent alarmCheckIntent = new Intent(this, MedicineCheckBroadCastReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        PendingIntent checkPendingIntent = PendingIntent.getBroadcast(this, 0, alarmCheckIntent, 0);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i("AlarmService", "if");
            //alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
        } else {
            Log.i("AlarmService", "else");
            //alarmManager.setExact(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
        }

    }
}
