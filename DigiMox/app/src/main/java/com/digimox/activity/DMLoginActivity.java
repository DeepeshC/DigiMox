package com.digimox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.digimox.R;
import com.digimox.api.DMApiManager;
import com.digimox.models.response.DMCurrency;
import com.digimox.models.response.DMLanguage;
import com.digimox.models.response.DMUserDetails;
import com.digimox.utils.DMDataBaseHelper;
import com.digimox.utils.DMUtils;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class DMLoginActivity extends DMBaseActivity implements View.OnClickListener {
    private EditText userName;
    private EditText password;
    private DMDataBaseHelper dmDataBaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dm_login);
        dmDataBaseHelper = new DMDataBaseHelper(this);
        initViews();
        setOnClickListener();
    }

    private void initViews() {
        userName = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.password);
    }

    private void setOnClickListener() {
        findViewById(R.id.language_select).setOnClickListener(this);
        findViewById(R.id.sign_in).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.language_select:
                showLanguagePopUp();
                break;
            case R.id.sign_in:
                signIn();
                break;
            default:
                break;
        }
    }

    private void signIn() {
        if (DMUtils.isOnline()) {
            String userNameString = DMUtils.getValueFromView(userName);
            String passwordString = DMUtils.getValueFromView(password);
            if (null != userNameString) {
                if (null != passwordString) {
                    requestDidStart();
                    String url = DMApiManager.METHOD_LOGIN + "username=" + userNameString + "&password=" + passwordString;
                    DMApiManager dmApiManager = new DMApiManager(this);
                    dmApiManager.get(url, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray responseArray) {
                            super.onSuccess(statusCode, headers, responseArray);
                            try {
                                JSONObject responseJson = responseArray.getJSONObject(0);
                                Gson gson = new Gson();
                                DMUserDetails dmUserDetails = gson.fromJson(responseJson.toString(), DMUserDetails.class);
                                DMUtils.setUserId(DMLoginActivity.this, dmUserDetails.getUserDetailsId());
                                dmDataBaseHelper.openDataBase();
                                dmDataBaseHelper.insertUserData(dmUserDetails);
                                dmDataBaseHelper.close();
                                getLanguageList();
                            } catch (JSONException e) {
                                requestDidFinish();
                                e.printStackTrace();
                                showToast(getResources().getString(R.string.login_error));
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            requestDidFinish();
                            showToast(getResources().getString(R.string.login_error));
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            requestDidFinish();
                            showToast(getResources().getString(R.string.login_error));
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            requestDidFinish();
                            showToast(getResources().getString(R.string.login_error));
                        }
                    });
                } else {
                    showToast(getResources().getString(R.string.please_enter_password));
                }
            } else {
                showToast(getResources().getString(R.string.please_enter_user_name));
            }
        } else {
            showToast(getResources().getString(R.string.api_error_no_network));
        }
    }

    private void getLanguageList() {
        if (DMUtils.isOnline()) {
            requestDidStart();
            String url = DMApiManager.METHOD_LANGUAGE + "uid=" + DMUtils.getUserId(this);
            DMApiManager dmApiManager = new DMApiManager(this);
            dmApiManager.get(url, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);

                    try {
                        ArrayList<DMLanguage> dmLanguages = new ArrayList<DMLanguage>();
                        Gson gson = new Gson();
                        for (int i = 0; i < response.length() - 1; i++) {
                            JSONObject responseJSon = response.getJSONObject(i);
                            DMLanguage dmLanguage = gson.fromJson(responseJSon.toString(), DMLanguage.class);
                            dmLanguages.add(dmLanguage);
                        }
                        getCurrencyList(dmLanguages);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            showToast(getResources().getString(R.string.api_error_no_network));
        }
    }

    private void getCurrencyList(final ArrayList<DMLanguage> dmLanguages) {
        String url = DMApiManager.METHOD_CURRENCY + "uid=" + DMUtils.getUserId(this);
        DMApiManager dmApiManager = new DMApiManager(this);
        dmApiManager.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                requestDidFinish();
                try {
                    ArrayList<DMCurrency> dmCurrencies = new ArrayList<DMCurrency>();
                    Gson gson = new Gson();
                    for (int i = 0; i < response.length() - 1; i++) {
                        JSONObject responseJSon = response.getJSONObject(i);
                        DMCurrency dmCurrency = gson.fromJson(responseJSon.toString(), DMCurrency.class);
                        dmCurrencies.add(dmCurrency);
                    }
                    startHomeView(dmLanguages, dmCurrencies);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void startHomeView(ArrayList<DMLanguage> dmLanguages, ArrayList<DMCurrency> dmCurrencies) {
        dmDataBaseHelper.openDataBase();
        dmDataBaseHelper.deleteCurrencyTable();
        dmDataBaseHelper.deleteLanguageTable();
        dmDataBaseHelper.insertCurrencyData(dmCurrencies);
        dmDataBaseHelper.insertLanguageData(dmLanguages);
        dmDataBaseHelper.close();
        Intent homeIntent = new Intent(this, DMMainHomeActivity.class);
        finish();
        startActivity(homeIntent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_language, menu);
        return true;
    }

    private void showLanguagePopUp() {
        PopupMenu pum = new PopupMenu(this, findViewById(R.id.language_select));
        pum.inflate(R.menu.menu_language);
        pum.show();
        pum.setOnMenuItemClickListener(listener);
    }

    public PopupMenu.OnMenuItemClickListener listener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.english:
                    selectEnglish();
                    break;
                case R.id.arabic:
                    selectArabic();
                    break;
            }

            return true;
        }
    };

    private void setLanguage(String language, String languageName) {
        ((TextView) findViewById(R.id.language_select)).setText(languageName);
    }


    public void selectEnglish() {
        setLanguage("", getResources().getString(R.string.english));
    }

    public void selectArabic() {
        setLanguage("", getResources().getString(R.string.arabic));
    }
}
