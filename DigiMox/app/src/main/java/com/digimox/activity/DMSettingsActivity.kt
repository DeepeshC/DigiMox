package com.digimox.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView

import com.digimox.R
import com.digimox.models.response.DMUserDetails
import com.digimox.utils.DMDataBaseHelper
import com.digimox.utils.DMUtils

class DMSettingsActivity : DMBaseActivity(), View.OnClickListener {
    private val termsConditionText: TextView? = null
    private var dmDataBaseHelper: DMDataBaseHelper? = null
    private var dmUserDetails: DMUserDetails? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        dmDataBaseHelper = DMDataBaseHelper(this)
        dmDataBaseHelper!!.openDataBase()
        dmUserDetails = dmDataBaseHelper!!.userData
        initViews()
        setOnClickListener()
    }

    private fun initViews() {
        DMUtils.setValueToView(findViewById(R.id.hotel_text), dmUserDetails!!.restaurantAbout + "\n" + dmUserDetails!!.userDetailsAddress + "\n" + dmUserDetails!!.userDetailsCity
                + "\n" + dmUserDetails!!.userDetailsState + "\n" + dmUserDetails!!.userDetailsWebsite + "\n" + dmUserDetails!!.userDetailsTiming)
    }

    private fun setOnClickListener() {
        findViewById<View>(R.id.settings_close).setOnClickListener(this)
        findViewById<View>(R.id.settings_feedback).setOnClickListener(this)
        findViewById<View>(R.id.settings_main_category).setOnClickListener(this)
        findViewById<View>(R.id.settings_logout).setOnClickListener(this)
        findViewById<View>(R.id.settings_sub_category).setOnClickListener(this)
        findViewById<View>(R.id.settings_search).setOnClickListener(this)
        findViewById<View>(R.id.settings_home).setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.settings_close -> finish()
            R.id.settings_feedback -> startActivity(DMFeedbackActivity::class.java)
            R.id.settings_main_category -> goToMainCategory(false)
            R.id.settings_logout -> logout()
            R.id.settings_sub_category -> goToMainCategory(true)
            R.id.settings_search -> {
            }
            R.id.settings_home -> {
                val intent = Intent("finish_activity")
                sendBroadcast(intent)
                startActivityClass(DMMainHomeActivity::class.java)
            }
        }

    }

    private fun logout() {
        dmDataBaseHelper!!.deleteUserTable()
        DMUtils.setUserId(this@DMSettingsActivity, "")
        DMUtils.setLanguageId(this, "")
        val intent = Intent(this, DMLoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun goToMainCategory(isSub: Boolean) {

        val homeIntent = Intent(this@DMSettingsActivity, DMHomeActivity::class.java)
        homeIntent.putExtra("SUB", isSub)
        startActivity(homeIntent)
    }
}
