package com.digimox.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.digimox.R;
import com.digimox.activity.DMHomeActivity;
import com.digimox.adapters.DMSubCategoryAdapter;
import com.digimox.api.DMApiManager;
import com.digimox.app.DMAppConstants;
import com.digimox.models.response.DMCurrency;
import com.digimox.models.response.DMLanguage;
import com.digimox.models.response.DMMainCategory;
import com.digimox.models.response.DMSubCategory;
import com.digimox.utils.DMDataBaseHelper;
import com.digimox.utils.DMDividerItemDecoration;
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
public class DMSubCategoryFragment extends DMBaseFragment {
    private View view;
    private RecyclerView subCategoryList;
    private DMMainCategory dmMainCategory;
    private FragmentActivity fragmentActivity;
    private DMDataBaseHelper dmDataBaseHelper;
    private int mIndex;
    private SwipeRefreshLayout swipeRefreshLayout;


    @SuppressLint({"NewApi", "ValidFragment"})
    public DMSubCategoryFragment(int index) {
        mIndex = index;
    }

    public DMSubCategoryFragment() {
    }

    public void setDmMainCategory(DMMainCategory dmMainCategory) {
        this.dmMainCategory = dmMainCategory;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dish_list, container, false);
        initViews();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getSubCategory();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.fragmentActivity = (FragmentActivity) context;
        dmDataBaseHelper = new DMDataBaseHelper(fragmentActivity);
        dmDataBaseHelper.openDataBase();
    }

    public void getSubCategory() {
        if (DMUtils.isOnline()) {
            view.findViewById(R.id.progress_pager).setVisibility(View.VISIBLE);
            String url = DMApiManager.METHOD_SUB_CATEGORY + "gid=" + dmMainCategory.getGroupId()
                    + "&lid=" + DMUtils.getLanguageId(fragmentActivity) + "&curid=" + DMUtils.getCurrencyId(fragmentActivity);
            DMApiManager dmApiManager = new DMApiManager(fragmentActivity);
            dmApiManager.get(url, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    view.findViewById(R.id.progress_pager).setVisibility(View.GONE);
                    try {
                        ArrayList<DMSubCategory> dmSubCategories = new ArrayList<DMSubCategory>();
                        Gson gson = new Gson();
                        for (int i = 0; i < response.length() - 1; i++) {
                            JSONObject responseJSon = response.getJSONObject(i);
                            DMSubCategory dmSubCategory = gson.fromJson(responseJSon.toString(), DMSubCategory.class);
                            dmSubCategories.add(dmSubCategory);
                        }
                        initList(dmSubCategories);
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

    private void initList(ArrayList<DMSubCategory> dmSubCategories) {
        if (null != fragmentActivity && null != dmSubCategories && dmSubCategories.size() > 0) {
            view.findViewById(R.id.no_item_found).setVisibility(View.GONE);
            subCategoryList.setAdapter(null);
            DMSubCategoryAdapter dmSubCategoryAdapter = new DMSubCategoryAdapter(fragmentActivity, this, dmSubCategories);
            subCategoryList.setAdapter(dmSubCategoryAdapter);
            swipeRefreshLayout.setRefreshing(false);
        } else {
            view.findViewById(R.id.no_item_found).setVisibility(View.VISIBLE);
        }
    }

    private void initViews() {
        subCategoryList = (RecyclerView) view.findViewById(R.id.sub_category_list);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        subCategoryList.setHasFixedSize(true);
        checkOrientation();
        subCategoryList.addItemDecoration(
                new DMDividerItemDecoration(fragmentActivity, R.drawable.sublist_divider));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSubCategory();
            }
        });
    }

//    private void getLanguageList() {
//        if (DMUtils.isOnline()) {
//            requestDidStart();
//            String url = DMApiManager.METHOD_LANGUAGE + "uid=" + DMUtils.getUserId(getActivity());
//            DMApiManager dmApiManager = new DMApiManager(getActivity());
//            dmApiManager.get(url, new JsonHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                    super.onSuccess(statusCode, headers, response);
//
//                    try {
//                        ArrayList<DMLanguage> dmLanguages = new ArrayList<DMLanguage>();
//                        Gson gson = new Gson();
//                        for (int i = 0; i < response.length() - 1; i++) {
//                            JSONObject responseJSon = response.getJSONObject(i);
//                            DMLanguage dmLanguage = gson.fromJson(responseJSon.toString(), DMLanguage.class);
//                            dmLanguages.add(dmLanguage);
//                        }
//                        getCurrencyList(dmLanguages);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        } else {
//            showToast(getResources().getString(R.string.api_error_no_network));
//        }
//    }
//
//    private void getCurrencyList(final ArrayList<DMLanguage> dmLanguages) {
//        String url = DMApiManager.METHOD_CURRENCY + "uid=" + DMUtils.getUserId(getActivity());
//        DMApiManager dmApiManager = new DMApiManager(getActivity());
//        dmApiManager.get(url, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                super.onSuccess(statusCode, headers, response);
//                requestDidFinish();
//                try {
//                    ArrayList<DMCurrency> dmCurrencies = new ArrayList<DMCurrency>();
//                    Gson gson = new Gson();
//                    for (int i = 0; i < response.length() - 1; i++) {
//                        JSONObject responseJSon = response.getJSONObject(i);
//                        DMCurrency dmCurrency = gson.fromJson(responseJSon.toString(), DMCurrency.class);
//                        dmCurrencies.add(dmCurrency);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    private void startHomeView(ArrayList<DMLanguage> dmLanguages, ArrayList<DMCurrency> dmCurrencies) {
        dmDataBaseHelper = new DMDataBaseHelper(getActivity());
        dmDataBaseHelper.openDataBase();
        dmDataBaseHelper.deleteCurrencyTable();
        dmDataBaseHelper.deleteLanguageTable();
        dmDataBaseHelper.insertCurrencyData(dmCurrencies);
        dmDataBaseHelper.insertLanguageData(dmLanguages);
        dmDataBaseHelper.close();
    }

    public void setRefresh() {
        getSubCategory();
    }

    private void checkOrientation() {
        int orientation = DMUtils.getScreenOrientation(fragmentActivity);
        if (DMAppConstants.PORTRAIT == orientation) {
            subCategoryList.setLayoutManager(new GridLayoutManager(fragmentActivity, 1));
        } else {
            subCategoryList.setLayoutManager(new GridLayoutManager(fragmentActivity, 2));
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation = newConfig.orientation;
        switch (orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                subCategoryList.setLayoutManager(new GridLayoutManager(fragmentActivity, 2));
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                subCategoryList.setLayoutManager(new GridLayoutManager(fragmentActivity, 1));
                break;
        }
    }

    public void showItemDetails(DMSubCategory dmSubCategory, ArrayList<DMSubCategory> dmSubCategories, int position) {
        if (null != fragmentActivity) {
            final android.support.v4.app.FragmentManager fm = fragmentActivity.getSupportFragmentManager();
            if (null != fm) {
                DMMenuDetailParentFragment dmMenuDetailParentFragment = new DMMenuDetailParentFragment();
                dmMenuDetailParentFragment.setDmSubCategories(dmSubCategories);
                dmMenuDetailParentFragment.setDmSubCategory(dmSubCategory);
                dmMenuDetailParentFragment.setPosition(position);
                final android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_left)
                        .add(R.id.fragment_container, dmMenuDetailParentFragment, "itemDetailsParentFragment")
                        .addToBackStack("itemDetailsParentFragment")
                        .commit();
            }
        }
    }

    public void addToDb(ImageView imageView, TextView textView, DMSubCategory dmSubCategory) {
//        if (dmDataBaseHelper.hasObject(dmSubCategory.getItemId())) {
//            if (isAdded()) {
//                showToast(getResources().getString(R.string.already_added));
//            }
//        } else {
        try {
            dmDataBaseHelper.insertData(dmSubCategory);
            if (isAdded()) {
                imageView.setImageResource(R.drawable.select_menu_icon_selected);
                textView.setText(getResources().getString(R.string.selected));
                textView.setTextColor(getColor(getActivity(), R.color.green_bg));
            }
            ((DMHomeActivity) fragmentActivity).showListButton();
        } catch (Exception e) {
            e.printStackTrace();
            if (isAdded()) {
                showToast(getResources().getString(R.string.already_added));
            }
        }
//        }
    }

    public static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    public void back() {
        ((DMHomeActivity) fragmentActivity).popStack();
    }
}
