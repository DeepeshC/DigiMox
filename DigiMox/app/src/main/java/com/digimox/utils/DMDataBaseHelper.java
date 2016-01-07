package com.digimox.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.digimox.models.response.DMCurrency;
import com.digimox.models.response.DMLanguage;
import com.digimox.models.response.DMSubCategory;
import com.digimox.models.response.DMUserDetails;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Deepesh on 20-Dec-15.
 */
public class DMDataBaseHelper extends SQLiteOpenHelper {

    // All Static variables
// Database Version
    private static final int DATABASE_VERSION = 1;
    private String TABLE_NAME = "menu_lists";
    private String TABLE_NAME_USER = "user_details";
    private String TABLE_NAME_LANGUAGE = "language_list";
    private String TABLE_NAME_CURRENCY = "currency_list";
    // Database Name
    private static final String DATABASE_NAME = "digimox.sqlite";
    private static final String DB_PATH_SUFFIX = "/databases/";
    static Context ctx;

    public DMDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ctx = context;
    }

    // Getting single contact
    public ArrayList<DMSubCategory> getAddedList() {
        ArrayList<DMSubCategory> dmSubCategories = new ArrayList<DMSubCategory>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM menu_lists", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    DMSubCategory dmSubCategory = new DMSubCategory();
                    dmSubCategory.setItemId(String.valueOf(cursor.getString(0)));
                    dmSubCategory.setItemName(String.valueOf(cursor.getString(1)));
                    dmSubCategory.setItemDesc(String.valueOf(cursor.getString(2)));
                    dmSubCategory.setLanguageId(String.valueOf(cursor.getString(3)));
                    dmSubCategory.setItemImage(String.valueOf(cursor.getString(4)));
                    dmSubCategory.setItemGroupId(String.valueOf(cursor.getString(5)));
                    dmSubCategory.setItemPriceUnit(String.valueOf(cursor.getString(6)));
                    dmSubCategory.setItemPriceUsd(String.valueOf(cursor.getString(7)));
                    dmSubCategories.add(dmSubCategory);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return dmSubCategories;

        }
        return null;
    }

    public int getAddedCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public void insertData(DMSubCategory dmSubCategory) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put("item_id", Integer.parseInt(dmSubCategory.getItemId()));
        initialValues.put("item_name", dmSubCategory.getItemName());
        initialValues.put("item_desc", dmSubCategory.getItemDesc());
        initialValues.put("language_id", dmSubCategory.getLanguageId());
        initialValues.put("item_image", dmSubCategory.getItemImage());
        initialValues.put("item_group_id", dmSubCategory.getItemGroupId());
        initialValues.put("item_price_unit", dmSubCategory.getItemPriceUnit());
        initialValues.put("item_price_usd", dmSubCategory.getItemPriceUsd());
        db.insert(TABLE_NAME, null, initialValues);
        db.close();
    }

    public boolean hasObject(String id) {
        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT * FROM " + TABLE_NAME + " WHERE item_id =?";

        // Add the String you are searching by here.
        // Put it in an array to avoid an unrecognized token error
        Cursor cursor = db.rawQuery(selectString, new String[]{id});

        boolean hasObject = false;
        if (cursor.moveToFirst()) {
            hasObject = true;
            int count = 0;
            while (cursor.moveToNext()) {
                count++;
            }
            //here, count is records found
            Log.d("COUNT", String.format("%d records found", count));


        }

        cursor.close();          // Dont forget to close your cursor
        db.close();              //AND your Database!
        return hasObject;
    }

    private boolean existsColumnInTable() {
        Cursor mCursor = null;
        SQLiteDatabase inDatabase = getWritableDatabase();
        try {
            // Query 1 row
            mCursor = inDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " LIMIT 0", null);

            if (mCursor.getColumnIndex("user_default_currency") != -1)
                return true;
            else
                return false;

        } catch (Exception Exp) {
            // Something went wrong. Missing the database? The table?
            return false;
        } finally {
            if (mCursor != null) mCursor.close();
        }
    }

    public void insertUserData(DMUserDetails dmUserDetails) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put("user_details_id", dmUserDetails.getUserDetailsId());
        initialValues.put("user_fname", dmUserDetails.getUserFname());
        initialValues.put("user_lname", dmUserDetails.getUserLname());
        initialValues.put("user_email", dmUserDetails.getUserEmail());
        initialValues.put("user_name", dmUserDetails.getUserName());
        initialValues.put("user_details_restaurant_name", dmUserDetails.getUserDetailsRestaurantName());
        initialValues.put("user_details_timing", dmUserDetails.getUserDetailsTiming());
        initialValues.put("user_details_address", dmUserDetails.getUserDetailsAddress());
        initialValues.put("user_details_city", dmUserDetails.getUserDetailsCity());
        initialValues.put("user_details_state", dmUserDetails.getUserDetailsState());
        initialValues.put("user_details_website", dmUserDetails.getUserDetailsWebsite());
        initialValues.put("user_details_logo", dmUserDetails.getUserDetailsLogo());
        initialValues.put("restaurant_about", dmUserDetails.getRestaurantAbout());
        initialValues.put("user_details_currency_id", dmUserDetails.getUser_details_currency_id());
        initialValues.put("user_default_currency", dmUserDetails.getUser_default_currency());
        initialValues.put("user_details_language_id", dmUserDetails.getUser_details_language_id());
        initialValues.put("user_default_language", dmUserDetails.getUser_default_language());
        db.insert(TABLE_NAME_USER, null, initialValues);
        db.close();
    }

    public void insertLanguageData(ArrayList<DMLanguage> dmLanguages) {
        SQLiteDatabase db = this.getReadableDatabase();
        for (DMLanguage dmLanguage : dmLanguages) {
            ContentValues initialValues = new ContentValues();
            initialValues.put("language_id", dmLanguage.getLanguageId());
            initialValues.put("language_name", dmLanguage.getLanguageName());
            initialValues.put("language_short", dmLanguage.getLanguageShort());
            initialValues.put("language_status", dmLanguage.getLanguageStatus());
            initialValues.put("language_added_date", dmLanguage.getLanguageAddedDate());
            initialValues.put("language_updated_date", dmLanguage.getLanguageUpdatedDate());
            db.insert(TABLE_NAME_LANGUAGE, null, initialValues);
        }
        db.close();
    }

    public void insertCurrencyData(ArrayList<DMCurrency> dmCurrencyArrayList) {
        SQLiteDatabase db = this.getReadableDatabase();
        for (DMCurrency dmCurrency : dmCurrencyArrayList) {
            ContentValues initialValues = new ContentValues();
            initialValues.put("currency_id", dmCurrency.getCurrencyId());
            initialValues.put("currency_code", dmCurrency.getCurrencyCode());
            initialValues.put("currency_country", dmCurrency.getCurrencyCountry());
            initialValues.put("currency_exchange_rate", dmCurrency.getCurrencyExchangeRate());
            initialValues.put("currency_status", dmCurrency.getCurrencyStatus());
            initialValues.put("currency_added_date", dmCurrency.getCurrencyAddedDate());
            initialValues.put("currency_updated_date", dmCurrency.getCurrencyUpdatedDate());
            db.insert(TABLE_NAME_CURRENCY, null, initialValues);
        }
        db.close();
    }

    // Getting single contact
    public ArrayList<DMLanguage> getLanguageList() {
        ArrayList<DMLanguage> dmLanguages = new ArrayList<DMLanguage>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM language_list", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    DMLanguage dmLanguage = new DMLanguage();
                    dmLanguage.setLanguageId(String.valueOf(cursor.getString(0)));
                    dmLanguage.setLanguageName(String.valueOf(cursor.getString(1)));
                    dmLanguage.setLanguageShort(String.valueOf(cursor.getString(2)));
                    dmLanguage.setLanguageStatus(String.valueOf(cursor.getString(3)));
                    dmLanguage.setLanguageAddedDate(String.valueOf(cursor.getString(4)));
                    dmLanguage.setLanguageUpdatedDate(String.valueOf(cursor.getString(5)));
                    dmLanguages.add(dmLanguage);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return dmLanguages;

        }
        return null;
    }

    // Getting single contact
    public ArrayList<DMCurrency> getCurrencyList() {
        ArrayList<DMCurrency> dmCurrencies = new ArrayList<DMCurrency>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM currency_list", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    DMCurrency dmCurrency = new DMCurrency();
                    dmCurrency.setCurrencyId(String.valueOf(cursor.getString(0)));
                    dmCurrency.setCurrencyCode(String.valueOf(cursor.getString(1)));
                    dmCurrency.setCurrencyCountry(String.valueOf(cursor.getString(2)));
                    dmCurrency.setCurrencyExchangeRate(String.valueOf(cursor.getString(3)));
                    dmCurrency.setCurrencyStatus(String.valueOf(cursor.getString(4)));
                    dmCurrency.setCurrencyAddedDate(String.valueOf(cursor.getString(5)));
                    dmCurrency.setCurrencyUpdatedDate(String.valueOf(cursor.getString(6)));
                    dmCurrencies.add(dmCurrency);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return dmCurrencies;

        }
        return null;
    }

    // Getting single contact
    public DMUserDetails getUserData() {
        DMUserDetails dmUserDetails = new DMUserDetails();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM user_details", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    dmUserDetails.setUserDetailsId(String.valueOf(cursor.getString(0)));
                    dmUserDetails.setUserFname(String.valueOf(cursor.getString(1)));
                    dmUserDetails.setUserLname(String.valueOf(cursor.getString(2)));
                    dmUserDetails.setUserEmail(String.valueOf(cursor.getString(3)));
                    dmUserDetails.setUserName(String.valueOf(cursor.getString(4)));
                    dmUserDetails.setUserDetailsRestaurantName(String.valueOf(cursor.getString(5)));
                    dmUserDetails.setUserDetailsTiming(String.valueOf(cursor.getString(6)));
                    dmUserDetails.setUserDetailsAddress(String.valueOf(cursor.getString(7)));
                    dmUserDetails.setUserDetailsCity(String.valueOf(cursor.getString(8)));
                    dmUserDetails.setUserDetailsState(String.valueOf(cursor.getString(9)));
                    dmUserDetails.setUserDetailsWebsite(String.valueOf(cursor.getString(10)));
                    dmUserDetails.setUserDetailsLogo(String.valueOf(cursor.getString(11)));
                    dmUserDetails.setRestaurantAbout(String.valueOf(cursor.getString(12)));
                    dmUserDetails.setUser_details_currency_id(String.valueOf(cursor.getString(13)));
                    dmUserDetails.setUser_default_currency(String.valueOf(cursor.getString(14)));
                    dmUserDetails.setUser_default_language(String.valueOf(cursor.getString(15)));
                    dmUserDetails.setUser_details_language_id(String.valueOf(cursor.getString(16)));
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return dmUserDetails;

        }
        return null;
    }

    public void deleteTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
        db.close();
    }

    public void deleteRecord(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String whereClause = "item_id" + "=?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
    }

    public void deleteUserTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from " + TABLE_NAME_USER);
        db.close();
    }

    public void deleteLanguageTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from " + TABLE_NAME_LANGUAGE);
        db.close();
    }

    public void deleteCurrencyTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from " + TABLE_NAME_CURRENCY);
        db.close();
    }

    public void CopyDataBaseFromAsset() throws IOException {

        InputStream myInput = ctx.getAssets().open(DATABASE_NAME);

// Path to the just created empty db
        String outFileName = getDatabasePath();

// if the path doesn't exist first, create it
        File f = new File(ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
        if (!f.exists())
            f.mkdir();

// Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

// transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

// Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    private static String getDatabasePath() {
        return ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX
                + DATABASE_NAME;
    }

    public SQLiteDatabase openDataBase() throws SQLException {
        File dbFile = ctx.getDatabasePath(DATABASE_NAME);

        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset();
                System.out.println("Copying sucess from Assets folder");
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        }

        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// TODO Auto-generated method stub

    }
}