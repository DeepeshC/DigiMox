package com.digimox;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.digimox.adapters.DMSubCategoryAdapter;

/**
 * Created by Deepesh on 17-Nov-15.
 */
public class DMSubDishActivity extends Activity {
    private RecyclerView subCategoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_dish);
        subCategoryList = (RecyclerView) findViewById(R.id.sub_category_list);
        subCategoryList.setHasFixedSize(true);
        subCategoryList.setLayoutManager(new GridLayoutManager(this, 3));
        DMSubCategoryAdapter dmSubCategoryAdapter = new DMSubCategoryAdapter(this);
        subCategoryList.setAdapter(dmSubCategoryAdapter);
    }
}
