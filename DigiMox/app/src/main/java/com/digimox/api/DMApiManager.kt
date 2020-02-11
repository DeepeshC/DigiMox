package com.digimox.api

import android.content.Context
import android.os.Looper

import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.loopj.android.http.SyncHttpClient

/**
 * Created by Deepesh on 29-Nov-15.
 */
class DMApiManager(private val context: Context) {

    operator fun get(url: String, responseHandler: AsyncHttpResponseHandler) {
        val absoluteUrl = getAbsoluteUrl(url)
        client.get(context, absoluteUrl, responseHandler)
    }


    fun post(url: String, params: RequestParams, responseHandler: AsyncHttpResponseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler)
    }

    private fun getAbsoluteUrl(relativeUrl: String): String {
        return DMApiManager.BASE_URL + relativeUrl
    }

    companion object {
        val DM_REQUEST_BODY_CONTENT_TYPE = "application/json"
        private val BASE_URL = "http://ipnets.in/digimox/"
        val METHOD_LOGIN = "login-action.php?"
        val METHOD_MAIN_CATEGORY = "categoryDetails.php?"
        val METHOD_SUB_CATEGORY = "MenuLists.php?"
        val METHOD_DETAIL = "MenuDetails.php?"
        val METHOD_FEEDBACK = "FeedbackLists.php?"
        val METHOD_LANGUAGE = "LanguageList.php?"
        val METHOD_CURRENCY = "CurrencyList.php?"
        val METHOD_FEEDBACK_SEND = "FeedbackPost.php?"
        // A SyncHttpClient is an AsyncHttpClient
        var syncHttpClient: AsyncHttpClient = SyncHttpClient()
        var asyncHttpClient = AsyncHttpClient()

        /**
         * @return an async client when calling from the main thread, otherwise a sync client.
         */
        private// Return the synchronous HTTP client when the thread is not prepared
        val client: AsyncHttpClient
            get() {
                if (Looper.myLooper() == null) {
                    syncHttpClient.setTimeout(15 * 1000)
                    syncHttpClient.setMaxRetriesAndTimeout(1, 15 * 1000)
                    return syncHttpClient
                } else {
                    asyncHttpClient.setTimeout(15 * 1000)
                    asyncHttpClient.setMaxRetriesAndTimeout(1, 15 * 1000)
                    return asyncHttpClient
                }
            }
    }
}
