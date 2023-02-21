package com.bingle.ameba.bingle_bar.common_functions.firebase_chat_managers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bingle.ameba.bingle_bar.R;
import com.bingle.ameba.bingle_bar.activities.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static com.bingle.ameba.bingle_bar.common_functions.Constants.chatWindowActive;

/**
 * Created by ErAshwini on 26/5/18.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private static final int REQUEST_CODE = 1;
    private static int NOTIFICATION_ID = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        Log.e(TAG, "MyFirebaseMessagingService" +"MyFirebaseMessagingService");
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.e(TAG, "From:" + remoteMessage.getFrom());
        Log.e(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());


        //Calling method to generate notification
        Log.e(TAG, "onMessageReceived" +"onMessageReceived"+chatWindowActive.toString());
       if(chatWindowActive.equalsIgnoreCase("true")){
            Log.e(TAG, "chatActive:" + remoteMessage.getFrom());
        }
        else {
            Log.e(TAG, "chatNotActive:" + remoteMessage.getFrom());
         /*  sendNotification(remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody(), remoteMessage.getData());
           chatWindowActive="false";*/

           foregroundNotification(remoteMessage.getNotification().getTitle(),
                   remoteMessage.getNotification().getBody(), remoteMessage.getData());
           chatWindowActive="false";
        }


        /*sendNotification(remoteMessage.getNotification().getTitle(),
                remoteMessage.getNotification().getBody(), remoteMessage.getData());*/
    }

    //This method is only generating push notification
   private void sendNotification(String messageTitle, String messageBody, Map <String, String> row) {
        Log.e(TAG, "sendNotification" +"sendNotification");
        PendingIntent contentIntent = null;
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.bingle_icon))
                .setSmallIcon(R.drawable.bingle_icon)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(contentIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int count = 0;
        notificationManager.notify(count, notificationBuilder.build());
        count++;
    }


    private void foregroundNotification(String messageTitle, String messageBody, Map <String, String> row){
        Log.e(TAG, "sendNotification" +"sendNotification");
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";
        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.bingle_icon)
                .setContentTitle(messageTitle)
                .setContentText(messageBody).setAutoCancel(true).setContentIntent(pendingIntent);;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());
    }

}
