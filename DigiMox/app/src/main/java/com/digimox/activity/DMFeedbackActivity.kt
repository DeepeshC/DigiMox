package com.digimox.activity

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ListView
import com.digimox.R
import com.digimox.adapters.DMFeedbackAdapter
import com.digimox.api.DMApiManager
import com.digimox.models.response.DMFeedback
import com.digimox.utils.DMUtils
import com.google.gson.Gson
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONException
import java.util.*

/**
 * Created by Deepesh on 21-Dec-15.
 */
class DMFeedbackActivity : DMBaseActivity(), View.OnClickListener {
    private var feedbackList: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        setContentView(R.layout.activity_feedback)
        initViews()
        setOnClickListener()
        getFeedback()
    }

    private fun initViews() {
        findViewById<View>(R.id.progress_layout_feedback).visibility = View.VISIBLE
        feedbackList = findViewById<View>(R.id.feedback_list) as ListView
    }

    private fun setOnClickListener() {
        findViewById<View>(R.id.feedback_send).setOnClickListener(this)
    }

    private fun getFeedback() {

        if (DMUtils.isOnline()) {
            requestDidStart()
            val url = DMApiManager.METHOD_FEEDBACK + "lid=" + DMUtils.getLanguageId(this) + "&uid=" + DMUtils.getUserId(this)
            val dmApiManager = DMApiManager(this)
            dmApiManager.get(url, object : JsonHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONArray?) {
                    super.onSuccess(statusCode, headers, response)
                    requestDidFinish()
                    try {
                        val dmFeedbacks = ArrayList<DMFeedback>()
                        val gson = Gson()
                        for (i in 0 until response!!.length() - 1) {
                            val responseJSon = response.getJSONObject(i)
                            val dmFeedback = gson.fromJson(responseJSon.toString(), DMFeedback::class.java)
                            dmFeedbacks.add(dmFeedback)
                        }
                        initFeedback(dmFeedbacks)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            })
        } else {
            showToast(resources.getString(R.string.api_error_no_network))
        }
    }

    private fun initFeedback(dmFeedbacks: ArrayList<DMFeedback>) {
        val dmFeedbackAdapter = DMFeedbackAdapter(this, dmFeedbacks)
        feedbackList!!.adapter = dmFeedbackAdapter
        findViewById<View>(R.id.progress_layout_feedback).visibility = View.GONE
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.feedback_send -> {
            }
        }
    }
}
