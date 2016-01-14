package com.digimox.api;

import android.content.Context;
import android.os.Looper;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

/**
 * Created by Deepesh on 29-Nov-15.
 */
public class DMApiManager {
    public static final String DM_REQUEST_BODY_CONTENT_TYPE = "application/json";
    private static final String BASE_URL = "http://ipnets.in/digimox/";
    public static final String METHOD_LOGIN = "login-action.php?";
    public static final String METHOD_MAIN_CATEGORY = "categoryDetails.php?";
    public static final String METHOD_SUB_CATEGORY = "MenuLists.php?";
    public static final String METHOD_DETAIL = "MenuDetails.php?";
    public static final String METHOD_FEEDBACK = "FeedbackLists.php?";
    public static final String METHOD_LANGUAGE = "LanguageList.php?";
    public static final String METHOD_CURRENCY = "CurrencyList.php?";
    public static final String METHOD_FEEDBACK_SEND = "FeedbackPost.php?";
    private Context context;
    // A SyncHttpClient is an AsyncHttpClient
    public static AsyncHttpClient syncHttpClient = new SyncHttpClient();
    public static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    public DMApiManager(Context context) {
        this.context = context;
    }

    /**
     * @return an async client when calling from the main thread, otherwise a sync client.
     */
    private static AsyncHttpClient getClient() {

        // Return the synchronous HTTP client when the thread is not prepared
        if (Looper.myLooper() == null) {
            syncHttpClient.setTimeout(15 * 1000);
            syncHttpClient.setMaxRetriesAndTimeout(1, 15 * 1000);
            return syncHttpClient;
        } else {
            asyncHttpClient.setTimeout(15 * 1000);
            asyncHttpClient.setMaxRetriesAndTimeout(1, 15 * 1000);
            return asyncHttpClient;
        }
    }

    public void get(String url, AsyncHttpResponseHandler responseHandler) {
        String absoluteUrl = getAbsoluteUrl(url);
        getClient().get(context, absoluteUrl, responseHandler);
    }


    public void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getClient().post(getAbsoluteUrl(url), params, responseHandler);
    }

    private String getAbsoluteUrl(String relativeUrl) {
        return DMApiManager.BASE_URL + relativeUrl;
    }
}
