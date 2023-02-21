package com.bingle.ameba.bingle_bar.java_classes;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Master Gaurav Singla on 14/4/18.
 */

public class APIClientCountry {
    public static final String BASE_URL = "http://paybill.amebaindia.com/webservices/";


    public static Retrofit retrofit ;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

