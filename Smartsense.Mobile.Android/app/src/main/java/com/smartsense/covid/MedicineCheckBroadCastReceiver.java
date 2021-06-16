package com.smartsense.covid;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.smartsense.covid.model.Medicine;
import com.smartsense.covid.model.MedicineTime;
import com.smartsense.covid.model.MedicineUsage;
import com.smartsense.covid.repo.MedicineRepository;
import com.smartsense.covid.repo.MedicineTimeRepo;
import com.smartsense.covid.repo.MedicineUsageRepo;

import java.util.Random;

public class MedicineCheckBroadCastReceiver extends BroadcastReceiver {

    private Context context;
    private int checkValueAdd = 100000;
    private MedicineUsageRepo medicineUsageRepo;
    private MedicineTimeRepo medicineTimeRepo;
    private MedicineRepository medicineRepository;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        medicineUsageRepo = new MedicineUsageRepo(context);
        medicineTimeRepo = new MedicineTimeRepo(context);
        medicineRepository = new MedicineRepository(context);
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Log.i("MedicineCheckBroadCast", "BOOT_COMPLETED");
        } else {
            Log.i("MedicineCheckBroadCast", "Alarm Time Check");
            if (intent.getExtras() != null) {
                String id = String.valueOf((intent.getExtras().getInt("id") - checkValueAdd));
                Log.i("MedicineCheckBroadCast", String.valueOf((intent.getExtras().getInt("id") - checkValueAdd)));

                MedicineUsage medicineUsage = medicineUsageRepo.getMedicineUsageByID(Long.parseLong(id));
                if (medicineUsage != null) {
                    if (!medicineUsage.isUsed()) {
                        MedicineTime medicineTime = medicineTimeRepo.getMedicineTimeByID(Long.parseLong(id));
                        if (medicineTime != null) {
                            Medicine medicine = medicineRepository.getMedicineByID(medicineTime.getForeignMedicineID());
                            if (medicine != null) {
                                sendNotification((context.getString(R.string.medicine_reminding) + " - " + medicine.getMedicineName() + " - " + medicineTime.getDose()), context.getString(R.string.notify_medicine_not_used));
                            }
                        }
                    }
                }
            }
        }
    }

    private void sendNotification(String title, String messageBody) {
        int notificationId = new Random().nextInt(1000);

        Intent contentIntent = new Intent(context, CovidMainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 1, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        String channelId = context.getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.ic_pill_notify)
                        .setContentTitle(title)
                        //.setLargeIcon(icon)
                        .setContentText(messageBody)
                        .setSound(defaultSoundUri)
                        .setContentIntent(resultPendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setVibrate(new long[]{100, 200, 200, 100});

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        context.getResources().getString(R.string.app_name),
                        NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription(context.getResources().getString(R.string.notification_description));
                channel.enableLights(true);
                channel.enableVibration(true);
                notificationManager.createNotificationChannel(channel);
            }
        }

        if (notificationManager != null) {
            notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());
        }
    }

}
