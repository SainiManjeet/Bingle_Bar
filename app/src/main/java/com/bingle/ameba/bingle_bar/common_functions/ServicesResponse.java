package com.bingle.ameba.bingle_bar.common_functions;

import org.json.JSONException;
public interface ServicesResponse {
    void onResponseSuccess(Bean_CommonResponse response) throws JSONException;
}