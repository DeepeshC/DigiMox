package com.digimox.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digimox.R;
import com.digimox.api.DMApiManager;
import com.digimox.app.DMAppConstants;
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
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Deepesh on 10-Dec-15.
 */
public class DMMainHomeActivity extends DMBaseActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    private PopupWindow popupWindowLanguage;
    private PopupWindow popupWindowCurrency;
    private String popUpContentsLanguage[];
    private String popUpContentsCurrency[];
    private PopupMenu popupMenuLanguage;
    private PopupMenu popupMenuCurrency;
    private DMDataBaseHelper dmDataBaseHelper;
    private DMUserDetails dmUserDetails;
    private ArrayList<DMLanguage> dmLanguages;
    private ArrayList<DMCurrency> dmCurrencies;
    private String selectedLanguage;
    private String selectedLanguageId;
    private String selectedCurrency;
    private String selectedCurrencyRate;
    private String currencyId;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout pullLayout;
    private Handler pullHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_page);
        dmDataBaseHelper = new DMDataBaseHelper(this);
        dmDataBaseHelper.openDataBase();
        dmUserDetails = dmDataBaseHelper.getUserData();
        dmLanguages = dmDataBaseHelper.getLanguageList();
        dmCurrencies = dmDataBaseHelper.getCurrencyList();
        initViews();
        showPullAnimation();
        setOnClickListener();
//        showLanguagePopup();
//        showCurrencyPopup();
        createLanguageSpinner();
        createCurrencyPopUp();
    }

    private void showPullAnimation() {
        pullLayout = (RelativeLayout) findViewById(R.id.pull_down_layout);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_up);
        animation.setDuration(1000);
        pullLayout.setVisibility(View.VISIBLE);
        pullLayout.setAnimation(animation);
        pullLayout.animate();
        animation.start();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                pullHandler.postDelayed(pullRunnable, 2000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //Here's a runnable/handler combo
    private Runnable pullRunnable = new Runnable() {
        @Override
        public void run() {
            pullLayout.clearAnimation();
            Animation animation = AnimationUtils.loadAnimation(DMMainHomeActivity.this, R.anim.slide_out_up);
            animation.setDuration(1000);
            pullLayout.setAnimation(animation);
            pullLayout.animate();
            animation.start();
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    pullLayout.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

            });
        }
    };

    private void initViews() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setRefreshing(false);
        if (!TextUtils.isEmpty(dmUserDetails.getUserDetailsLogo())) {
            DMUtils.setImageUrlToView(this, ((ImageView) findViewById(R.id.restaurant_logo)),
                    dmUserDetails.getUserDetailsLogo(), ((ProgressBar) findViewById(R.id.progress_restaurant_logo)));


        }
        if (!TextUtils.isEmpty(DMUtils.getLanguageName(this))) {
            selectedLanguage = DMUtils.getLanguage(this);
            DMUtils.setValueToView(findViewById(R.id.lang_selector), DMUtils.getLanguageName(this));
        } else {
            for (DMLanguage dmLanguage : dmLanguages) {
                if (dmLanguage.getLanguageId().equalsIgnoreCase(dmUserDetails.getUser_default_language())) {
                    selectedLanguage = dmLanguage.getLanguageShort();
                    DMUtils.setValueToView(findViewById(R.id.lang_selector), dmLanguage.getLanguageName());
                }
            }
        }
        if (!TextUtils.isEmpty(DMUtils.getCurrencyCode(this))) {
            selectedCurrency = DMUtils.getCurrencyCode(this);
            selectedCurrencyRate = DMUtils.getExchangeRate(this);
            currencyId = DMUtils.getCurrencyId(this);
            DMUtils.setValueToView(findViewById(R.id.currency_selector), selectedCurrency);
        } else {
            for (DMCurrency dmCurrency : dmCurrencies) {
                if (dmCurrency.getCurrencyId().equalsIgnoreCase(dmUserDetails.getUser_default_currency())) {
                    selectedCurrency = dmCurrency.getCurrencyCode();
                    selectedCurrencyRate = dmCurrency.getCurrencyExchangeRate();
                    currencyId = dmCurrency.getCurrencyId();
                    DMUtils.setValueToView(findViewById(R.id.currency_selector), dmCurrency.getCurrencyCode());
                }
            }
        }
        selectedLanguageId = dmUserDetails.getUser_default_language();
        DMUtils.setValueToView(findViewById(R.id.restaurant_name), dmUserDetails.getUserDetailsRestaurantName());
        DMUtils.setValueToView(findViewById(R.id.title_header), dmUserDetails.getUserDetailsRestaurantName());
        DMUtils.setValueToView(findViewById(R.id.restaurant_description), dmUserDetails.getRestaurantAbout());
