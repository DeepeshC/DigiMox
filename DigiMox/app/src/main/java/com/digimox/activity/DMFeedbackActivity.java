package com.digimox.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.digimox.R;
import com.digimox.adapters.DMFeedbackAdapter;
import com.digimox.api.DMApiManager;
import com.digimox.models.response.DMFeedback;
import com.digimox.utils.DMUtils;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Deepesh on 21-Dec-15.
 */
public class DMFeedbackActivity extends DMBaseActivity implements View.OnClickListener {
    private ListView feedbackList;
    private ActionBar actionBar;
    private RelativeLayout actionBarLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_feedback);
        initViews();
        setOnClickListener();
        getFeedback();
    }

    private void initViews() {
        findViewById(R.id.progress_layout_feedback).setVisibility(View.VISIBLE);
        feedbackList = (ListView) findViewById(R.id.feedback_list);
    }

    private void setOnClickListener() {
        findViewById(R.id.feedback_send).setOnClickListener(this);
    }

    private void getFeedback() {

        if (DMUtils.isOnline()) {
            requestDidStart();
            String url = DMApiManager.METHOD_FEEDBACK + "lid=" + DMUtils.getLanguageId(this) + "&uid=" + DMUtils.getUserId(this);
            DMApiManager dmApiManager = new DMApiManager(this);
            dmApiManager.get(url, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    requestDidFinish();
                    try {
                        ArrayList<DMFeedback> dmFeedbacks = new ArrayList<DMFeedback>();
                        Gson gson = new Gson();
                        for (int i = 0; i < response.length() - 1; i++) {
                            JSONObject responseJSon = response.getJSONObject(i);
                            DMFeedback dmFeedback = gson.fromJson(responseJSon.toString(), DMFeedback.class);
                            dmFeedbacks.add(dmFeedback);
                        }
                        initFeedback(dmFeedbacks);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            showToast(getResources().getString(R.string.api_error_no_network));
        }
    }

    private void initFeedback(ArrayList<DMFeedback> dmFeedbacks) {
        DMFeedbackAdapter dmFeedbackAdapter = new DMFeedbackAdapter(this, dmFeedbacks);
        feedbackList.setAdapter(dmFeedbackAdapter);
        findViewById(R.id.progress_layout_feedback).setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.feedback_send:
                break;
        }
    }
}
