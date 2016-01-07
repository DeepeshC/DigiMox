package com.digimox.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digimox.R;
import com.digimox.adapters.DMMainCategoryPagerAdapter;
import com.digimox.models.response.DMMainCategory;
import com.digimox.utils.DMUtils;

import java.util.ArrayList;

/**
 * Created by Deepesh on 10-Dec-15.
 */
public class DMSubCategoryParentFragment extends DMBaseFragment implements View.OnClickListener {
    private View view;
    private ArrayList<DMMainCategory> dmMainCategories;
    private DMMainCategory dmMainCategory;
    private int position;
    private ViewPager categoryPager;
    private DMMainCategoryPagerAdapter dmMainCategoryPagerAdapter;

    public DMSubCategoryParentFragment() {
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setDmMainCategory(DMMainCategory dmMainCategory) {
        this.dmMainCategory = dmMainCategory;
    }

    public void setDmMainCategories(ArrayList<DMMainCategory> dmMainCategories) {
        this.dmMainCategories = dmMainCategories;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sub_category_parent, container, false);
        initViews();

        return view;
    }

    public void initViews() {
        setOnClickListener();
//        ((DMHomeActivity) getActivity()).showBackButton();
        DMUtils.setValueToView(view.findViewById(R.id.sub_category_header), dmMainCategory.getGroupName());
        categoryPager = (ViewPager) view.findViewById(R.id.main_category_pager);
        if (position == dmMainCategories.size() - 1) {
            view.findViewById(R.id.sub_category_header_next).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.sub_category_header_next_arrow).setVisibility(View.INVISIBLE);
        } else {
            DMUtils.setValueToView(view.findViewById(R.id.sub_category_header_next), dmMainCategories.get(position + 1).getGroupName());
        }
        if (0 == position) {
            view.findViewById(R.id.sub_category_header_previous_arrow).setVisibility(View.INVISIBLE);
        }

        dmMainCategoryPagerAdapter = new DMMainCategoryPagerAdapter(getChildFragmentManager());
        for (int i = 0; i < dmMainCategories.size(); i++) {
            dmMainCategory = dmMainCategories.get(i);
            DMSubCategoryFragment dmSubCategoryFragment = new DMSubCategoryFragment(Integer.parseInt(dmMainCategory.getGroupId()));
            dmSubCategoryFragment.setDmMainCategory(dmMainCategory);
            dmMainCategoryPagerAdapter.addFragment(i, dmSubCategoryFragment);
        }
        categoryPager.setAdapter(dmMainCategoryPagerAdapter);
        categoryPager.setCurrentItem(position);
        categoryPager.setOffscreenPageLimit(dmMainCategories.size());
        viewpagerPageListener();
    }

    private void setOnClickListener() {
        view.findViewById(R.id.sub_category_header_next_arrow).setOnClickListener(this);
        view.findViewById(R.id.sub_category_header_previous_arrow).setOnClickListener(this);
        view.findViewById(R.id.sub_category_header_next).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sub_category_header_next_arrow:
                showNext();
                break;
            case R.id.sub_category_header_next:
                showNext();
                break;
            case R.id.sub_category_header_previous_arrow:
                showPrevious();
                break;

        }
    }

    private void showNext() {
        position = position + 1;
        if (position < dmMainCategories.size()) {
            categoryPager.setCurrentItem(position);
        }
    }

    private void showPrevious() {
        position = position - 1;
        if (position >= 0) {
            categoryPager.setCurrentItem(position);
        }
    }

    private void viewpagerPageListener() {
        categoryPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setPosition(position);
                if (position == 0) {
                    view.findViewById(R.id.sub_category_header_previous_arrow).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.sub_category_header_next_arrow).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.sub_category_header_next).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.sub_category_header).setVisibility(View.VISIBLE);
                    DMUtils.setValueToView(view.findViewById(R.id.sub_category_header_next), dmMainCategories.get(position + 1).getGroupName());
                    DMUtils.setValueToView(view.findViewById(R.id.sub_category_header), dmMainCategories.get(position).getGroupName());
                } else if (position == dmMainCategories.size() - 1) {
                    view.findViewById(R.id.sub_category_header_next).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.sub_category_header_next_arrow).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.sub_category_header_previous_arrow).setVisibility(View.VISIBLE);
                    DMUtils.setValueToView(view.findViewById(R.id.sub_category_header), dmMainCategories.get(position).getGroupName());
                } else {
                    view.findViewById(R.id.sub_category_header_next_arrow).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.sub_category_header_previous_arrow).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.sub_category_header_next).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.sub_category_header).setVisibility(View.VISIBLE);
                    DMUtils.setValueToView(view.findViewById(R.id.sub_category_header_next), dmMainCategories.get(position + 1).getGroupName());
                    DMUtils.setValueToView(view.findViewById(R.id.sub_category_header), dmMainCategories.get(position).getGroupName());
                }
//                if (null != dmMainCategoryPagerAdapter) {
//                    DMSubCategoryFragment dmSubCategoryFragment = (DMSubCategoryFragment) dmMainCategoryPagerAdapter.getFragment(position);
//                    dmSubCategoryFragment.getSubCategory();
//                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
