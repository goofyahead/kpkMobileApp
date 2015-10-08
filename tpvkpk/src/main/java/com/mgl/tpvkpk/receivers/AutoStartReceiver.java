package com.mgl.tpvkpk.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mgl.tpvkpk.services.PrinterService;

/**
 * Created by goofyahead on 10/2/15.
 */
public class AutoStartReceiver extends BroadcastReceiver{

    public void onReceive(Context arg0, Intent arg1) {
        Intent intent = new Intent(arg0, PrinterService.class);
        arg0.startService(intent);
        Log.i("Autostart", "started");
    }
}
