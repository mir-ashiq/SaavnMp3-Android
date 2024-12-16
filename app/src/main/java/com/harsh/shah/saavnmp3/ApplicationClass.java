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
import com.harsh.shah.saavnmp3.activities.MusicOverviewActivity;
import com.harsh.shah.saavnmp3.services.NotificationReceiver;
import com.harsh.shah.saavnmp3.utils.MediaPlayerUtil;
import com.harsh.shah.saavnmp3.utils.SharedPreferenceManager;

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
    public String MUSIC_ID = "";

    public SharedPreferenceManager sharedPreferenceManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaSession = new MediaSessionCompat(this, "ApplicationClass");
        mediaSession.setActive(true);
        createNotificationChannel();
        sharedPreferenceManager = SharedPreferenceManager.getInstance(this);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel1 = new NotificationChannel(CHANNEL_ID_1, "Media Controls", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel1.setDescription("Notifications for media playback");

//            NotificationChannel notificationChannel2 = new NotificationChannel(CHANNEL_ID_2, "Fallback Media Control", NotificationManager.IMPORTANCE_LOW);
//            notificationChannel2.setDescription("Notifications For media playback");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel1);
//            notificationManager.createNotificationChannel(notificationChannel2);

        }
    }

    public void setMusicDetails(String image, String title, String description, String id) {
        IMAGE_URL = image;
        MUSIC_TITLE = title;
        MUSIC_DESCRIPTION = description;
        MUSIC_ID = id;
    }

    public void showNotification(int playPauseButton) {
        try {

            Intent intent = new Intent(this, MusicOverviewActivity.class);
            intent.putExtra("id", MUSIC_ID);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            Intent prevIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_PREV);
            PendingIntent prevPendingIntent = PendingIntent.getBroadcast(this, 0, prevIntent, PendingIntent.FLAG_IMMUTABLE);

            Intent playIntent = new Intent(this, NotificationReceiver.class).setAction(ApplicationClass.ACTION_PLAY);
            PendingIntent playPendingIntent = PendingIntent.getBroadcast(this, 0, playIntent, PendingIntent.FLAG_IMMUTABLE);

            Intent nextIntent = new Intent(this, NotificationReceiver.class).setAction(ApplicationClass.ACTION_NEXT);
            PendingIntent nextPendingIntent = PendingIntent.getBroadcast(this, 0, nextIntent, PendingIntent.FLAG_IMMUTABLE);


            Glide.with(this)
                    .asBitmap()
                    .load(IMAGE_URL)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                            Notification notification = new androidx.core.app.NotificationCompat.Builder(ApplicationClass.this, CHANNEL_ID_1)
                                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                                    .setLargeIcon(resource)
                                    .setContentTitle(MUSIC_TITLE)
                                    .setOngoing(true)
                                    .setContentText(MUSIC_DESCRIPTION)
                                    .setStyle(new NotificationCompat.MediaStyle()
                                            .setMediaSession(mediaSession.getSessionToken())
                                            .setShowActionsInCompactView(0, 1, 2))
//                                    .addAction(R.drawable.skip_previous_24px, "prev", prevPendingIntent)
//                                    .addAction(playPauseButton, "play", playPendingIntent)
//                                    .addAction(R.drawable.skip_next_24px, "next", nextPendingIntent)
                                    .addAction(new androidx.core.app.NotificationCompat.Action(R.drawable.skip_previous_24px, "prev", prevPendingIntent))
                                    .addAction(new androidx.core.app.NotificationCompat.Action(playPauseButton, "play", playPendingIntent))
                                    .addAction(new androidx.core.app.NotificationCompat.Action(R.drawable.skip_next_24px, "next", nextPendingIntent))
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
