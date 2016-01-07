package com.digimox.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

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
 * Created by Deepesh on 31-Dec-15.
 */
public class DMFeedBackFragment extends DMBaseFragment implements View.OnClickListener {
    private View feedbackView;
    private ListView feedbackList;
    private DMFeedbackAdapter dmFeedbackAdapter;
    public ArrayList<String> rates;
    private ArrayList<DMFeedback> dmFeedbacks;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        feedbackView = inflater.inflate(R.layout.activity_feedback, container, false);
        initViews();
        setOnClickListener();
        return feedbackView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getFeedback();
    }

    private void initViews() {
        feedbackView.findViewById(R.id.progress_layout_feedback).setVisibility(View.VISIBLE);
        feedbackList = (ListView) feedbackView.findViewById(R.id.feedback_list);
    }

    private void setOnClickListener() {
        feedbackView.findViewById(R.id.feedback_send).setOnClickListener(this);
    }

    private void getFeedback() {

        if (DMUtils.isOnline()) {
            requestDidStart();
            String url = DMApiManager.METHOD_FEEDBACK + "lid=" + DMUtils.getLanguageId(getActivity()) + "&uid=" + DMUtils.getUserId(getActivity());
            DMApiManager dmApiManager = new DMApiManager(getActivity());
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
            if (isAdded())
                showToast(getResources().getString(R.string.api_error_no_network));
        }
    }

    private void initFeedback(ArrayList<DMFeedback> dmFeedbacks) {
        this.dmFeedbacks = dmFeedbacks;
        dmFeedbackAdapter = new DMFeedbackAdapter(getActivity(), dmFeedbacks);
        feedbackList.setAdapter(dmFeedbackAdapter);
        feedbackView.findViewById(R.id.progress_layout_feedback).setVisibility(View.GONE);
        setListViewHeightBasedOnChildren(feedbackList);
        ((NestedScrollView) feedbackView.findViewById(R.id.nested_scroll)).scrollTo(0, 0);
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.feedback_send:
                sendFeedback();
                break;
        }
    }

    private void sendFeedback() {
        if (null != dmFeedbackAdapter && null != dmFeedbackAdapter.rates && dmFeedbackAdapter.rates.size() > 0) {
            rates = dmFeedbackAdapter.rates;
            if (rates.contains("0")) {
                showToast("Please answer all the question");
            } else {
                sendFeedbackApi();
            }
        } else {
            showToast("Please answer all the question");
        }
    }

    public void sendFeedbackApi() {
        if (DMUtils.isOnline()) {
            String rate = "";
            String ids = "";
            if (null != rates && rates.size() > 0) {
                for (int i = 0; i < rates.size(); i++) {
                    if (i == rates.size() - 1) {
                        rate = rate + rates.get(i);
                    } else {
                        rate = rate + rates.get(i) + "-";
                    }

                }
            }
            if (null != dmFeedbacks && dmFeedbacks.size() > 0) {
                for (int i = 0; i < dmFeedbacks.size(); i++) {
                    if (i == dmFeedbacks.size() - 1) {
                        ids = ids + dmFeedbacks.get(i).getFeedbackId();
                    } else {
                        ids = ids + dmFeedbacks.get(i).getFeedbackId() + "-";
                    }

                }
            }
            requestDidStart();
            String url = DMApiManager.METHOD_FEEDBACK_SEND + "fids=" + ids + "&rates=" + rate;
            DMApiManager dmApiManager = new DMApiManager(getActivity());
            dmApiManager.get(url, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    requestDidFinish();
                    try {
                        showToast(response.get(1).toString());
                        getActivity().finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            if (isAdded())
                showToast(getResources().getString(R.string.api_error_no_network));
        }

    }

}
