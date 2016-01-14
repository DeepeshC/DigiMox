package com.digimox.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digimox.R;
import com.digimox.api.DMApiManager;
import com.digimox.fragments.DMBaseFragment;
import com.digimox.fragments.DMFeedBackFragment;
import com.digimox.fragments.DMMainCategoryFragment;
import com.digimox.fragments.DMMainCategorySubFragment;
import com.digimox.fragments.DMMenuDetailParentFragment;
import com.digimox.fragments.DMSelectedMenuFragment;
import com.digimox.fragments.DMSubCategoryParentFragment;
import com.digimox.models.response.DMMainCategory;
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
 * Created by Deepesh on 17-Nov-15.
 */
public class DMHomeActivity extends DMBaseActivity {
    private ActionBar actionBar;
    private RelativeLayout actionBarLayout;
    private boolean isSub = false;
    private boolean isFeedback = false;
    private DMDataBaseHelper dmDataBaseHelper;
    private ArrayList<DMSubCategory> dmSubCategories;
    public boolean isNeedRefresh = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        isSub = (boolean) getIntent().getBooleanExtra("SUB", false);
        isFeedback = (boolean) getIntent().getBooleanExtra("FEEDBACK", false);
        initViews();
        if (isFeedback) {
            showFeedback();
        } else if (isSub) {
            getMainCategory();
        } else {
            showMainCategory();
        }
    }


    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBarLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.action_bar, null);
        actionBar.setCustomView(actionBarLayout);
        toolbar.setContentInsetsAbsolute(0, 0);

        actionBarLayout.findViewById(R.id.action_bar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPress();
            }
        });
        actionBarLayout.findViewById(R.id.added_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectedListView();
            }
        });
        findViewById(R.id.actionbar_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityClass(DMMainHomeActivity.class);
            }
        });
        dmDataBaseHelper = new DMDataBaseHelper(this);
        dmDataBaseHelper.openDataBase();
        dmSubCategories = dmDataBaseHelper.getAddedList();
        if (null == dmSubCategories || dmSubCategories.size() == 0) {
            DMUtils.setValueToView(findViewById(R.id.added_list_count), String.valueOf(0));
        } else {
            DMUtils.setValueToView(findViewById(R.id.added_list_count), String.valueOf(dmSubCategories.size()));
        }
