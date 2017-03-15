package com.rrmsense.radiostream.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Talha on 3/12/2017.
 */

public class RadioControllerBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        System.out.println("intent action = " + action);
        long id = intent.getLongExtra("id", -1);

        switch (action){
            case "com.rrmsense.radiostream.ACTION_CONTROLLER":
                context.sendBroadcast(new Intent("CONTROLLER"));
                //Toast.makeText(context,"Controller",Toast.LENGTH_SHORT).show();
                break;
            case "com.rrmsense.radiostream.ACTION_CLOSE":
                //Toast.makeText(context,"Close",Toast.LENGTH_SHORT).show();
                //((MainActivity)context).finish();
                context.sendBroadcast(new Intent("CLOSE"));
                break;
        }

    }

}
