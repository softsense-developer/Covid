package com.smartsense.covid.location;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.smartsense.covid.R;

import org.greenrobot.eventbus.EventBus;

public class MyBackgroundService extends Service {

    private static final String TAG = "MyBackgroundService";
    private final IBinder mBinder = new LocalBinder();

    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 15 * 60 * 1000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =15 * 60 * 1000;


    private static final String CHANNEL_ID = "Location Services";
    private static final int NOTI_ID = 1224;
    private static final String EXTRA_STARTED_FROM_NOTIFICATION = "com.smartsense.locationapp" + ".started_from_notification";

    private boolean mChangingConfiguration = false;
    private NotificationManager mNotificationManager;

    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private Handler mServiceHandler;
    private Location mLocation;

    public MyBackgroundService() {

    }


    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate: ");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onNewLocation(locationResult.getLastLocation());
            }
        };

        createLocationRequest();
        getLastLocation();

        HandlerThread handlerThread = new HandlerThread("Smartsense");
        handlerThread.start();
        mServiceHandler = new Handler(handlerThread.getLooper());
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ");
        boolean startedFromNotification = intent.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION, false);

        if (startedFromNotification) {
            removeLocationUpdates();
            stopSelf();
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i(TAG, "onConfigurationChanged: ");
        super.onConfigurationChanged(newConfig);
        mChangingConfiguration = true;
    }

    public void removeLocationUpdates() {
        try {
            Log.i(TAG, "removeLocationUpdates: ");
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            Common.setRequestingLocationUpdates(this, false);
            stopSelf();
        } catch (SecurityException e) {
            Common.setRequestingLocationUpdates(this, true);
            Log.e(TAG, getString(R.string.not_have_location_permission_for_remove) + ": " + e);
        }
    }

    private void getLastLocation() {
        try {
            Log.i(TAG, "getLastLocation: ");
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        mLocation = task.getResult();
                    } else {
                        Log.e(TAG, getString(R.string.not_get_location));
                    }
                }
            });
        } catch (SecurityException e) {
            Log.e(TAG, getString(R.string.location_permission_gone) + ": " + e);
        }
    }

    private void createLocationRequest() {
        Log.i(TAG, "createLocationRequest: ");
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void onNewLocation(Location lastLocation) {
        Log.i(TAG, "onNewLocation: ");
        mLocation = lastLocation;
        EventBus.getDefault().postSticky(new SendLocationToActivity(mLocation));
        if (serviceIsRunningInForeGround(this)) {
            mNotificationManager.notify(NOTI_ID, getNotification());
        }
    }

    private Notification getNotification() {
        Log.i(TAG, "getNotification: ");
        Intent intent = new Intent(this, MyBackgroundService.class);
        String text = Common.getLocationText(mLocation);

        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true);
        //PendingIntent servicePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //PendingIntent activityPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, CovidMainActivity.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                //.addAction(R.drawable.ic_launcher_foreground, "Launch", activityPendingIntent)
                //.addAction(R.drawable.ic_launcher_background, "Remove", servicePendingIntent)
                .setContentText(getString(R.string.location_service_running))
                .setContentTitle(getString(R.string.app_name))
                .setOngoing(true)
                .setSound(null)
                .setPriority(Notification.PRIORITY_LOW)
                .setSmallIcon(R.drawable.ic_service_running)
                //.setTicker(text)
                .setNotificationSilent()
                .setWhen(System.currentTimeMillis());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (mNotificationManager != null) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                        getResources().getString(R.string.app_name),
                        NotificationManager.IMPORTANCE_LOW);
                channel.setDescription(getResources().getString(R.string.notification_description));
                channel.enableLights(false);
                channel.enableVibration(false);
                channel.setSound(null,null);
                mNotificationManager.createNotificationChannel(channel);
            }
        }

        return builder.build();
    }

    private boolean serviceIsRunningInForeGround(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
            if (getClass().getName().equals(service.service.getClassName()))
                if (service.foreground)
                    return true;
        return false;
    }

    public void requestLocationUpdates() {
        Log.i(TAG, "requestLocationUpdates: ");
        Common.setRequestingLocationUpdates(this, true);
        startService(new Intent(getApplicationContext(), MyBackgroundService.class));
        try {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        } catch (SecurityException e) {
            Log.e("Smartsense", "Lost location permission. Could not request it " + e);
        }
    }

    public class LocalBinder extends Binder {
        public MyBackgroundService getService() {
            return MyBackgroundService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: ");
        stopForeground(true);
        mChangingConfiguration = false;
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i(TAG, "onRebind: ");
        stopForeground(true);
        mChangingConfiguration = false;
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind: ");
        if (!mChangingConfiguration && Common.requestingLocationUpdates(this))
            startForeground(NOTI_ID, getNotification());
        return true;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        mServiceHandler.removeCallbacks(null);
        super.onDestroy();
    }
}
