package com.digimox.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.digimox.R;
import com.digimox.activity.DMHomeActivity;
import com.digimox.api.DMApiManager;
import com.digimox.models.response.DMMenuDetail;
import com.digimox.models.response.DMSubCategory;
import com.digimox.utils.DMDataBaseHelper;
import com.digimox.utils.DMUtils;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Deepesh on 26-Nov-15.
 */
public class DMSelectedItemDetailFragment extends DMBaseFragment implements View.OnClickListener {
    private View detailView;
    private FragmentActivity fragmentActivity;
    private DMSubCategory dmSubCategory;
    private ArrayList<DMSubCategory> dmSubCategoriesDB;
    private DMDataBaseHelper dmDataBaseHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        detailView = inflater.inflate(R.layout.fragment_selected_item_details, container, false);
        setOnClickListener();
        return detailView;
    }

    public void setDmSubCategory(DMSubCategory dmSubCategory) {
        this.dmSubCategory = dmSubCategory;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.fragmentActivity = (FragmentActivity) context;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DMUtils.setValueToView(view.findViewById(R.id.menu_detail_header), dmSubCategory.getItemName());
        getMenuDetail();
    }

    private void getMenuDetail() {
        if (DMUtils.isOnline()) {
            detailView.findViewById(R.id.progress_pager_menu).setVisibility(View.VISIBLE);
            String url = DMApiManager.METHOD_DETAIL + "itemid=" + dmSubCategory.getItemId()
                    + "&lid=" + DMUtils.getLanguageId(getActivity()) + "&curid=" + DMUtils.getCurrencyId(fragmentActivity);
            DMApiManager dmApiManager = new DMApiManager(fragmentActivity);
            dmApiManager.get(url, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    detailView.findViewById(R.id.progress_pager_menu).setVisibility(View.GONE);
                    try {
                        ArrayList<DMMenuDetail> dmMenuDetails = new ArrayList<DMMenuDetail>();
                        Gson gson = new Gson();
                        for (int i = 0; i < response.length() - 1; i++) {
                            JSONObject responseJSon = response.getJSONObject(i);
                            DMMenuDetail dmMenuDetail = gson.fromJson(responseJSon.toString(), DMMenuDetail.class);
                            dmMenuDetails.add(dmMenuDetail);
                        }
                        initViews(dmMenuDetails);
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

    private void initViews(ArrayList<DMMenuDetail> dmMenuDetails) {
        dmDataBaseHelper = new DMDataBaseHelper(fragmentActivity);
        dmDataBaseHelper.openDataBase();
        dmSubCategoriesDB = dmDataBaseHelper.getAddedList();
        final DMMenuDetail dmMenuDetail = dmMenuDetails.get(0);
        for (DMMenuDetail menuDetail : dmMenuDetails) {
            for (DMSubCategory subCategory : dmSubCategoriesDB) {
                if (subCategory.getItemId().equalsIgnoreCase(menuDetail.getItemId())) {
                    if (isAdded()) {
                        ((ImageView) detailView.findViewById(R.id.add_list_img)).setImageResource(R.drawable.select_menu_icon_selected);
                        ((TextView) detailView.findViewById(R.id.add_list)).setText(getResources().getString(R.string.selected));
                        ((TextView) detailView.findViewById(R.id.add_list)).setTextColor(getColor(getActivity(), R.color.green_bg));
                    }
                }
            }
        }
        DMUtils.setValueToView(detailView.findViewById(R.id.menu_item_name), dmMenuDetail.getItemName());
        DMUtils.setValueToView(detailView.findViewById(R.id.menu_item_description), dmMenuDetail.getItemDesc());
        if (isAdded()) {
            int price = Integer.parseInt(dmSubCategory.getItemPriceUsd());
            DMUtils.setValueToView(detailView.findViewById(R.id.menu_item_aed), DMUtils.getFormattedCurrencyString(DMUtils.getCurrencyCode(getActivity()), price));
        }
        if (!TextUtils.isEmpty(dmMenuDetail.getItemImage())) {
            DMUtils.setImageUrlToView(fragmentActivity, ((ImageView) detailView.findViewById(R.id.menu_item_image)), dmMenuDetail.getItemImage(), ((ProgressBar) detailView.findViewById(R.id.progress_menu_image)));
        }
        detailView.findViewById(R.id.progress_layout).setVisibility(View.GONE);
        detailView.findViewById(R.id.menu_item_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DMUtils.showImageDialog(getActivity(), dmMenuDetail.getItemImage());
            }
        });
    }

    public static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    private void setOnClickListener() {
        detailView.findViewById(R.id.back_list).setOnClickListener(this);
        detailView.findViewById(R.id.add_list_layout).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_list:
                ((DMHomeActivity) getActivity()).popStack();
                break;
            case R.id.add_list_layout:
                addToDb(dmSubCategory);
                break;
        }
    }

    public void addToDb(DMSubCategory dmSubCategory) {
//        if (dmDataBaseHelper.hasObject(dmSubCategory.getItemId())) {
//            if (isAdded()) {
//                showToast(getResources().getString(R.string.already_added));
//            }
//        } else {
        try {
            ((DMHomeActivity) getActivity()).isNeedRefresh = true;
            if (isAdded()) {
                ((ImageView) detailView.findViewById(R.id.add_list_img)).setImageResource(R.drawable.select_menu_icon_selected);
                ((TextView) detailView.findViewById(R.id.add_list)).setText(getResources().getString(R.string.selected));
                ((TextView) detailView.findViewById(R.id.add_list)).setTextColor(getColor(getActivity(), R.color.green_bg));
            }
            dmDataBaseHelper.insertData(dmSubCategory);
            ((DMHomeActivity) fragmentActivity).showListButton();
        } catch (Exception e) {
            e.printStackTrace();

        }
//        }
    }
}
