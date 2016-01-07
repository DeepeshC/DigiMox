package com.digimox.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.digimox.fragments.DMBaseFragment;
import com.digimox.utils.DMUtils;

/**
 * Created by Deepesh on 26-Nov-15.
 */
public class DMBaseActivity extends AppCompatActivity {
    public Dialog _progressDialogue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public DMBaseFragment getActiveFragment() {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        if (null == fragmentManager) {
            return null;
        }
        if (fragmentManager.getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        return (DMBaseFragment) getSupportFragmentManager().findFragmentByTag(tag);
    }

    public DMBaseFragment getFragment() {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        if (null == fragmentManager) {
            return null;
        }
        if (fragmentManager.getBackStackEntryCount() == 0) {
            return null;
        }
        if (fragmentManager.getBackStackEntryCount() == 1) {
            return null;
        }
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 2).getName();
        return (DMBaseFragment) getSupportFragmentManager().findFragmentByTag(tag);
    }

    protected void startActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));

    }

    protected void startActivityClass(Class<?> cls) {
        finish();
        startActivity(new Intent(this, cls));

    }

    public void showToast(String message) {
        DMUtils.showToastOnTop(this, message);

    }

    public void requestDidStart() {
        /**
         * Starting the progressing indicator
         */
        if (_progressDialogue != null) {
            if (_progressDialogue.isShowing()) {
            }
        } else {
            try {
                _progressDialogue = DMUtils.showProgressDialog(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void requestDidFinish() {
        /**
         * Finishing the progressing indicator
         */

        if (_progressDialogue != null) {
            if (_progressDialogue.isShowing()) {
                try {

                    _progressDialogue.dismiss();
                    _progressDialogue = null;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && isTaskRoot()) {
//            DMUtils.setLanguageId(this, "");
//            DMUtils.setExchangeRate(this, "");
//            DMUtils.setCurrencyCode(this, "");
//            DMUtils.setLanguageName(this, "");
//            DMUtils.setLanguage(this, "");
//            finish();
//            return true;
//        } else {
//            return super.onKeyDown(keyCode, event);
//        }
//    }
}
