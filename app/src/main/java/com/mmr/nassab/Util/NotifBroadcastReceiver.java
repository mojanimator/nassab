package com.mmr.nassab.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mmr.nassab.MainActivity;

/**
 * Created by Mojtaba Rajabi on 10/01/2019.
 */

public class NotifBroadcastReceiver
        extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("com.mmr.nassab.pusheco.NOTIF_CLICKED")) {
            Log.i(MainActivity.TAG, "Broadcast com.mmr.nassab.pusheco.NOTIF_CLICKED received");
            //add your logic here
        } else if (intent.getAction().equals("com.mmr.nassab.NOTIF_DISMISSED")) {
            Log.i(MainActivity.TAG, "Broadcast YOUR_PKG_NAME.NOTIF_DISMISSED received");
            //add your logic here
        } else if (intent.getAction().equals("com.mmr.nassab.pusheco.NOTIF_BTN_CLICKED")) {
            String btnId = intent.getStringExtra("pushe_notif_btn_id");
            Log.i(MainActivity.TAG, "Broadcast YOUR_PKG_NAME.pusheco.NOTIF_BTN_CLICKED received. BtnId = " + btnId);
            //add your logic here
        }
    }


}