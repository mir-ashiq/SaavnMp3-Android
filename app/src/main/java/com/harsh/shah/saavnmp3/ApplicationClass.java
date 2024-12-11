package com.harsh.shah.saavnmp3;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.harsh.shah.saavnmp3.utils.MediaPlayerUtil;

public class ApplicationClass extends Application {

    public static final String CHANNEL_ID_1 = "channel_1";
    public static final String CHANNEL_ID_2 = "channel_2";
    public static final String ACTION_NEXT = "next";
    public static final String ACTION_PREV = "prev";
    public static final String ACTION_PLAY = "play";

    public static final MediaPlayerUtil mediaPlayerUtil = MediaPlayerUtil.getInstance();

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel1 = new NotificationChannel(CHANNEL_ID_1, "ch1", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel1.setDescription("channel 1 description");
            NotificationChannel notificationChannel2 = new NotificationChannel(CHANNEL_ID_2, "ch1", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel2.setDescription("channel 2 description");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel1);
            notificationManager.createNotificationChannel(notificationChannel2);

        }
    }
}
