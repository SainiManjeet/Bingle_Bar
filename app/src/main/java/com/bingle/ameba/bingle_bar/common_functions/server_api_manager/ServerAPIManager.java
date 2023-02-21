package com.bingle.ameba.bingle_bar.common_functions.server_api_manager;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bingle.ameba.bingle_bar.common_functions.CommonMethods;
import com.bingle.ameba.bingle_bar.common_functions.Constants;
import com.bingle.ameba.bingle_bar.receivers.ConnectivityReceiver;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import pojo.RestaurantDetailsByLatLong;

/**
 * Created by Master Gaurav Singla on 13/5/18.
 */

public class ServerAPIManager {

    private static final ServerAPIManager ourInstance = new ServerAPIManager();
    private static final String TAG = "ServerAPIManager";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public RestaurantDetailsByLatLong restaurantsListData;

    private ServerAPIManager() {

    }

    public static ServerAPIManager getInstance() {
        return ourInstance;
    }

    public void getAllDetailsByLatLong(String latValue, String lngValue, final Context appContextValue, final ServerAPIDataListner responseDataCallBack) {
        //current time
        String currentDateandTime = sdf.format(new Date()).replace( " " , "T" );
        Log.e(TAG, "currentDateandTime: "+currentDateandTime);



       /* String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        Log.e(TAG, "currentDateandTime1: "+currentDateTimeString );*/

        String url = Constants.BASE_URL + "getAllDetailsByLatLong?latitude=" + latValue + "&longitude=" + lngValue + "&distanceInKm=20&currentDateTime="+currentDateandTime;

        //url.replace(" ","%20");


        Log.e(TAG, "getAllDetailsByLatLong: "+url.replaceAll(" ", "%20") );
        //current lat long value..................


        getDataFromAPI(url, appContextValue, responseDataCallBack);
    }

    public void getDataFromAPI(String urlValue, final Context appContextValue, final ServerAPIDataListner responseDataCallBack) {

        if (ConnectivityReceiver.isConnected()) {

            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlValue,
                    new Response.Listener <String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                JSONObject jsonObject = new JSONObject(response);

                                // Callback: return all data in callback.
                                responseDataCallBack.onCompleted(jsonObject);

                                CommonMethods.getInstance().dismissProgressDialog();

                            } catch (Exception e) {

                                Log.e("in_exception_parse", e.toString());

                                responseDataCallBack.onError(e.toString());

                                CommonMethods.getInstance().dismissProgressDialog();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //displaying the error in toast if occurrs
                            Toast.makeText(appContextValue, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            //creating a request queue
            RequestQueue requestQueue = Volley.newRequestQueue(appContextValue);
            requestQueue.add(stringRequest);

        } else {

            //displaying the error in toast if occurrs
            Toast.makeText(appContextValue, "Internet connection not found.", Toast.LENGTH_SHORT).show();
        }

    }
    //For Firebase
}
