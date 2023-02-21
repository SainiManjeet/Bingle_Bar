package com.bingle.ameba.bingle_bar.common_functions;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ameba on 26/3/18.
 */

public class Users {

    Context context;
   // public  static  String loginType="default";

    public void remove(){
        sharedPreferences.edit().clear().commit();
    }

    public String name;
    public String phone;
    public String otp;
    public String welcome_name;
    public String firebaseChatUserHashId;
    public String currentVersionKey;
    public String appUpgradeVersionKey;
    public String gpsLocation;
    public String restaurantId;

    public String getRestaurantId() {
        restaurantId=sharedPreferences.getString("restaurantId","");
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
        sharedPreferences.edit().putString("restaurantId",restaurantId).commit();
    }


    public String getGpsLocation() {
        gpsLocation=sharedPreferences.getString("gpsLocation","");
        return gpsLocation;
    }

    public void setGpsLocation(String gpsLocation) {
        this.gpsLocation = gpsLocation;
        sharedPreferences.edit().putString("gpsLocation",gpsLocation).commit();
    }


    //local
    // Block Status
    private String BlockedStatus;
    private String BlockedBy;

    // no need to store blocked status
    public String getBlockedStatus() {
        BlockedStatus=sharedPreferences.getString("BlockedStatus","");
        return BlockedStatus;
    }

    public void setBlockedStatus(String blockedStatus) {
        this.BlockedStatus = BlockedStatus;
        sharedPreferences.edit().putString("BlockedStatus",BlockedStatus).commit();
    }

    public String getBlockedBy() {
        BlockedBy=sharedPreferences.getString("BlockedBy","");
        return BlockedBy;
    }

    public void setBlockedBy(String blockedBy) {
        this.BlockedBy = BlockedBy;
        sharedPreferences.edit().putString("BlockedBy",BlockedBy).commit();
    }



    //till


    public String getAppUpgradeVersionKey() {
        appUpgradeVersionKey=sharedPreferences.getString("upgradeVersionKey","");
        return appUpgradeVersionKey;
    }

    public void setAppUpgradeVersionKey(String appUpgradeVersionKey) {
        this.appUpgradeVersionKey = appUpgradeVersionKey;
        sharedPreferences.edit().putString("upgradeVersionKey",appUpgradeVersionKey).commit();
    }


    public String getFirebaseChatUserHashId() {

        firebaseChatUserHashId=sharedPreferences.getString("chatPrimaryUserId","");
        return firebaseChatUserHashId;
    }

    public void setFirebaseChatUserHashId(String firebaseChatUserHashId) {
        this.firebaseChatUserHashId = firebaseChatUserHashId;
        sharedPreferences.edit().putString("chatPrimaryUserId",firebaseChatUserHashId).commit();
    }


    public String getCurrentVersionKey() {
        currentVersionKey=sharedPreferences.getString("currentVersionKey","");
        return currentVersionKey;
    }

    public void setCurrentVersionKey(String currentVersionKey) {
        this.currentVersionKey = currentVersionKey;
        sharedPreferences.edit().putString("currentVersionKey",currentVersionKey).commit();
    }

    public String getContactNo() {
        contactNo=sharedPreferences.getString("contactNo","");
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
        sharedPreferences.edit().putString("contactNo",contactNo).commit();
    }

    public String contactNo;

    public String getFirebaseChatID() {
        firebaseChatID=sharedPreferences.getString("getFirebaseChatID","");
        return firebaseChatID;
    }

    public void setFirebaseChatID(String firebaseChatID) {
        this.firebaseChatID = firebaseChatID;
        sharedPreferences.edit().putString("getFirebaseChatID",firebaseChatID).commit();
    }

    public String firebaseChatID;

    public String getUpgradeVersionKey() {
        upgradeVersionKey=sharedPreferences.getString("upgradeVersionKey","");
        return upgradeVersionKey;
    }

    public void setUpgradeVersionKey(String upgradeVersionKey) {
        this.upgradeVersionKey = upgradeVersionKey;
        sharedPreferences.edit().putString("upgradeVersionKey",upgradeVersionKey).commit();

    }

    public String upgradeVersionKey;

    public String getUserId() {

        userId=sharedPreferences.getString("userId","");
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        sharedPreferences.edit().putString("userId",userId).commit();
    }

    public String userId;

    public String getOtp() {
        otp=sharedPreferences.getString("userOtp","");
        return otp;
    }

    public String getWelcome_name() {
        welcome_name=sharedPreferences.getString("welcome_name","");
        return welcome_name;
    }

    public void setWelcome_name(String welcome_name) {
        this.welcome_name = welcome_name;
        sharedPreferences.edit().putString("welcome_name",welcome_name).commit();
    }

    public void setOtp(String otp) {
        this.otp = otp;
        sharedPreferences.edit().putString("userOtp",otp).commit();

    }

    public String getPhone() {
        phone=sharedPreferences.getString("userphone","");
        return phone;
    }

    public void setPhone(String phone_e) {
        this.phone = phone_e;
        sharedPreferences.edit().putString("userphone",phone).commit();
    }
    public String getName() {
        name=sharedPreferences.getString("userdata","");
        return name;
    }

    public void setName(String user_name) {
        name = user_name;
        sharedPreferences.edit().putString("userdata",name).commit();
    }

    SharedPreferences sharedPreferences;

    public  Users(Context context){

        try{
            this.context=context;
        sharedPreferences=context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);}
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