//        findViewById(R.id.back_list).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPress();
//            }
//        });
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                Fragment fr = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (fr != null) {
                    if (fr instanceof DMBaseFragment) {
                        if (fr instanceof DMMainCategoryFragment || fr instanceof DMMainCategorySubFragment || fr instanceof DMFeedBackFragment) {
//                            hideBackButton();
                        }
                    }
                }
            }
        });
    }

    public void showListButton() {
        dmSubCategories = dmDataBaseHelper.getAddedList();
        if (null != dmSubCategories && dmSubCategories.size() > 0) {
            ScaleAnimation scale = new ScaleAnimation(0, 2, 0, 2, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
            scale.setDuration(1000);
            scale.setInterpolator(new OvershootInterpolator());
            findViewById(R.id.added_list).startAnimation(scale);
            findViewById(R.id.added_list).clearAnimation();
            scale = new ScaleAnimation(2, 1, 2, 1, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
            scale.setDuration(1000);
            scale.setInterpolator(new OvershootInterpolator());
            findViewById(R.id.added_list).startAnimation(scale);
            findViewById(R.id.added_list_count).startAnimation(scale);
            DMUtils.setValueToView(findViewById(R.id.added_list_count), String.valueOf(dmSubCategories.size()));
        }
    }

    public void moveViewToNextView(View view) {
        TranslateAnimation anim = new TranslateAnimation(0, getRelativeLeft(view), 0, getRelativeTop(findViewById(R.id.added_list)));
        anim.setDuration(1000);
        anim.setFillAfter(true);
        view.startAnimation(anim);
    }

    private int getRelativeLeft(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getLeft();
        else
            return myView.getLeft() + getRelativeLeft((View) myView.getParent());
    }

    private int getRelativeTop(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getTop();
        else
            return myView.getTop() + getRelativeTop((View) myView.getParent());
    }

    private void showSelectedListView() {
        final android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        DMBaseFragment baseFragment = getActiveFragment();

        if (null != fm && !(baseFragment instanceof DMSelectedMenuFragment)) {
            isNeedRefresh = true;
            DMSelectedMenuFragment dmSelectedMenuFragment = new DMSelectedMenuFragment();
            dmSelectedMenuFragment.setDmSubCategories(dmSubCategories);
            final android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_left)
                    .add(R.id.fragment_container, dmSelectedMenuFragment, "selectedMenuFragment")
                    .addToBackStack("selectedMenuFragment")
                    .commit();
        }

    }

    public int getActiveFragmentCount() {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        if (null == fragmentManager) {
            return 0;
        }
        if (fragmentManager.getBackStackEntryCount() == 0) {
            return 0;
        }
        int count = fragmentManager.getBackStackEntryCount();
        return count;
    }

    public void setMeuCount(String count) {
        ((TextView) findViewById(R.id.added_list_count)).setText(count);
    }


    public void hideBackArrow() {
        actionBarLayout.findViewById(R.id.action_bar_back).setVisibility(View.INVISIBLE);
    }

    public void showBackArrow() {
        actionBarLayout.findViewById(R.id.action_bar_back).setVisibility(View.VISIBLE);
    }

    private void showMainCategory() {
        final android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        if (null != fm) {
            final android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragment_container, new DMMainCategoryFragment(), "mainCategoryFragment")
                    .addToBackStack("mainCategoryFragment")
                    .commit();
            hideBackArrow();
        }
    }

    private void showFeedback() {
        final android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        if (null != fm) {
            final android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragment_container, new DMFeedBackFragment(), "feedbackFragment")
                    .addToBackStack("feedbackFragment")
                    .commit();
        }
    }

    private void getMainCategory() {
        if (DMUtils.isOnline()) {
            requestDidStart();
            String url = DMApiManager.METHOD_MAIN_CATEGORY + "gid=0&lid=" + DMUtils.getLanguageId(this);
            DMApiManager dmApiManager = new DMApiManager(this);
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
                        showSubCategoryMainFragment(dmMainCategories, dmMainCategories.get(0), 0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            showToast(getResources().getString(R.string.api_error_no_network));
        }

    }

    public void showSubCategoryMainFragment(ArrayList<DMMainCategory> dmMainCategories, DMMainCategory dmMainCategory, int position) {
        final android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
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
            showBackArrow();
        }
    }

    private void onBackPress() {
        DMBaseFragment baseFragment = getFragment();
        if (null != baseFragment && baseFragment instanceof DMSubCategoryParentFragment && isNeedRefresh) {
            DMSubCategoryParentFragment dmSubCategoryFragment = (DMSubCategoryParentFragment) baseFragment;
            dmSubCategoryFragment.initViews();
            isNeedRefresh = false;
            popStack();
        } else if (null != baseFragment && baseFragment instanceof DMMenuDetailParentFragment) {
            DMMenuDetailParentFragment dmMenuDetailParentFragment = (DMMenuDetailParentFragment) baseFragment;
            dmMenuDetailParentFragment.initViews();
            popStack();
        } else {
            DMBaseFragment dmBaseFragment = getActiveFragment();
            if (getActiveFragmentCount() == 1) {
                if (isTaskRoot()) {
                    DMUtils.setLanguageId(this, "");
                    DMUtils.setExchangeRate(this, "");
                    DMUtils.setCurrencyCode(this, "");
                    DMUtils.setLanguageName(this, "");
                    DMUtils.setLanguage(this, "");
                }
                finish();
            } else if (dmBaseFragment instanceof DMMainCategoryFragment) {
                finish();
            } else if (dmBaseFragment instanceof DMSubCategoryParentFragment && isSub) {
                finish();
            } else if (dmBaseFragment instanceof DMFeedBackFragment && isFeedback) {
                finish();
            } else {
                popStack();
            }
        }

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPress();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcast_reciever, new IntentFilter("finish_activity"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcast_reciever);

    }

    BroadcastReceiver broadcast_reciever = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            String action = intent.getAction();
            if (action.equals("finish_activity")) {
                finish();
            }
        }
    };

    public void popStack() {
        getSupportFragmentManager().popBackStackImmediate();
        if (getFragmentCount() == 1) {
            hideBackArrow();
        }

    }

    @Override
    public void onBackPressed() {
        onBackPress();
    }

    public void showBackButton() {
        findViewById(R.id.back_list).setVisibility(View.VISIBLE);
    }

    public void hideBackButton() {
        findViewById(R.id.back_list).setVisibility(View.GONE);
    }
}
