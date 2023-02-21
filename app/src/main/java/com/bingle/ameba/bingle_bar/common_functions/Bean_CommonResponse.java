package com.bingle.ameba.bingle_bar.common_functions;


import android.app.ProgressDialog;

import org.json.JSONArray;
import org.json.JSONObject;


public class Bean_CommonResponse {
    private int serviceId;
    private String errorMessage;
    private boolean isSuccess;
    private JSONArray josnArray;
    private JSONObject jsonObject;
    private ProgressDialog progress;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public JSONArray getJosnArray() {
        return josnArray;
    }

    public void setJosnArray(JSONArray josnArray) {
        this.josnArray = josnArray;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public ProgressDialog getProgress() {
        return progress;
    }

    public void setProgress(ProgressDialog progress) {
        this.progress = progress;
    }
}