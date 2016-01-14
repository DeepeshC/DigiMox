package com.digimox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.digimox.R;
import com.digimox.api.DMApiManager;
import com.digimox.models.response.DMCurrency;
import com.digimox.models.response.DMLanguage;
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
 * Created by Deepesh on 10-Dec-15.
 */
public class DMSplashActivity extends DMBaseActivity {
    private DMDataBaseHelper dmDataBaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        checkIsLogin();
    }

    private void checkIsLogin() {
        if (TextUtils.isEmpty(DMUtils.getUserId(this))) {
            Thread timerThread = new Thread() {
                public void run() {
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        startActivityClass(DMLoginActivity.class);
                    }
                }
            };
            timerThread.start();

        } else {
            getLanguageList();

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

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
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
        dmDataBaseHelper = new DMDataBaseHelper(this);
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
}
