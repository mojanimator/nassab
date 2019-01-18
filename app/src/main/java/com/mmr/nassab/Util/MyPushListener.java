package com.mmr.nassab.Util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.mmr.nassab.MainActivity;
import com.mmr.nassab.R;

import org.json.JSONException;
import org.json.JSONObject;

import co.ronash.pushe.PusheListenerService;

/**
 * Created by Mojtaba Rajabi on 10/01/2019.
 */

public class MyPushListener extends PusheListenerService {
    @Override
    public void onMessageReceived(JSONObject customContent, JSONObject pushMessage) {
        if (customContent == null || customContent.length() == 0)
            return; //json is empty
//        android.util.Log.i(MainActivity.TAG, "Custom json Message: " + customContent.toString()); //print json to logCat
        //Do something with json
        try {
            String s1 = customContent.getString("title");
            String s2 = customContent.getString("message");
            int id = (int) System.currentTimeMillis();
            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, id, intent, 0);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.logo)
                            .setContentTitle(s1)
                            .setContentText(s2)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(id, mBuilder.build());

//            android.util.Log.i(MainActivity.TAG, "Json Message\n title: " + s1 + "\n content: " + s2);
        } catch (JSONException e) {
//            android.util.Log.e(MainActivity.TAG, "Exception in parsing json", e);
        }
    }
}