package com.digimox.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digimox.utils.DMUtils;

/**
 * Created by Deepesh on 26-Nov-15.
 */
public class DMBaseFragment extends Fragment {
    public Dialog _progressDialogue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void showToast(String message) {
        DMUtils.showToastOnTop(getActivity(), message);
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
                _progressDialogue = DMUtils.showProgressDialog(getActivity());
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
}
