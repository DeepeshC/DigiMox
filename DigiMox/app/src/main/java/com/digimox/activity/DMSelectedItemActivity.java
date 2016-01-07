package com.digimox.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.digimox.R;
import com.digimox.adapters.DMSelectedItemAdapter;
import com.digimox.app.DMAppConstants;
import com.digimox.models.response.DMSubCategory;
import com.digimox.utils.DMDataBaseHelper;
import com.digimox.utils.DMDividerItemDecoration;
import com.digimox.utils.DMUtils;

import java.util.ArrayList;

/**
 * Created by Deepesh on 20-Dec-15.
 */
public class DMSelectedItemActivity extends DMBaseActivity implements View.OnClickListener {
    private RecyclerView subCategoryList;
    private ArrayList<DMSubCategory> dmSubCategories;
    private DMDataBaseHelper dmDataBaseHelper;
    private DMSelectedItemAdapter dmSelectedItemAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_list);
        dmSubCategories = (ArrayList<DMSubCategory>) getIntent().getSerializableExtra("SELECTED_ITEM");
        initViews();
        setOnClickListener();
        if (null != dmSubCategories && dmSubCategories.size() > 0) {
            initList(dmSubCategories);
        }

    }

    private void initViews() {
        subCategoryList = (RecyclerView) findViewById(R.id.sub_category_list);
        subCategoryList.setHasFixedSize(true);
        checkOrientation();
        subCategoryList.addItemDecoration(
                new DMDividerItemDecoration(this, R.drawable.sublist_divider));
    }

    private void checkOrientation() {
        int orientation = DMUtils.getScreenOrientation(this);
        if (DMAppConstants.PORTRAIT == orientation) {
            subCategoryList.setLayoutManager(new GridLayoutManager(this, 1));
        } else {
            subCategoryList.setLayoutManager(new GridLayoutManager(this, 2));
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation = newConfig.orientation;
        switch (orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                subCategoryList.setLayoutManager(new GridLayoutManager(this, 2));
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                subCategoryList.setLayoutManager(new GridLayoutManager(this, 1));
                break;
        }
    }

    private void initList(ArrayList<DMSubCategory> dmSubCategories) {
//        dmSelectedItemAdapter = new DMSelectedItemAdapter(this, dmSubCategories);
//        subCategoryList.setAdapter(dmSelectedItemAdapter);
    }

    public void clearData() {
        dmDataBaseHelper = new DMDataBaseHelper(this);
        dmDataBaseHelper.openDataBase();
        dmDataBaseHelper.deleteTable();
    }

    private void setOnClickListener() {
        findViewById(R.id.clear_list).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear_list:
                if (null != dmSelectedItemAdapter) {
                    clearData();
                    dmSelectedItemAdapter.remove();
                    findViewById(R.id.clear_list).setVisibility(View.GONE);
                    findViewById(R.id.no_item).setVisibility(View.VISIBLE);
                }
                break;
        }

    }
}
