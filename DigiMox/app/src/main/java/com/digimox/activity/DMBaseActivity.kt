package com.digimox.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.digimox.fragments.DMBaseFragment
import com.digimox.utils.DMUtils

/**
 * Created by Deepesh on 26-Nov-15.
 */
open class DMBaseActivity : AppCompatActivity() {
    private var progressDialogue: Dialog? = null

    val activeFragment: DMBaseFragment?
        get() {
            val fragmentManager = supportFragmentManager
            if (fragmentManager.backStackEntryCount == 0) {
                return null
            }
            val tag = supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1).name
            return supportFragmentManager.findFragmentByTag(tag) as DMBaseFragment?
        }

    val fragmentCount: Int
        get() {
            val fragmentManager = supportFragmentManager
            return if (fragmentManager.backStackEntryCount == 0) {
                0
            } else fragmentManager.backStackEntryCount
        }

    val fragment: DMBaseFragment?
        get() {
            val fragmentManager = supportFragmentManager
            if (fragmentManager.backStackEntryCount == 0) {
                return null
            }
            if (fragmentManager.backStackEntryCount == 1) {
                return null
            }
            val tag = supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 2).name
            return supportFragmentManager.findFragmentByTag(tag) as DMBaseFragment?
        }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected fun startActivity(cls: Class<*>) {
        startActivity(Intent(this, cls))

    }

    protected fun startActivityClass(cls: Class<*>) {
        finish()
        startActivity(Intent(this, cls))

    }

    fun showToast(message: String) {
        DMUtils.showToastOnTop(this, message)

    }

    fun requestDidStart() {
        /**
         * Starting the progressing indicator
         */
        if (progressDialogue != null) {
            if (progressDialogue!!.isShowing) {
            }
        } else {
            try {
                progressDialogue = DMUtils.showProgressDialog(this)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }

    fun requestDidFinish() {
        /**
         * Finishing the progressing indicator
         */

        if (progressDialogue != null) {
            if (progressDialogue!!.isShowing) {
                try {

                    progressDialogue!!.dismiss()
                    progressDialogue = null

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }
}
