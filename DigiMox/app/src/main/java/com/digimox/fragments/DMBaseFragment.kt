package com.digimox.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.digimox.utils.DMUtils

/**
 * Created by Deepesh on 26-Nov-15.
 */
open class DMBaseFragment : Fragment() {
    var _progressDialogue: Dialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun showToast(message: String) {
        DMUtils.showToastOnTop(activity, message)
    }


    fun requestDidStart() {
        /**
         * Starting the progressing indicator
         */
        if (_progressDialogue != null) {
            if (_progressDialogue!!.isShowing) {
            }
        } else {
            try {
                _progressDialogue = DMUtils.showProgressDialog(activity)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }

    fun requestDidFinish() {
        /**
         * Finishing the progressing indicator
         */

        if (_progressDialogue != null) {
            if (_progressDialogue!!.isShowing) {
                try {

                    _progressDialogue!!.dismiss()
                    _progressDialogue = null

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }
}
