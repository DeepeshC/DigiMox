package com.digimox.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.digimox.R;
import com.digimox.app.DMApplication;
import com.digimox.views.DMTouchImageView;
import com.github.florent37.materialimageloading.MaterialImageLoading;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * Created by Deepesh on 26-Nov-15.
 */
public class DMUtils {
    private static Toast toast;

    public static int getScreenOrientation(Context context) {
        Display getOrient = ((AppCompatActivity) context).getWindowManager().getDefaultDisplay();

        int orientation = getOrient.getOrientation();

        // Sometimes you may get undefined orientation Value is 0
        // simple logic solves the problem compare the screen
        // X,Y Co-ordinates and determine the Orientation in such cases
        if (orientation == Configuration.ORIENTATION_UNDEFINED) {

            Configuration config = context.getResources().getConfiguration();
            orientation = config.orientation;

            if (orientation == Configuration.ORIENTATION_UNDEFINED) {
                //if height and widht of screen are equal then
                // it is square orientation
                if (getOrient.getWidth() == getOrient.getHeight()) {
                    orientation = Configuration.ORIENTATION_SQUARE;
                } else { //if widht is less than height than it is portrait
                    if (getOrient.getWidth() < getOrient.getHeight()) {
                        orientation = Configuration.ORIENTATION_PORTRAIT;
                    } else { // if it is not any of the above it will defineitly be landscape
                        orientation = Configuration.ORIENTATION_LANDSCAPE;
                    }
                }
            }
        }
        return orientation; // return value 1 is portrait and 2 is Landscape Mode
    }

