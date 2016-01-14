package com.digimox.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digimox.R;
import com.digimox.activity.DMHomeActivity;
import com.digimox.adapters.DMSelectedItemAdapter;
import com.digimox.app.DMAppConstants;
import com.digimox.models.response.DMSubCategory;
import com.digimox.utils.DMDataBaseHelper;
import com.digimox.utils.DMUtils;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Deepesh on 31-Dec-15.
 */
public class DMSelectedMenuFragment extends DMBaseFragment implements View.OnClickListener {
    private View selectedView;
    private RecyclerView subCategoryList;
    private ArrayList<DMSubCategory> dmSubCategories;
    private DMDataBaseHelper dmDataBaseHelper;
    private DMSelectedItemAdapter dmSelectedItemAdapter;

    public DMSelectedMenuFragment() {
    }

    public void setDmSubCategories(ArrayList<DMSubCategory> dmSubCategories) {
        this.dmSubCategories = dmSubCategories;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        selectedView = inflater.inflate(R.layout.activity_selected_list, container, false);
        initViews();
        setOnClickListener();
//        ((DMHomeActivity) getActivity()).showBackButton();
        if (null != dmSubCategories && dmSubCategories.size() > 0) {
            initList(dmSubCategories);
        }
        return selectedView;
    }


    private void initViews() {
        subCategoryList = (RecyclerView) selectedView.findViewById(R.id.sub_category_list);
        subCategoryList.setHasFixedSize(true);
        checkOrientation();
//        subCategoryList.addItemDecoration(
//                new DMDividerItemDecoration(getActivity(), R.drawable.sublist_divider));
        setMenuCount();
    }

    private void checkOrientation() {
        int orientation = DMUtils.getScreenOrientation(getActivity());
        if (DMAppConstants.PORTRAIT == orientation) {
            subCategoryList.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        } else {
            subCategoryList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation = newConfig.orientation;
        switch (orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                subCategoryList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                subCategoryList.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                break;
        }
    }

    private void initList(ArrayList<DMSubCategory> dmSubCategories) {
        dmSubCategories.removeAll(Collections.singleton(null));
        dmSubCategories.add(null);
        dmSelectedItemAdapter = new DMSelectedItemAdapter(getActivity(), dmSubCategories, this);
        subCategoryList.setAdapter(dmSelectedItemAdapter);
    }

    public void clearData() {
        dmDataBaseHelper = new DMDataBaseHelper(getActivity());
        dmDataBaseHelper.openDataBase();
        dmDataBaseHelper.deleteTable();
        dmDataBaseHelper.close();
        ((DMHomeActivity) getActivity()).setMeuCount("0");
    }

    public void setMenuCount() {
        dmDataBaseHelper = new DMDataBaseHelper(getActivity());
        dmDataBaseHelper.openDataBase();
        dmSubCategories = dmDataBaseHelper.getAddedList();
        int count = dmSubCategories.size();
        ((DMHomeActivity) getActivity()).setMeuCount(String.valueOf(count));
        dmDataBaseHelper.close();
        if (count == 0) {
            selectedView.findViewById(R.id.no_item).setVisibility(View.VISIBLE);
        }
    }

    private void setOnClickListener() {
        selectedView.findViewById(R.id.clear_list).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear_list:
                if (null != dmSelectedItemAdapter) {
                    clearData();
                    dmSelectedItemAdapter.remove();
                    selectedView.findViewById(R.id.no_item).setVisibility(View.VISIBLE);
                }
                break;

        }

    }

    public void showItemDetails(DMSubCategory dmSubCategory) {
        if (null != getActivity()) {
            final android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
            if (null != fm) {
                DMSelectedItemDetailFragment dmSelectedItemDetailFragment = new DMSelectedItemDetailFragment();
                dmSelectedItemDetailFragment.setDmSubCategory(dmSubCategory);
                final android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_left)
                        .add(R.id.fragment_container, dmSelectedItemDetailFragment, "selectedItemDetailsFragment")
                        .addToBackStack("selectedItemDetailsFragment")
                        .commit();
            }
        }
    }
}

