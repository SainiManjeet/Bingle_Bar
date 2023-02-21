package com.bingle.ameba.bingle_bar.common_functions;

public class Constants {
    //Live URL Do change Before APK Live
    public static final String BASE_URL = "http://116.193.162.126:2027/api/restaurant/";

    public static final int RESTAURANTS_SERVICE_ID = 0;
    public static final int NEARBY_RESTAURANTS_SERVICE_ID = 1;
    public static final int checkIsUserLoggedInWithLoginType = 2;
    public static final int getRestaurantImagesUsingRestaurantID = 3;
    public static final int RATING_SERVICE_ID = 4;
    public static final int saveAppFeedbackByUserId = 5;
    public static final int saveAppRatingByUser = 6;
    //User Login(Call this API only when Restaurant Location Matched with the current location)
    public static final int USER_LOGIN_SERVICE_ID = 7;
    //User Logout(Call this API only when User is out of range from Restaurant)
    public static final int USER_LOGOUT_SERVICE_ID = 8;
    //FCM Internal API
    public static final int FCM_PUSH = 9;
    //Block user chat
    public static final int BLOCK_USER = 10;
    public static String androidId = "";
    public static String loginType = "default";
    public static String from = "";
    public static String CHANGED = "";
    public static String CURRENT_LOC = "";
    public static String USER_TYPE = "Active";
    public static String chatWindowActive="";
    public static String messageUnread="";
    public static String tabType="0";
    public static String chatWindow="0";



}

