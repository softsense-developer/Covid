package com.smartsense.covid;

import android.app.AlarmManager;
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

import com.smartsense.covid.adapters.medicineTimeDoseAdapter.Item;
import com.smartsense.covid.model.Medicine;
import com.smartsense.covid.model.MedicineTime;
import com.smartsense.covid.model.MedicineUsage;
import com.smartsense.covid.repo.MedicineRepository;
import com.smartsense.covid.repo.MedicineTimeRepo;
import com.smartsense.covid.repo.MedicineUsageRepo;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MyBroadCastReceiver extends BroadcastReceiver {

    private Context context;
    private MedicineUsageRepo medicineUsageRepo;
    private MedicineTimeRepo medicineTimeRepo;
    private MedicineRepository medicineRepository;
    private int checkValueAdd = 100000;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        medicineUsageRepo = new MedicineUsageRepo(context);
        medicineTimeRepo = new MedicineTimeRepo(context);
        medicineRepository = new MedicineRepository(context);


        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Log.i("MyBroadCastReceiver", "BOOT_COMPLETED");

            List<MedicineTime> medicineTimes = medicineTimeRepo.getAllDataList();
            if (medicineTimes != null) {

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                Intent alarmIntent = new Intent(context, MyBroadCastReceiver.class);
                Intent alarmCheckIntent = new Intent(context, MedicineCheckBroadCastReceiver.class);

                for (int i = 0; i < medicineTimes.size(); i++) {
                    Item item = new Item();
                    item.setTimeStamp(medicineTimes.get(i).getTimeUsage());
                    item.setDose(medicineTimes.get(i).getDose());

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context
                            , (int) medicineTimes.get(i).getMedicineTimeID()
                            , alarmIntent.putExtra("id", ((int) medicineTimes.get(i).getMedicineTimeID())), 0);
                    PendingIntent pendingIntentCheck = PendingIntent.getBroadcast(context
                            , (int) (medicineTimes.get(i).getMedicineTimeID() + checkValueAdd)
                            , alarmCheckIntent.putExtra("id", ((int) (medicineTimes.get(i).getMedicineTimeID() + checkValueAdd))), 0);


                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(item.getTimeStamp());
                    calendar.add(Calendar.HOUR_OF_DAY, 1);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                                item.getTimeStamp(), AlarmManager.INTERVAL_DAY, pendingIntent);

                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP
                                , calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentCheck);

                    } else {
                        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, item.getTimeStamp(), AlarmManager.INTERVAL_DAY, pendingIntent);

                        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentCheck);
                    }
                    Log.i("MyBroadCastReceiver", "Alarm " + (int) medicineTimes.get(i).getMedicineTimeID() + " " + item.getTimeStamp() + " added");
                    Log.i("MyBroadCastReceiver", "Alarm Check " + ((int) medicineTimes.get(i).getMedicineTimeID() + checkValueAdd) + " " + calendar.getTimeInMillis() + " added");
                }
            }
        } else {
            Log.i("MyBroadCastReceiver", "Alarm Manager just ran");
            if (intent.getExtras() != null) {
                String id = String.valueOf(intent.getExtras().getInt("id"));
                Log.i("MyBroadCastReceiver", id);

                MedicineTime medicineTime = medicineTimeRepo.getMedicineTimeByID(Long.parseLong(id));
                if (medicineTime != null) {
                    Medicine medicine = medicineRepository.getMedicineByID(medicineTime.getForeignMedicineID());
                    if (medicine != null) {
                        sendNotification((context.getString(R.string.medicine_reminding) + " - " + medicine.getMedicineName() + " - " + medicineTime.getDose()), context.getString(R.string.use_your_medicine));
                        MedicineUsage medicineUsage = new MedicineUsage(medicineTime.getMedicineTimeID(), medicineTime.getTimeUsage(), false, true);
                        medicineUsageInsert(medicineUsage);
                    }
                }
            }
        }
    }


    private void medicineUsageInsert(MedicineUsage medicineUsage) {
        medicineUsageRepo.insert(medicineUsage);
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
