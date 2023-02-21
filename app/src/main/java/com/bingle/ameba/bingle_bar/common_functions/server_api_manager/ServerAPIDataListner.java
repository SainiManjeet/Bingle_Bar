package com.bingle.ameba.bingle_bar.common_functions.server_api_manager;

import org.json.JSONObject;

/**
 * Created by Master Gaurav Singla on 13/5/18.
 */

public interface ServerAPIDataListner {

    void onCompleted(JSONObject jsonObject);

    void onError(String error);
}
