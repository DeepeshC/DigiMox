package com.digimox.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digimox.R;
import com.digimox.adapters.DMDetailParentPagerAdapter;
import com.digimox.models.response.DMSubCategory;
import com.digimox.utils.DMUtils;

import java.util.ArrayList;

/**
 * Created by Deepesh on 13-Dec-15.
 */
public class DMMenuDetailParentFragment extends DMBaseFragment implements View.OnClickListener {
    private View view;
    private DMSubCategory dmSubCategory;
    private ArrayList<DMSubCategory> dmSubCategories;
    private int position;
    private ViewPager detailPager;
    private DMDetailParentPagerAdapter dmDetailParentPagerAdapter;

    public DMMenuDetailParentFragment() {

    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setDmSubCategory(DMSubCategory dmSubCategory) {
        this.dmSubCategory = dmSubCategory;
    }

    public void setDmSubCategories(ArrayList<DMSubCategory> dmSubCategories) {
        this.dmSubCategories = dmSubCategories;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.menu_detail, container, false);
        initViews();
        return view;
    }

    public void initViews() {
        setOnClickListener();
//        ((DMHomeActivity) getActivity()).showBackButton();
        DMUtils.setValueToView(view.findViewById(R.id.menu_detail_header), dmSubCategory.getItemName());
        detailPager = (ViewPager) view.findViewById(R.id.menu_detail_pager);
        if (position == dmSubCategories.size() - 1) {
            view.findViewById(R.id.menu_detail_header_next_arrow).setVisibility(View.INVISIBLE);
        }
        if (0 == position) {
            view.findViewById(R.id.menu_detail_header_previous_arrow).setVisibility(View.INVISIBLE);
        }

        dmDetailParentPagerAdapter = new DMDetailParentPagerAdapter(getChildFragmentManager());
        for (int i = 0; i < dmSubCategories.size(); i++) {
            dmSubCategory = dmSubCategories.get(i);
            DMItemDetailFragment dmItemDetailFragment = new DMItemDetailFragment();
            dmItemDetailFragment.setDmSubCategory(dmSubCategory);
            dmDetailParentPagerAdapter.addFragment(dmItemDetailFragment);
        }
        detailPager.setAdapter(dmDetailParentPagerAdapter);
        detailPager.setCurrentItem(position);
        detailPager.setOffscreenPageLimit(dmSubCategories.size());
        viewpagerPageListener();

    }

    private void setOnClickListener() {
        view.findViewById(R.id.menu_detail_header_next_arrow).setOnClickListener(this);
        view.findViewById(R.id.menu_detail_header_previous_arrow).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_detail_header_next_arrow:
                showNext();
                break;
            case R.id.menu_detail_header_previous_arrow:
                showPrevious();
                break;
        }
    }

    private void showNext() {
        position = position + 1;
        if (position < dmSubCategories.size()) {
            detailPager.setCurrentItem(position);
        }
    }

    private void showPrevious() {
        position = position - 1;
        if (position >= 0) {
            detailPager.setCurrentItem(position);
        }
    }

    private void viewpagerPageListener() {
        detailPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setPosition(position);
                if (position == 0) {
                    view.findViewById(R.id.menu_detail_header_previous_arrow).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.menu_detail_header_next_arrow).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.menu_detail_header).setVisibility(View.VISIBLE);
                    DMUtils.setValueToView(view.findViewById(R.id.sub_category_header_next), dmSubCategories.get(position + 1).getItemName());

                } else if (position == dmSubCategories.size() - 1) {
                    view.findViewById(R.id.menu_detail_header_next_arrow).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.menu_detail_header_previous_arrow).setVisibility(View.VISIBLE);
                    DMUtils.setValueToView(view.findViewById(R.id.menu_detail_header), dmSubCategories.get(position).getItemName());
                } else {
                    view.findViewById(R.id.menu_detail_header_next_arrow).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.menu_detail_header_previous_arrow).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.menu_detail_header).setVisibility(View.VISIBLE);
                    DMUtils.setValueToView(view.findViewById(R.id.menu_detail_header), dmSubCategories.get(position).getItemName());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
