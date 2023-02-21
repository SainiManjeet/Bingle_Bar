package com.bingle.ameba.bingle_bar.common_functions;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bingle.ameba.bingle_bar.common_functions.server_api_manager.ServerAPIManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import pojo.RestaurantDetailsByLatLong;

/**
 * Created by Manjeet Saini on 29/5/18.
 */

public class BackGroundService extends Service implements ServicesResponse {

    String restaurantId="",loginChoiceID="",IMEI;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("onBind", "onBind");
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("onStartCommand", "onStartCommand");

        try {
            final String latitude = intent.getStringExtra("latitude");
            Log.e("latitude Ser", "latitude Ser" + latitude);
            final String longitude = intent.getStringExtra("longitude");
            Log.e("latLon", "latLon" + latitude);

            final RestaurantDetailsByLatLong restaurantDetailsByLatLong = ServerAPIManager.getInstance().restaurantsListData;
            Log.e("for", "for"+restaurantDetailsByLatLong.getData().size());

            restaurantId=intent.getStringExtra("restaurantId");
            loginChoiceID=intent.getStringExtra("loginChoiceID");
            IMEI=intent.getStringExtra("IMEI");


            for (int i = 0; i < restaurantDetailsByLatLong.getData().size(); i++) {



                final Handler handler = new Handler();
                final int finalI = i;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //Calculate Distance
                            calculateDistance(latitude, longitude, restaurantDetailsByLatLong.getData().get(finalI).getGeoLatitude(),restaurantDetailsByLatLong.getData().get(finalI).getGeoLongitude());
                        } catch (Exception e) {
                        }
                        handler.postDelayed(this, 120000);//2 min
                    }
                }, 120000);





                //if (latitude.equals(restaurantDetailsByLatLong.getData().get(i).getGeoLatitude()) && longitude.equals(restaurantDetailsByLatLong.getData().get(i).getGeoLongitude())) {

                   // calculateDistance(latitude, longitude, restaurantDetailsByLatLong.getData().get(i).getGeoLatitude(),restaurantDetailsByLatLong.getData().get(i).getGeoLongitude());//Working
               // }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

       // return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }



    private void calculateDistance(String lat, String longi, String restaurantLatitude, String restaurantLongitude) {
        Log.e("calculateDistance", "calculateDistance");
      // Toast.makeText(getApplicationContext(), "calculateDistance!", Toast.LENGTH_SHORT).show();


        Location selected_location = new Location("locationA");
        selected_location.setLatitude(Double.parseDouble(lat));
        selected_location.setLongitude(Double.parseDouble(longi));

        Location near_locations = new Location("locationB");
        near_locations.setLatitude(Double.parseDouble(restaurantLatitude));
        near_locations.setLongitude(Double.parseDouble(restaurantLongitude));

        int distance = (int) selected_location.distanceTo(near_locations);
        Log.e("distanceM", "distanceM" + distance);
        // return distance;

        if(distance>20){
           // Toast.makeText(getApplicationContext(), "NotMatched!", Toast.LENGTH_SHORT).show();
            userLogoutApiCall();
            return;
        }
        else{
            Log.e("Matched:", "Matched");
          //  Toast.makeText(getApplicationContext(), "Matched!", Toast.LENGTH_SHORT).show();
            userLoginApiCall(restaurantId, loginChoiceID,IMEI);
            return;
        }


        /*if (distance<=20) {
            Log.e("Matched:", "Matched");
            Toast.makeText(getApplicationContext(), "Matched!", Toast.LENGTH_SHORT).show();
            userLoginApiCall(restaurantId, loginChoiceID,IMEI);
            return;
        }*/

        /*else {
            Log.e("NotMatched:", "NotMatched");
           // Toast.makeText(getApplicationContext(), "NotMatched!", Toast.LENGTH_SHORT).show();

            //userLogoutApiCall();

        }*/



    }

    private void userLogoutApiCall() {
        CommonMethods.getInstance().createRetrofitBuilderWithHeader(getApplicationContext());
        try {
            HashMap <String, String> params = new HashMap <>();
            Users users = new Users(getApplicationContext());
            params.put("userID", users.getUserId());
            params.put("deviceNumber_IMEI", Constants.androidId);
            Log.e("params", params.toString());
            CommonMethods.getInstance().callServiceBackground(getApplicationContext(), this, Constants.USER_LOGOUT_SERVICE_ID, params);
        } catch (Exception e) {
            Log.e("in_exception_" + Constants.USER_LOGOUT_SERVICE_ID, "" + e.toString());
        }
    }


    @Override
    public void onResponseSuccess(Bean_CommonResponse response) throws JSONException {

        JSONObject jsonObject = response.getJsonObject();

        Boolean responseStatus = jsonObject.getBoolean("response");
        Log.e("responseStatus", "responseStatus"+responseStatus);

        if (responseStatus) {
            Log.e("True", "True");

        } else {
            Log.e("False", "False");
        }

    }


    private void userLoginApiCall(String RestaurantId, String loginChoiceId, String IMEI) {
        CommonMethods.getInstance().createRetrofitBuilderWithHeader(getApplicationContext());

        try {
            HashMap<String, String> params = new HashMap<>();
            Users users = new Users(getApplicationContext());
            params.put("userID", users.getUserId());
            params.put("RestaurantId", RestaurantId);
            params.put("loginChoiceId", loginChoiceId);
            params.put("deviceNumber_IMEI", IMEI);

            Log.e("params", params.toString());

            CommonMethods.getInstance().callServiceBackground(getApplicationContext(), this, Constants.USER_LOGIN_SERVICE_ID, params);

        } catch (Exception e) {

            CommonMethods.getInstance().dismissProgressDialog();

            Log.e("in_exception_" + Constants.USER_LOGIN_SERVICE_ID, "" + e.toString());
        }
    }

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        Log.e("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent("com.bingle.ameba.bingle_bar.receivers.RestarterLocationBroadcastReceiver");
        sendBroadcast(broadcastIntent);


    }*/

}
