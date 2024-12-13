package com.harsh.shah.saavnmp3;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.media.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.harsh.shah.saavnmp3.activities.MainActivity;
import com.harsh.shah.saavnmp3.services.NotificationReceiver;
import com.harsh.shah.saavnmp3.utils.MediaPlayerUtil;

public class ApplicationClass extends Application {

    public static final String CHANNEL_ID_1 = "channel_1";
    public static final String CHANNEL_ID_2 = "channel_2";
    public static final String ACTION_NEXT = "next";
    public static final String ACTION_PREV = "prev";
    public static final String ACTION_PLAY = "play";

    public static final MediaPlayerUtil mediaPlayerUtil = MediaPlayerUtil.getInstance();
    private MediaSessionCompat mediaSession;

    private String MUSIC_TITLE = "";
    private String MUSIC_DESCRIPTION = "";
    public String IMAGE_URL = "";

    @Override
    public void onCreate() {
        super.onCreate();
        mediaSession = new MediaSessionCompat(this, "ApplicationClass");
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

    public void setMusicDetails(String image, String title, String description) {
        IMAGE_URL = image;
        MUSIC_TITLE = title;
        MUSIC_DESCRIPTION = description;
    }

    public void showNotification(int playPauseButton) {
        try {

            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);

            Intent prevIntent = new Intent(this, NotificationReceiver.class).setAction(ApplicationClass.ACTION_PREV);
            PendingIntent prevPendingIntent = PendingIntent.getBroadcast(this, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent playIntent = new Intent(this, NotificationReceiver.class).setAction(ApplicationClass.ACTION_PLAY);
            PendingIntent playPendingIntent = PendingIntent.getBroadcast(this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent nextIntent = new Intent(this, NotificationReceiver.class).setAction(ApplicationClass.ACTION_NEXT);
            PendingIntent nextPendingIntent = PendingIntent.getBroadcast(this, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            Glide.with(this)
                    .asBitmap()
                    .load(IMAGE_URL) // Replace with your URL string
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                            Notification notification = new androidx.core.app.NotificationCompat.Builder(ApplicationClass.this, ApplicationClass.CHANNEL_ID_2)
                                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                                    .setLargeIcon(resource)
                                    .setContentTitle(MUSIC_TITLE)
                                    .setContentText(MUSIC_DESCRIPTION)
                                    .addAction(R.drawable.skip_previous_24px, "prev", prevPendingIntent)
                                    .addAction(playPauseButton, "play", playPendingIntent)
                                    .addAction(R.drawable.skip_next_24px, "next", nextPendingIntent)
                                    .setStyle(new NotificationCompat.MediaStyle().setMediaSession(mediaSession.getSessionToken()))
                                    .setPriority(Notification.PRIORITY_LOW)
                                    .setContentIntent(contentIntent)
                                    .setOnlyAlertOnce(true)
                                    .build();

                            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            notificationManager.notify(0, notification);


                        }

                        @Override
                        public void onLoadCleared(Drawable placeholder) {
                            // Handle placeholder if needed
                        }
                    });

        } catch (Exception e) {
            Log.e("ApplicationClass", "showNotification: ", e);
        }
    }
}
