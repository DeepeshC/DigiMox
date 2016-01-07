package com.digimox.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digimox.R;
import com.digimox.activity.DMHomeActivity;
import com.digimox.adapters.DMCategorySubAdapter;
import com.digimox.api.DMApiManager;
import com.digimox.app.DMAppConstants;
import com.digimox.models.response.DMMainCategory;
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
public class DMMainCategorySubFragment extends DMBaseFragment {
    private RecyclerView mainCategoryList;
    private View view;
    public DMMainCategory dmMainCategory;
    private SwipeRefreshLayout swipeRefreshLayout;

    public void setDmMainCategory(DMMainCategory dmMainCategory) {
        this.dmMainCategory = dmMainCategory;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_category, container, false);
        initViews();
        return view;
    }

    private void initViews() {
        mainCategoryList = (RecyclerView) view.findViewById(R.id.main_category_list);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mainCategoryList.setHasFixedSize(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMainCategory();
    }

    private void getMainCategory() {
        if (DMUtils.isOnline()) {
            requestDidStart();
            String url = DMApiManager.METHOD_MAIN_CATEGORY + "gid=" + dmMainCategory.getGroupId() + "&lid=" + DMUtils.getLanguageId(getActivity()) + "&uid=" + DMUtils.getUserId(getActivity());
            DMApiManager dmApiManager = new DMApiManager(getActivity());
            dmApiManager.get(url, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    requestDidFinish();
                    try {
                        ArrayList<DMMainCategory> dmMainCategories = new ArrayList<DMMainCategory>();
                        Gson gson = new Gson();
                        for (int i = 0; i < response.length() - 1; i++) {
                            JSONObject responseJSon = response.getJSONObject(i);
                            DMMainCategory dmMainCategory = gson.fromJson(responseJSon.toString(), DMMainCategory.class);
                            dmMainCategories.add(dmMainCategory);
                        }
                        initViews(dmMainCategories);

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

    private void initViews(ArrayList<DMMainCategory> dmMainCategories) {
        mainCategoryList.setAdapter(null);
        checkOrientation();
        DMCategorySubAdapter dmCategorySubAdapter = new DMCategorySubAdapter(getActivity(), this, dmMainCategories);
        mainCategoryList.setAdapter(dmCategorySubAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMainCategory();
            }
        });
        swipeRefreshLayout.setRefreshing(false);
    }

    private void checkOrientation() {
        int orientation = DMUtils.getScreenOrientation(getActivity());
        if (DMAppConstants.PORTRAIT == orientation) {
            mainCategoryList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            mainCategoryList.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        }
    }

    public void clickMainCategory(ArrayList<DMMainCategory> dmMainCategories, DMMainCategory dmMainCategory, int position) {
        if (0 < Integer.parseInt(dmMainCategory.getHasChild())) {
            showMainCategory(dmMainCategory);
        } else {
            showSubCategoryMainFragment(dmMainCategories, dmMainCategory, position);
        }
    }

    private void showMainCategory(DMMainCategory dmMainCategory) {
        final android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
        if (null != fm) {
            DMMainCategorySubFragment dmMainCategorySubFragment = new DMMainCategorySubFragment();
            dmMainCategorySubFragment.setDmMainCategory(dmMainCategory);
            final android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragment_container, dmMainCategorySubFragment, "mainCategorySubFragment")
                    .addToBackStack("mainCategorySubFragment")
                    .commit();
        }
    }

    private void showSubCategoryMainFragment(ArrayList<DMMainCategory> dmMainCategories, DMMainCategory dmMainCategory, int position) {
        final android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
        if (null != fm) {
            DMSubCategoryParentFragment dmSubCategoryParentFragment = new DMSubCategoryParentFragment();
            dmSubCategoryParentFragment.setDmMainCategory(dmMainCategory);
            dmSubCategoryParentFragment.setDmMainCategories(dmMainCategories);
            dmSubCategoryParentFragment.setPosition(position);
            final android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_left)
                    .add(R.id.fragment_container, dmSubCategoryParentFragment, "subCategoryMainFragment")
                    .addToBackStack("subCategoryMainFragment")
                    .commit();
            ((DMHomeActivity) getActivity()).showBackArrow();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation = newConfig.orientation;
        switch (orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                mainCategoryList.setLayoutManager(new GridLayoutManager(getActivity(), 4));
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                mainCategoryList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                break;
        }
    }
}
