package com.bingle.ameba.bingle_bar.java_classes;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ameba on 14/2/18.
 */

public class ApiClient {
   public static final String BASE_URL1 = "http://paybill.amebaindia.com/webservices/";


   public static final String BASE_URL2 = "http://116.193.162.126:2027/api/restaurant/";
   public static Retrofit retrofit ;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL2)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getClient1() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL1)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
