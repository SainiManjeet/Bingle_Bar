package com.bingle.ameba.bingle_bar.common_functions;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.bingle.ameba.bingle_bar.common_functions.firebase_chat_managers.FirebaseChatManager;

import java.util.List;

/**
 * Created by Er.Manjeet Kaur Saini on 22/5/18.
 */

public class Foreground implements Application.ActivityLifecycleCallbacks {

    private static Foreground instance;


    public static void init(Application app) {
        if (instance == null) {
            instance = new Foreground();
            app.registerActivityLifecycleCallbacks(instance);
        }
    }

    public static Foreground get() {
        return instance;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

        Log.e("onActivityCreated", "onActivityCreated");

        Users users = new Users(activity);
        if (users.getFirebaseChatUserHashId() != null && !users.getFirebaseChatUserHashId().isEmpty()) {
            //Update status "Online" in FCM data base TableName:Users-Live : Application Created First Time Only
            FirebaseChatManager.getInstance().setUserPresenceStatus("Online");
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

        Log.e("onActivityStarted", "onActivityStarted");

        Users users = new Users(activity);
        if (users.getFirebaseChatUserHashId() != null && !users.getFirebaseChatUserHashId().isEmpty()) {
            //Update status "Online" in FCM data base TableName:Users-Live : Application Started(Activated)
            FirebaseChatManager.getInstance().setUserPresenceStatus("Online");
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {

        Log.e("onActivityResumed", "onActivityResumed");

        Users users = new Users(activity);
        if (users.getFirebaseChatUserHashId() != null && !users.getFirebaseChatUserHashId().isEmpty()) {
            //Update status "Online" in FCM data base TableName:Users-Live : Application been Resumed and then Activated
            FirebaseChatManager.getInstance().setUserPresenceStatus("Online");
        }

    }

    @Override
    public void onActivityPaused(Activity activity) {

        Log.e("onActivityPaused", "onActivityPaused");

        Users users = new Users(activity);
        if (users.getFirebaseChatUserHashId() != null && !users.getFirebaseChatUserHashId().isEmpty()) {
            //Update status "Offline" in FCM data base TableName:Users-Live : Application been Paused
            FirebaseChatManager.getInstance().setUserPresenceStatus("Offline");


        }

    }

    @Override
    public void onActivityStopped(Activity activity) {

        Log.e("onActivityStopped", "onActivityStopped");

        Users users = new Users(activity);
        if (users.getFirebaseChatUserHashId() != null && !users.getFirebaseChatUserHashId().isEmpty()) {
            //Update status "Offline" in FCM data base TableName:Users-Live : Application Stopped
           // FirebaseChatManager.getInstance().setUserPresenceStatus("Offline"); //W bf
            if(isAppIsInBackground(activity)) {
                FirebaseChatManager.getInstance().setUserPresenceStatus("Offline");
            }
            else{
                FirebaseChatManager.getInstance().setUserPresenceStatus("Online");
            }
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        Log.e("onActivitySaveInstance", "onActivitySaveInstance");

        Users users = new Users(activity);
        if (users.getFirebaseChatUserHashId() != null && !users.getFirebaseChatUserHashId().isEmpty()) {
            //Update status "Online" in FCM data base TableName:Users-Live : Application Already in Foreground State
            FirebaseChatManager.getInstance().setUserPresenceStatus("Online");
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {

        Log.e("onActivityDestroyed", "onActivityDestroyed");

        Users users = new Users(activity);
        if (users.getFirebaseChatUserHashId() != null && !users.getFirebaseChatUserHashId().isEmpty()) {
            //Update status "Online" in FCM data base TableName:Users-Live : Application been Killed and come back to the foreground
          //  FirebaseChatManager.getInstance().setUserPresenceStatus("Online");

          if(isAppIsInBackground(activity)) {
              FirebaseChatManager.getInstance().setUserPresenceStatus("Offline");
          }
          else{
              FirebaseChatManager.getInstance().setUserPresenceStatus("Online");
          }

        }
    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

}

