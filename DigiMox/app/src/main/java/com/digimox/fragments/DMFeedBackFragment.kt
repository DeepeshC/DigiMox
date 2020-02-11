package com.digimox.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.core.widget.NestedScrollView
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
 * Created by Deepesh on 31-Dec-15.
 */
class DMFeedBackFragment : DMBaseFragment(), View.OnClickListener {
    private var feedbackView: View? = null
    private var feedbackList: ListView? = null
    private var dmFeedbackAdapter: DMFeedbackAdapter? = null
    var rates: ArrayList<String>? = null
    private var dmFeedbacks: ArrayList<DMFeedback>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        feedbackView = inflater.inflate(R.layout.activity_feedback, container, false)
        initViews()
        setOnClickListener()
        return feedbackView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFeedback()
    }

    private fun initViews() {
        feedbackView!!.findViewById<View>(R.id.progress_layout_feedback).visibility = View.VISIBLE
        feedbackList = feedbackView!!.findViewById<View>(R.id.feedback_list) as ListView
    }

    private fun setOnClickListener() {
        feedbackView!!.findViewById<View>(R.id.feedback_send).setOnClickListener(this)
    }

    private fun getFeedback() {

        if (DMUtils.isOnline()) {
            requestDidStart()
            val url = DMApiManager.METHOD_FEEDBACK + "lid=" + DMUtils.getLanguageId(activity!!) + "&uid=" + DMUtils.getUserId(activity!!)
            val dmApiManager = DMApiManager(activity!!)
            dmApiManager[url, object : JsonHttpResponseHandler() {
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
            }]
        } else {
            if (isAdded)
                showToast(resources.getString(R.string.api_error_no_network))
        }
    }

    private fun initFeedback(dmFeedbacks: ArrayList<DMFeedback>) {
        this.dmFeedbacks = dmFeedbacks
        dmFeedbackAdapter = DMFeedbackAdapter(activity!!, dmFeedbacks)
        feedbackList!!.adapter = dmFeedbackAdapter
        feedbackView!!.findViewById<View>(R.id.progress_layout_feedback).visibility = View.GONE
        setListViewHeightBasedOnChildren(feedbackList!!)
        (feedbackView!!.findViewById<View>(R.id.nested_scroll) as NestedScrollView).scrollTo(0, 0)
    }

    private fun setListViewHeightBasedOnChildren(listView: ListView) {
        val listAdapter = listView.adapter
                ?: // pre-condition
                return

        var totalHeight = 0
        for (i in 0 until listAdapter.count) {
            val listItem = listAdapter.getView(i, null, listView)
            listItem.measure(0, 0)
            totalHeight += listItem.measuredHeight
        }

        val params = listView.layoutParams
        params.height = totalHeight + listView.dividerHeight * (listAdapter.count - 1)
        listView.layoutParams = params
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.feedback_send -> sendFeedback()
        }
    }

    private fun sendFeedback() {
        if (null != dmFeedbackAdapter && !dmFeedbackAdapter?.rates.isNullOrEmpty()) {
            rates = dmFeedbackAdapter?.rates
            if (rates!!.contains("0")) {
                showToast("Please answer all the question")
            } else {
                sendFeedbackApi()
            }
        } else {
            showToast("Please answer all the question")
        }
    }

    fun sendFeedbackApi() {
        if (DMUtils.isOnline()) {
            var rate = ""
            var ids = ""
            if (null != rates && rates!!.size > 0) {
                for (i in rates!!.indices) {
                    if (i == rates!!.size - 1) {
                        rate = rate + rates!![i]
                    } else {
                        rate = rate + rates!![i] + "-"
                    }

                }
            }
            if (null != dmFeedbacks && dmFeedbacks!!.size > 0) {
                for (i in dmFeedbacks!!.indices) {
                    if (i == dmFeedbacks!!.size - 1) {
                        ids = ids + dmFeedbacks!![i].feedbackId
                    } else {
                        ids = ids + dmFeedbacks!![i].feedbackId + "-"
                    }

                }
            }
            requestDidStart()
            val url = DMApiManager.METHOD_FEEDBACK_SEND + "fids=" + ids + "&rates=" + rate
            val dmApiManager = DMApiManager(activity!!)
            dmApiManager[url, object : JsonHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONArray?) {
                    super.onSuccess(statusCode, headers, response)
                    requestDidFinish()
                    try {
                        showToast(response!!.get(1).toString())
                        activity!!.finish()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }]
        } else {
            if (isAdded)
                showToast(resources.getString(R.string.api_error_no_network))
        }

    }

}
