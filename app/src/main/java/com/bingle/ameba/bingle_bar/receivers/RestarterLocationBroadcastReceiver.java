package com.bingle.ameba.bingle_bar.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bingle.ameba.bingle_bar.common_functions.BackGroundService;

/**
 * Created by ErAshwini on 6/6/18.
 */

public class RestarterLocationBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(RestarterLocationBroadcastReceiver.class.getSimpleName(), "Service Stops! Oooooooooooooppppssssss!!!!");
        context.startService(new Intent(context, BackGroundService.class));;

    }
}