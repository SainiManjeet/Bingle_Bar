package com.bingle.ameba.bingle_bar.common_functions;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bingle.ameba.bingle_bar.R;
import com.bingle.ameba.bingle_bar.activities.MainActivity;
import com.bingle.ameba.bingle_bar.interfaces.ApiInterface;
import com.bingle.ameba.bingle_bar.receivers.ConnectivityReceiver;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CommonMethods extends Application {

    public static final String TAG = CommonMethods.class.getSimpleName();
    private static CommonMethods instance;
    public final int LIGHT = 0, MEDIUM = 1, BOLD = 2;
    public final int REPLACE_FRAGMENT = 0, ADD_FRAGMENT = 1;
    public ProgressDialog mProgressDialog;
    public ProgressDialog progress;
    public Retrofit.Builder builder_without_header;
    public Retrofit.Builder builder_with_header;
    public String url = "http://116.193.162.126:2029/api/restaurant/";
    AlertDialog alert11;
    ArrayList <String> customFonts = new ArrayList <>(Arrays.asList(
            "HelveticaNeueRE.ttf",
            "HelveticaNeuBold.ttf",
            "Verdanab.ttf"
    ));

    public CommonMethods() {
        try {
            createRetrofitBuilderWithoutHeader();
        } catch (Exception e) {

        }

    }

    public static synchronized CommonMethods getInstance() {
        if (instance == null) {
            instance = new CommonMethods();
        }
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("in_common_method", "onCreate");
        instance = this;
        // Initialize Foreground class : To check Activity Life cycle
        Foreground.init(this);
    }

    /*************split name text*******/
    public void splitData(String value) {
        String[] splited = value.split("\\s+");
        MainActivity.m_activity.set_data_fb("splited", splited[0]);
    }

    public Retrofit.Builder createRetrofitBuilderWithHeader(Context context) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        //  .header(Constants.SH_PREF_AUTHENTICATION_TOKEN,token)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        builder_with_header = new Retrofit
                .Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson));
        return builder_with_header;
    }


    //for push notification only
    public Retrofit.Builder createRetrofitBuilderWithHeaderPush(Context context) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("Authorization", "key=AAAAx_ZE8Rk:APA91bGJCtFSvs6nL-_larRg1Fuhjsts_dM5nI7i2OXgKjzDgb_2k8faP3XpIVFddPZkz_VrlEpxNJIBqdXfSojQyt99mx7vSKbrJyawY-jLo0hB7-xyxcYTT3tbOPcz6eL-wITnxmB0")
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        builder_with_header = new Retrofit
                .Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson));
        return builder_with_header;
    }

    /*  Get share preferences*/
    public SharedPreferences getSharedPreferences(Context context, String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    /*Retrofit builder without header*/
    public Retrofit.Builder createRetrofitBuilderWithoutHeader() { // context added

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        builder_without_header = new Retrofit
                .Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson));


        return builder_without_header;

    }

    public ApiInterface getApiInterface(Retrofit.Builder builder) {
        return builder
                .baseUrl(Constants.BASE_URL)
                .build()
                .create(ApiInterface.class);
    }

    public ApiInterface getApiInterface(Context context) {
        Retrofit.Builder builder;
        if (builder_with_header != null) {
            builder = builder_with_header;
        } else {
            builder = createRetrofitBuilderWithHeader(context);
        }
        return builder
                .baseUrl(Constants.BASE_URL)
                .build()
                .create(ApiInterface.class);
    }

    private void getResponseAsJsonObject(final ServicesResponse callBack, final View parentLayout, final int serviceId, Observable <JsonObject> responseBodyCall) {
        try {
            Log.e("in_try_for_service_id ", "" + serviceId);
            final Bean_CommonResponse apiResponse = new Bean_CommonResponse();
            apiResponse.setServiceId(serviceId);
            responseBodyCall.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver <JsonObject>() {
                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject != null) {
                                Log.e("in_success value_" + serviceId, jsonObject.toString());
                                apiResponse.setSuccess(true);
                                JSONObject jSONObject = null;
                                try {
                                    jSONObject = new JSONObject(jsonObject.toString());
                                } catch (JSONException e) {
                                    Log.e("in_success exception_", "" + e.toString());
                                }
                                apiResponse.setJsonObject(jSONObject);
                                try {
                                    callBack.onResponseSuccess(apiResponse);
                                } catch (JSONException e) {
                                    Log.e("in_exceptionCallBacking", e.toString());
                                    e.printStackTrace();
                                }
                            } else {
                                Log.e("in_success value_", "Object_string is null");
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            try {
                                Log.e("in_fail_" + serviceId, e.getMessage());
                                apiResponse.setErrorMessage(e.getMessage());
                                showMessage(apiResponse.getErrorMessage(), parentLayout);//}
                                apiResponse.setSuccess(false);
                                progress.dismiss();
                            } catch (Exception ex) {
                                Log.e("in_fail_in_exceptn" + serviceId, "e is null== " + ex.toString());
                                Log.e("in_fail_in_exceptn" + serviceId, "e is null== " + ex);
                                apiResponse.setErrorMessage("Network connection error");
                                showMessage("Network connection error", parentLayout);
                                apiResponse.setSuccess(false);
                                progress.dismiss();
                            }
                        }

                        @Override
                        public void onComplete() {
                            // progress.dismiss();
                        }
                    });
        } catch (Exception e) {
            Log.e("in_exception_error " + serviceId, "" + e.toString());
        }
    }

    //For BackgroungService Only
    private void getResponseAsJsonObjectBackground(final ServicesResponse callBack, final int serviceId, Observable <JsonObject> responseBodyCall) {
        try {
            Log.e("in_try_for_service_id ", "" + serviceId);
            final Bean_CommonResponse apiResponse = new Bean_CommonResponse();
            apiResponse.setServiceId(serviceId);
            responseBodyCall.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver <JsonObject>() {
                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject != null) {
                                Log.e("in_success value_" + serviceId, jsonObject.toString());
                                apiResponse.setSuccess(true);
                                JSONObject jSONObject = null;
                                try {
                                    jSONObject = new JSONObject(jsonObject.toString());
                                } catch (JSONException e) {
                                    Log.e("in_success exception_", "" + e.toString());
                                }
                                apiResponse.setJsonObject(jSONObject);
                                try {
                                    callBack.onResponseSuccess(apiResponse);
                                } catch (JSONException e) {
                                    Log.e("in_exceptionCallBacking", e.toString());
                                    e.printStackTrace();
                                }
                            } else {
                                Log.e("in_success value_", "Object_string is null");
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            try {
                                Log.e("in_fail_" + serviceId, e.getMessage());
                                apiResponse.setErrorMessage(e.getMessage());
                                //showMessage(apiResponse.getErrorMessage(), parentLayout);//}
                                apiResponse.setSuccess(false);
                                progress.dismiss();
                            } catch (Exception ex) {
                                Log.e("in_fail_in_exceptn" + serviceId, "e is null== " + ex.toString());
                                Log.e("in_fail_in_exceptn" + serviceId, "e is null== " + ex);
                                apiResponse.setErrorMessage("Network connection error");
                                //showMessage("Network connection error", parentLayout);
                                apiResponse.setSuccess(false);
                                progress.dismiss();
                            }
                        }

                        @Override
                        public void onComplete() {
                            // progress.dismiss();
                        }
                    });
        } catch (Exception e) {
            Log.e("in_exception_error " + serviceId, "" + e.toString());
        }
    }


    public void showMessage(String message, View coordinatorLayout) {
        if (getLength(message)) {
            try {
                if (coordinatorLayout == null) {
                    makeToast(message);
                } else {
                    makeSnackBar(message, coordinatorLayout).show();
                }
            } catch (Exception e) {
                Log.e("in_exception_", "showing SnavckBar" + e.toString());
            }
        }
    }

    public boolean getLength(String string) {
        return !TextUtils.isEmpty(string);
    }

    private void makeToast(String message) {
        Toast toast = new Toast(instance);
        toast.setDuration(Toast.LENGTH_LONG);
        TextView tv = new TextView(instance);
        tv.setTextAppearance(instance, android.R.style.TextAppearance_DeviceDefault_Small);
        tv.setTextColor(Color.WHITE);
        tv.setBackgroundResource(R.drawable.app_ic);
        tv.setGravity(Gravity.BOTTOM);
        tv.setText(message);
        toast.setView(tv);
        setFont(LIGHT, tv);
        showToast(toast);
    }

    private Snackbar makeSnackBar(String message, View coordinatorLayout) {
        Snackbar snackbar = null;
        // snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT);
        snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextAppearance(coordinatorLayout.getContext(), android.R.style.TextAppearance_DeviceDefault_Small);
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        setFont(LIGHT, textView);
        final Snackbar finalSnackbar = snackbar;
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalSnackbar.dismiss();
            }
        });
        return snackbar;
    }

    public void showToast(final Toast toast) {
        int toastDurationInMilliSeconds = 200;
        // Set the countdown to display the toast
        CountDownTimer toastCountDown;
        toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 100) {
            public void onTick(long millisUntilFinished) {

                toast.show();
            }

            public void onFinish() {
                toast.cancel();
            }
        };
        // Show the toast and starts the countdown
        toast.show();
        toastCountDown.start();
    }

    public void setFont(int fontType, TextView lightTextView) {
        // Log.e("in_common_", "font");
        //Typeface myFont = Typeface.createFromAsset(getAssets(), "fonts/" + customFonts.get(fontType));
        //lightTextView.setTypeface(myFont);
    }

    public void setFont(int fontType, List <TextView> listTextViewsList) {
        for (int i = 0; i < listTextViewsList.size(); i++) {
            setFont(fontType, listTextViewsList.get(i));
        }
    }


    public void callService(Context context, ServicesResponse callBack, View parentLayout, final int serviceId, HashMap <String, String> params) {
        if (isConnectingToInternet(context)) {
            if (progress != null) {
                if (!progress.isShowing()) {
                    setProgressDialog(context);
                } else {
                    progress.show();
                }
            } else {
                setProgressDialog(context);
            }
            switch (serviceId) {
                case Constants.RESTAURANTS_SERVICE_ID:
                    getResponseAsJsonObject(callBack, parentLayout, serviceId, getApiInterface(builder_without_header).Restaurants(params));
                    break;
                case Constants.NEARBY_RESTAURANTS_SERVICE_ID:
                    getResponseAsJsonObject(callBack, parentLayout, serviceId, getApiInterface(builder_without_header).nearbyRestaurants(params));
                    break;

                /*case Constants.SIGNUP_SERVICE_ID:
                    getResponseAsJsonObject(callBack, parentLayout, serviceId, getApiInterface(builder_without_header).signUp(params));
                    break;
                case Constants.LOGIN_SERVICE_ID:
                    getResponseAsJsonObject(callBack, parentLayout, serviceId, getApiInterface(builder_without_header).Login(params));
                    break;*/


                case Constants.USER_LOGOUT_SERVICE_ID:
                    getResponseAsJsonObject(callBack, parentLayout, serviceId, getApiInterface(builder_without_header).Logout(params));
                    break;
                case Constants.USER_LOGIN_SERVICE_ID:
                    getResponseAsJsonObject(callBack, parentLayout, serviceId, getApiInterface(builder_without_header).Login(params));
                    break;
                case Constants.checkIsUserLoggedInWithLoginType:
                    getResponseAsJsonObject(callBack, parentLayout, serviceId, getApiInterface(builder_without_header).userLoggedInWithLoginType(params));
                    break;
                case Constants.RATING_SERVICE_ID:
                    getResponseAsJsonObject(callBack, parentLayout, serviceId, getApiInterface(builder_without_header).addRating(params));
                    break;
                case Constants.getRestaurantImagesUsingRestaurantID:
                    getResponseAsJsonObject(callBack, parentLayout, serviceId, getApiInterface(builder_without_header).getRestaurantImagesUsingId(params));
                    break;
                case Constants.saveAppFeedbackByUserId:
                    getResponseAsJsonObject(callBack, parentLayout, serviceId, getApiInterface(builder_without_header).saveAppFeedback(params));
                    break;
                case Constants.saveAppRatingByUser:
                    getResponseAsJsonObject(callBack, parentLayout, serviceId, getApiInterface(builder_without_header).saveAppRating(params));
                    break;
                case Constants.FCM_PUSH:
                    getResponseAsJsonObject(callBack, parentLayout, serviceId, getApiInterface(builder_with_header).fcmPush(params));
                    break;

            }
        } else {
            Log.e("hhhh", "***");
            //CommonMethods.getInstance().showMessage(context.getResources().getString(R.string.no_internet_connection), parentLayout);

        }
    }

    // for Background Service Only
    public void callServiceBackground(Context context, ServicesResponse callBack, final int serviceId, HashMap <String, String> params) {
        if (isConnectingToInternet(context)) {
            switch (serviceId) {
                case Constants.USER_LOGOUT_SERVICE_ID:
                    getResponseAsJsonObjectBackground(callBack, serviceId, getApiInterface(builder_without_header).Logout(params));
                    break;
                case Constants.BLOCK_USER:
                    getResponseAsJsonObjectBackground(callBack, serviceId, getApiInterface(builder_without_header).blockUser(params));
                    break;
                case Constants.USER_LOGIN_SERVICE_ID:
                    getResponseAsJsonObjectBackground(callBack, serviceId, getApiInterface(builder_without_header).Login(params));
                    break;
            }
        } else {
            Log.e("hhhh", "***");

        }
    }

    public boolean isConnectingToInternet(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    public boolean isInternetAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }


    @SuppressLint("NewApi")
    public void setProgressDialog(Context ctx) {

        // Use as default Progress Dialog
        setProgressDialog(ctx, "Loading..Please wait", false);


    }

    public void setProgressDialog(Context ctx, String message, Boolean isCancelable) {

        // Customised ProgressDialog
        Log.e("in_progress", "==initialization");

        progress = new ProgressDialog(ctx);
        progress.setMessage(message);

        progress.setCancelable(isCancelable);
        progress.show();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                progress.cancel();
            }
        };
        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(runnable, 3000);


    }

    public void dismissProgressDialog() {
        try {
            progress.dismiss();
        } catch (Exception e) {
            Log.e("in_ExcptnTo_dismiss", e.toString());
        }
    }

    public void hidekeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public void emailVali(TextView textView) {
        String email1 = textView.getText().toString();
        String str = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        Matcher matcherOb = Pattern.compile(str).matcher(email1);
        if (matcherOb.matches()) {

        } else {
            textView.setError("Please enter valid Email");
            textView.setHintTextColor(Color.RED);
            // editTexts.get(2).requestFocus();
            textView.setText("");
            return;
        }
    }

    /* @Override
     protected void attachBaseContext(Context base) {
         super.attachBaseContext(base);
         MultiDex.install(this);
     }*/
    public void setFont(TextView txt) {
        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/Intro_Bold.otf");
        txt.setTypeface(type);
    }

    public void setFontGray(TextView txt) {
        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/Intro_Regular.otf");
        txt.setTypeface(type);
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }


  /*  public void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        CommonMethods.getInstance().showSnack(isConnected);
    }*/

    // Showing the status in Snackbar
    public void showSnack(boolean isConnected, Context ctx) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
            noConnection(ctx);
        }
    }

    public void noConnection(Context ctx) {
        // AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
        // Setting Dialog Title
        alertDialog.setTitle("Info");
        alertDialog.setCancelable(false);

        // Setting Dialog Message
        alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke YES event
                if (ConnectivityReceiver.isConnected()) {
                    dialog.dismiss();
                } else {
                    try {
                        dialog.dismiss();
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory(Intent.CATEGORY_HOME);
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        alertDialog.show();
    }

    public void sharePrefForLoginType() {
        SharedPreferences sharedPreferences_getfb = getApplicationContext().getSharedPreferences("God", Context.MODE_PRIVATE);
    }

}
