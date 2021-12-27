package com.bharatapp.sgvuBus.forground;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bharatapp.sgvuBus.R;
import com.bharatapp.sgvuBus.activities.driver;

import static com.bharatapp.sgvuBus.forground.App.CHANNEL_ID;


public class ExampleService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notificationlayout);
        contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
        contentView.setTextViewText(R.id.title, "SGVU BUS Tract");
        contentView.setTextViewText(R.id.text, "This is a custom layout");


        String input = intent.getStringExtra("inputExtra");

        Intent notificationIntent = new Intent(this, driver.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                //.setContentTitle("SGVU BUS Tract")
              //  .setContentText(input)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContent(contentView)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        //do heavy work on a background thread
        //stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}