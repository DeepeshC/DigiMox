package com.digimox.models;

import android.util.Log;

import com.digimox.models.requests.DMAPIRequestObject;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by b2c on 11/14/15.
 */
public class DMAPIObject {
    private String method;
    private DMAPIRequestObject params;


    public DMAPIObject(DMAPIRequestObject object, String methodName) {
        this.method = methodName;
        this.params = object;
    }

    public DMAPIObject(String methodName) {
        this.method = methodName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public DMAPIRequestObject getParams() {
        return params;
    }

    public void setParams(DMAPIRequestObject params) {
        this.params = params;
    }

    public StringEntity getEntity() {
        Gson gson = new Gson();
        String requestJson = gson.toJson(this);
        Log.d("request", requestJson);
        StringEntity requestEntity = null;
        try {
            requestEntity = new StringEntity(requestJson.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        requestEntity.setContentType("application/json");

        return requestEntity;
    }

}
