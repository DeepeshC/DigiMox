package com.digimox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.digimox.R;
import com.digimox.models.response.DMUserDetails;
import com.digimox.utils.DMDataBaseHelper;
import com.digimox.utils.DMUtils;

public class DMSettingsActivity extends DMBaseActivity implements View.OnClickListener {
    private TextView termsConditionText;
    private DMDataBaseHelper dmDataBaseHelper;
    private DMUserDetails dmUserDetails;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        dmDataBaseHelper = new DMDataBaseHelper(this);
        dmDataBaseHelper.openDataBase();
        dmUserDetails = dmDataBaseHelper.getUserData();
        initViews();
        setOnClickListener();
    }

    private void initViews() {
        DMUtils.setValueToView(findViewById(R.id.hotel_text), dmUserDetails.getRestaurantAbout() + "\n" + dmUserDetails.getUserDetailsAddress() + "\n" + dmUserDetails.getUserDetailsCity()
                + "\n" + dmUserDetails.getUserDetailsState() + "\n" + dmUserDetails.getUserDetailsWebsite() + "\n" + dmUserDetails.getUserDetailsTiming());
    }

    private void setOnClickListener() {
        findViewById(R.id.settings_close).setOnClickListener(this);
        findViewById(R.id.settings_feedback).setOnClickListener(this);
        findViewById(R.id.settings_main_category).setOnClickListener(this);
        findViewById(R.id.settings_logout).setOnClickListener(this);
        findViewById(R.id.settings_sub_category).setOnClickListener(this);
        findViewById(R.id.settings_search).setOnClickListener(this);
        findViewById(R.id.settings_home).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.settings_close:
                finish();
                break;
            case R.id.settings_feedback:
                startActivity(DMFeedbackActivity.class);
                break;
            case R.id.settings_main_category:
                goToMainCategory(false);
                break;
            case R.id.settings_logout:
                logout();
                break;
            case R.id.settings_sub_category:
                goToMainCategory(true);
                break;
            case R.id.settings_search:
                break;
            case R.id.settings_home:
                Intent intent = new Intent("finish_activity");
                sendBroadcast(intent);
                startActivityClass(DMMainHomeActivity.class);
                break;
        }

    }

    private void logout() {
        dmDataBaseHelper.deleteUserTable();
        DMUtils.setUserId(DMSettingsActivity.this, "");
        DMUtils.setLanguageId(this, "");
        Intent intent = new Intent(this, DMLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void goToMainCategory(boolean isSub) {

        Intent homeIntent = new Intent(DMSettingsActivity.this, DMHomeActivity.class);
        homeIntent.putExtra("SUB", isSub);
        startActivity(homeIntent);
    }
}
