package com.harsh.shah.saavnmp3;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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

    public static String MUSIC_TITLE = "";
    public static String MUSIC_DESCRIPTION = "";
    public static String IMAGE_URL = "";
    public static String MUSIC_ID = "";

    public SharedPreferenceManager sharedPreferenceManager;
    private final String TAG = "ApplicationClass";
    public static int IMAGE_BG_COLOR = Color.argb(255,25,20,20);
    public static int TEXT_ON_IMAGE_COLOR = IMAGE_BG_COLOR ^ 0x00FFFFFF;

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
        if(image!=null) IMAGE_URL = image;
        if(title!=null) MUSIC_TITLE = title;
        if(description!=null) MUSIC_DESCRIPTION = description;
        MUSIC_ID = id;
        Log.i(TAG, "setMusicDetails: " + MUSIC_TITLE + "\t" + MUSIC_ID);
    }

    public void showNotification(int playPauseButton) {
        try {

            Log.i(TAG, "showNotification: " + MUSIC_TITLE + "\t" + MUSIC_ID);

            Intent intent = new Intent(this, MusicOverviewActivity.class);
            intent.putExtra("id", MUSIC_ID);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent contentIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

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
                            IMAGE_BG_COLOR = calculateDominantColor(resource);
                            TEXT_ON_IMAGE_COLOR = invertColor(IMAGE_BG_COLOR);

                            Notification notification = new androidx.core.app.NotificationCompat.Builder(ApplicationClass.this, CHANNEL_ID_1)
                                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                                    .setLargeIcon(resource)
                                    .setContentTitle(MUSIC_TITLE)
                                    .setOngoing(playPauseButton != R.drawable.play_arrow_24px)
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
                                    .setPriority(Notification.PRIORITY_DEFAULT)
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

    private int invertColor(int color) {
        return (color ^ 0x00FFFFFF);
    }
    int calculateDominantColor(Bitmap bitmap) {
        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        int redSum = 0;
        int greenSum = 0;
        int blueSum = 0;

        for (int pixel : pixels) {
            int red = Color.red(pixel);
            int green = Color.green(pixel);
            int blue = Color.blue(pixel);

            redSum += red;
            greenSum += green;
            blueSum += blue;
        }

        int dominantRed = redSum / pixels.length;
        int dominantGreen = greenSum / pixels.length;
        int dominantBlue = blueSum / pixels.length;

        return Color.argb(255, dominantRed, dominantGreen, dominantBlue);
    }

}