    public static boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) DMApplication.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return (wifiInfo != null && wifiInfo.isConnectedOrConnecting()) || (mobileInfo != null && mobileInfo.isConnectedOrConnecting());
    }

    public static void setImageUrlToImageView(Context context, final ImageView urlToImageView, String imageUrl, int placeHolder) {
        Picasso.with(context)
                .load(imageUrl).fit()
                .placeholder(placeHolder) // optional
                .error(placeHolder)         // optional
                .into(urlToImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        MaterialImageLoading.animate(urlToImageView).setDuration(2000).start();
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    public static void setImageUrlToView(Context context, final ImageView urlToImageView, String imageUrl, int placeHolder, final View progress) {
        Picasso.with(context)
                .load(imageUrl).fit()
                .placeholder(placeHolder) // optional
                .error(placeHolder)         // optional
                .into(urlToImageView, new Callback() {

                    @Override
                    public void onSuccess() {
                        progress.setVisibility(View.GONE);
                        MaterialImageLoading.animate(urlToImageView).setDuration(2000).start();
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    public static void setImageUrlToView(Context context, final ImageView urlToImageView, String imageUrl, final View progress) {
        Picasso.with(context)
                .load(imageUrl).fit()
                .into(urlToImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progress.setVisibility(View.GONE);
                        MaterialImageLoading.animate(urlToImageView).setDuration(2000).start();
                    }

                    @Override
                    public void onError() {
                        Log.d("", "");
                    }
                });
    }

    public static Dialog showProgressDialog(Context context) {

        Dialog dialog = new Dialog(context,
                android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.custom_progress_bar);
        dialog.setCancelable(false);
        dialog.show();

        return dialog;
    }

    public static String getValueFromView(View view) {
        String valueFromView = "";
        if (view instanceof EditText) {
            valueFromView = ((EditText) view).getText().toString().trim();
            return valueFromView.length() > 0 ? valueFromView : null;
        } else if (view instanceof TextView) {
            valueFromView = ((TextView) view).getText().toString().trim();
            return valueFromView.length() > 0 ? valueFromView : null;
        }


        return null;
    }

    public static void displayToast(Context context, String text) {
        if (context == null) {
            return;
        }


        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();


    }

    public static void setValueToView(View view, String text) {

        if (view == null) {
            return;
        }

        if (view instanceof EditText) {

            ((EditText) view).setText(text);

        } else if (view instanceof TextView) {

            ((TextView) view).setText(text);
        }


    }

    public static synchronized void setUserId(Context context, String userId) {

        SharedPreferences preferences = context.getSharedPreferences("USER_ID",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("USER_ID", userId).apply();
    }

    public static String getUserId(Context context) {

        SharedPreferences preferences = context.getSharedPreferences("USER_ID",
                Context.MODE_PRIVATE);

        return preferences.getString("USER_ID", "");
    }

    public static synchronized void setLanguageId(Context context, String languageId) {

        SharedPreferences preferences = context.getSharedPreferences("LANGUAGE_ID",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("LANGUAGE_ID", languageId).apply();
    }

    public static String getLanguageId(Context context) {

        SharedPreferences preferences = context.getSharedPreferences("LANGUAGE_ID",
                Context.MODE_PRIVATE);

        return preferences.getString("LANGUAGE_ID", "");
    }

    public static synchronized void setCurrencyCode(Context context, String currencyId) {

        SharedPreferences preferences = context.getSharedPreferences("CURRENCY_CODE",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("CURRENCY_CODE", currencyId).apply();
    }

    public static String getCurrencyCode(Context context) {

        SharedPreferences preferences = context.getSharedPreferences("CURRENCY_CODE",
                Context.MODE_PRIVATE);

        return preferences.getString("CURRENCY_CODE", "");
    }

    public static synchronized void setLanguage(Context context, String language) {
        SharedPreferences preferences = context.getSharedPreferences("LANGUAGE",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();


        editor.putString("LANGUAGE", language).commit();
    }

    public static String getLanguage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("LANGUAGE",
                Context.MODE_PRIVATE);

        return preferences.getString("LANGUAGE", "");
    }

    public static synchronized void setLanguageName(Context context, String language) {
        SharedPreferences preferences = context.getSharedPreferences("LANGUAGE_NAME",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();


        editor.putString("LANGUAGE_NAME", language).apply();
    }

    public static String getLanguageName(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("LANGUAGE_NAME",
                Context.MODE_PRIVATE);

        return preferences.getString("LANGUAGE_NAME", "");
    }

    public static synchronized void setExchangeRate(Context context, String exchangeRate) {
        SharedPreferences preferences = context.getSharedPreferences("EXCHANGE_RATE",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();


        editor.putString("EXCHANGE_RATE", exchangeRate).commit();
    }

    public static String getExchangeRate(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("EXCHANGE_RATE",
                Context.MODE_PRIVATE);

        return preferences.getString("EXCHANGE_RATE", "");
    }

    public static Point getRealScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();

        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealSize(size);
        } else if (Build.VERSION.SDK_INT >= 14) {
            try {
                size.x = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                size.y = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            } catch (NoSuchMethodException e) {
            }
        }

        return size;
    }

    public static void showToastOnTop(Context context, String message) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_layout,
                (ViewGroup) ((Activity) context)
                        .findViewById(R.id.toast_layout_root));

        TextView text = (TextView) layout.findViewById(R.id.text_custom_toast);
        text.setText(message);
        int location[] = new int[2];
        if (toast == null || toast.getView().getWindowVisibility() != View.VISIBLE) {
            toast = new Toast(context);
            toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0,
                    location[1] - 10);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        }

    }

    public static String getFormattedCurrencyString(String isoCurrencyCode, double amount) {
        // This formats currency values as the user expects to read them (default locale).
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

        // This specifies the actual currency that the value is in, and provides the currency symbol.
        Currency currency = Currency.getInstance(isoCurrencyCode);

        // Our fix is to use the US locale as default for the symbol, unless the currency is USD
        // and the locale is NOT the US, in which case we know it should be US$.
        String symbol;
        if (isoCurrencyCode.equalsIgnoreCase("usd") && !Locale.getDefault().equals(Locale.US)) {
            symbol = "US$";
        } else {
            symbol = currency.getSymbol(Locale.US); // US locale has the best symbol formatting table.
        }

        // We then tell our formatter to use this symbol.
        DecimalFormatSymbols decimalFormatSymbols = ((java.text.DecimalFormat) currencyFormat).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol(symbol);
        ((java.text.DecimalFormat) currencyFormat).setDecimalFormatSymbols(decimalFormatSymbols);

        return currencyFormat.format(amount);
    }

    public static void showImageDialog(Context context, String imageUrl) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_image_dialog);
        dialog.getWindow().getAttributes().width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().getAttributes().height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        DMTouchImageView itemImage = (DMTouchImageView) dialog.findViewById(R.id.dialog_item_image);
        itemImage.setMaxZoom(4f);
        DMUtils.setImageUrlToView(context, itemImage, imageUrl, ((ProgressBar) dialog.findViewById(R.id.dialog_item_progress)));
        dialog.show();
    }
}