// DMUtils.setValueToView(findViewById(R.id.restaurant_description), dmUserDetails.getRestaurantAbout() + "\n" + dmUserDetails.getUserDetailsAddress() + "\n" + dmUserDetails.getUserDetailsCity()
//                + "\n" + dmUserDetails.getUserDetailsState() + "\n" + dmUserDetails.getUserDetailsWebsite() + "\n" + dmUserDetails.getUserDetailsTiming());
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLanguageList();
            }
        });
    }

    private void getLanguageList() {
        if (DMUtils.isOnline()) {
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
        initViews();

    }

    private void setOnClickListener() {
        findViewById(R.id.lang_selector).setOnClickListener(this);
        findViewById(R.id.currency_selector).setOnClickListener(this);
        findViewById(R.id.go_to_next).setOnClickListener(this);
        findViewById(R.id.feed_back_text).setOnClickListener(this);
        findViewById(R.id.feed_back_img).setOnClickListener(this);
        findViewById(R.id.home_logout).setOnClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lang_selector:
//                popupMenuLanguage.show();
                popupWindowLanguage.showAsDropDown(view, -5, 0);
                break;
            case R.id.currency_selector:
//                popupMenuCurrency.show();
                popupWindowCurrency.showAsDropDown(view, -5, 0);
                break;
            case R.id.go_to_next:
                goNext();
                break;
            case R.id.feed_back_text:
                goToFeedback();
                break;
            case R.id.feed_back_img:
                goToFeedback();
                break;
            case R.id.home_logout:
                logout();
                break;
            default:
                break;
        }
    }

    private void logout() {
        dmDataBaseHelper.openDataBase();
        dmDataBaseHelper.deleteUserTable();
        dmDataBaseHelper.deleteCurrencyTable();
        dmDataBaseHelper.deleteLanguageTable();
        dmDataBaseHelper.deleteTable();
        DMUtils.setUserId(this, "");
        DMUtils.setLanguageId(this, "");
        Intent intent = new Intent(this, DMLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void goToFeedback() {
        DMUtils.setLanguageId(this, selectedLanguageId);
        DMUtils.setCurrencyCode(this, selectedCurrency);
        Intent homeIntent = new Intent(this, DMHomeActivity.class);
        homeIntent.putExtra("FEEDBACK", true);
        startActivity(homeIntent);
    }

    private void goNext() {
        if (null != DMUtils.getValueFromView(findViewById(R.id.lang_selector))) {
            if (null != DMUtils.getValueFromView(findViewById(R.id.currency_selector))) {
                DMUtils.setLanguageId(this, selectedLanguageId);
                DMUtils.setCurrencyId(this, currencyId);
                DMUtils.setExchangeRate(this, selectedCurrencyRate);
                DMUtils.setCurrencyCode(this, selectedCurrency);
                DMUtils.setLanguageName(this, DMUtils.getValueFromView(findViewById(R.id.lang_selector)));
                setLanguageLocate(this, selectedLanguage);
                startActivityClass(DMHomeActivity.class);
            } else {
                showToast(getResources().getString(R.string.please_select_currency));
            }
        } else {
            showToast(getResources().getString(R.string.please_select_language));
        }
    }

    public static void setLanguageLocate(Context context, String languageToLoad) {
        String localeCode = getLocaleCode(languageToLoad);
        DMUtils.setLanguage(context, localeCode);
        Locale locale = new Locale(localeCode);
        Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
    }

    private static String getLocaleCode(String languageToLoad) {
        String localeCode = "en";
        if (DMAppConstants.ARABIC.equalsIgnoreCase(languageToLoad)) {
            localeCode = "ar";
        } else if (DMAppConstants.FRENCH.equalsIgnoreCase(languageToLoad)) {
            localeCode = "fr";
        } else if (DMAppConstants.SWEDISH.equalsIgnoreCase(languageToLoad)) {
            localeCode = "sv";
        } else if (DMAppConstants.ITALIAN.equalsIgnoreCase(languageToLoad)) {
            localeCode = "it";
        } else if (DMAppConstants.ENGLISH.equalsIgnoreCase(languageToLoad)) {
            localeCode = "en";
        } else if (DMAppConstants.SPANISH.equalsIgnoreCase(languageToLoad)) {
            localeCode = "es";
        }
        return localeCode;
    }

    private void createLanguageSpinner() {
        popupWindowLanguage = popupWindowLanguage();
    }

    private void createCurrencyPopUp() {
        popupWindowCurrency = popupWindowCurrency();
    }

    private void showLanguagePopup() {

        popupMenuLanguage = new PopupMenu(this, findViewById(R.id.lang_selector));
        for (int i = 0; i < dmLanguages.size(); i++) {
            popupMenuLanguage.getMenu().add(i, i, i, dmLanguages.get(i).getLanguageName());
        }
        popupMenuLanguage.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                selectedLanguage = dmLanguages.get(menuItem.getItemId()).getLanguageShort();
                selectedLanguageId = dmLanguages.get(menuItem.getItemId()).getLanguageId();

                DMUtils.setValueToView(findViewById(R.id.lang_selector), dmLanguages.get(menuItem.getItemId()).getLanguageName());
                return false;
            }
        });
    }

    private void showCurrencyPopup() {

        popupMenuCurrency = new PopupMenu(this, findViewById(R.id.currency_selector));
        for (int i = 0; i < dmCurrencies.size(); i++) {
            popupMenuCurrency.getMenu().add(i, i, i, dmCurrencies.get(i).getCurrencyCode());
        }
        popupMenuCurrency.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                selectedCurrency = dmCurrencies.get(menuItem.getItemId()).getCurrencyCode();
                selectedCurrencyRate = dmCurrencies.get(menuItem.getItemId()).getCurrencyExchangeRate();

                DMUtils.setValueToView(findViewById(R.id.currency_selector), dmCurrencies.get(menuItem.getItemId()).getCurrencyCode());
                return false;
            }
        });
    }

    private PopupWindow popupWindowLanguage() {
        final PopupWindow popupWindow = new PopupWindow(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_up_view, null);
        popupWindow.setContentView(view);
        ListView listViewLanguage = (ListView) view.findViewById(R.id.pop_list);
        listViewLanguage.setDivider(new ColorDrawable(getResources().getColor(R.color.green_bg)));
        listViewLanguage.setDividerHeight(1);
        listViewLanguage.setAdapter(new ArrayLanguageAdapter(DMMainHomeActivity.this, dmLanguages));
        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        listViewLanguage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                DMUtils.setValueToView(findViewById(R.id.lang_selector), dmLanguages.get(position).getLanguageName());
                selectedLanguage = dmLanguages.get(position).getLanguageShort();
                selectedLanguageId = dmLanguages.get(position).getLanguageId();
                popupWindow.dismiss();
            }
        });
        return popupWindow;
    }

    public class ArrayLanguageAdapter extends ArrayAdapter<DMLanguage> {
        public ArrayLanguageAdapter(Context context, ArrayList<DMLanguage> dmLanguages) {
            super(context, 0, dmLanguages);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DMLanguage item = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_list, parent, false);
            }
            TextView tvName = (TextView) convertView.findViewById(R.id.spinner_list);
            tvName.setText(item.getLanguageName());
            return convertView;
        }
    }

    public class ArrayCurrencyAdapter extends ArrayAdapter<DMCurrency> {
        public ArrayCurrencyAdapter(Context context, ArrayList<DMCurrency> dmCurrencies) {
            super(context, 0, dmCurrencies);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DMCurrency item = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_list_currency, parent, false);
            }
            TextView tvName = (TextView) convertView.findViewById(R.id.spinner_list);
            tvName.setText(item.getCurrencyCode());
            return convertView;
        }
    }


    private PopupWindow popupWindowCurrency() {
        final PopupWindow popupWindow = new PopupWindow(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_up_view, null);
        popupWindow.setContentView(view);
        ListView listViewCurrency = (ListView) view.findViewById(R.id.pop_list);
        listViewCurrency.setDivider(new ColorDrawable(getResources().getColor(R.color.green_bg)));
        listViewCurrency.setDividerHeight(1);
        listViewCurrency.setAdapter(new ArrayCurrencyAdapter(DMMainHomeActivity.this, dmCurrencies));
        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        listViewCurrency.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                DMUtils.setValueToView(findViewById(R.id.currency_selector), dmCurrencies.get(position).getCurrencyCode());
                selectedCurrency = dmCurrencies.get(position).getCurrencyCode();
                selectedCurrencyRate = dmCurrencies.get(position).getCurrencyExchangeRate();
                currencyId = dmCurrencies.get(position).getCurrencyId();
                popupWindow.dismiss();
            }
        });
        return popupWindow;
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            DMUtils.setLanguageId(this, "");
            DMUtils.setExchangeRate(this, "");
            DMUtils.setCurrencyCode(this, "");
            DMUtils.setLanguageName(this, "");
            DMUtils.setLanguage(this, "");
            finish();
        }
    }
}
