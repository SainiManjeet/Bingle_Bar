package com.bingle.ameba.bingle_bar.interfaces;

import com.google.gson.JsonObject;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by ameba on 14/2/18.
 */

public interface ApiInterface {

    //Live URL Do change Before APK Live
    String mainURL="http://116.193.162.126:2027/api/";

    @GET("getAllDetails")
    Observable<JsonObject> Restaurants(@QueryMap HashMap <String, String> map);


    /*get nearby*/
    @GET("getNearByRestaurantsByLatLong")
    Observable<JsonObject> nearbyRestaurants(@QueryMap HashMap <String, String> map);


    @FormUrlEncoded
    @POST(mainURL+"user/getUserIDByUserDetail")
    Observable<JsonObject>userLoggedInWithLoginType(@FieldMap HashMap <String, String> map);

    @FormUrlEncoded
    @POST(mainURL+"UserLogin/userLogin")
    Observable<JsonObject>Login(@FieldMap HashMap <String, String> map);

    @FormUrlEncoded
    @POST(mainURL+"restaurant/addUpdateRestaurantRating")
    Observable<JsonObject>addRating(@FieldMap HashMap <String, String> map);


    @FormUrlEncoded
    @POST(mainURL+"restaurant/getRestaurantImagesByRestaurantId")
    Observable<JsonObject>getRestaurantImagesUsingId(@FieldMap HashMap <String, String> map);


    @FormUrlEncoded
    @POST(mainURL+"user/saveAppFeedbackByUserId")
    Observable<JsonObject>saveAppFeedback(@FieldMap HashMap <String, String> map);

    @FormUrlEncoded
    @POST(mainURL+"user/saveAppRatingByUser")
    Observable<JsonObject>saveAppRating(@FieldMap HashMap <String, String> map);


    @FormUrlEncoded
    @POST(mainURL+"UserLogin/userLogout")
    Observable<JsonObject>Logout(@FieldMap HashMap <String, String> map);


    @FormUrlEncoded
    @POST("https://fcm.googleapis.com/fcm/send")
    Observable<JsonObject>fcmPush(@FieldMap HashMap <String, String> map);

    @FormUrlEncoded
    @POST(mainURL+"user/blockUserByUserId")
    Observable<JsonObject>blockUser(@FieldMap HashMap <String, String> map);








}
