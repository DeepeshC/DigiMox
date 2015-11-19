package com.digimox;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.digimox.adapters.DMCategoryAdapter;

/**
 * Created by Deepesh on 17-Nov-15.
 */
public class DMHomeActivity extends FragmentActivity {
    private RecyclerView categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        categoryList = (RecyclerView) findViewById(R.id.category_list);
        categoryList.setHasFixedSize(true);
        categoryList.setLayoutManager(new GridLayoutManager(this, 2));
        DMCategoryAdapter dmCategoryAdapter = new DMCategoryAdapter(this);
        categoryList.setAdapter(dmCategoryAdapter);
    }
}
