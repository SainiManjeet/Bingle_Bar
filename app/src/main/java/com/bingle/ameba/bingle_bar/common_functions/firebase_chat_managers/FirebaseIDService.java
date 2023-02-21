package com.bingle.ameba.bingle_bar.common_functions.firebase_chat_managers;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by ErAshwini on 26/5/18.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";




    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        Log.e(TAG, "Refreshed token: " + "FirebaseInstanceIdService");

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "FirebaseInstanceIdService: " + refreshedToken);

        Toast.makeText(getApplicationContext(), "token="+refreshedToken, Toast.LENGTH_SHORT).show();

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.


    }}