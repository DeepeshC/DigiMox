package com.digimox.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView

import com.digimox.R
import com.digimox.api.DMApiManager
import com.digimox.models.response.DMCurrency
import com.digimox.models.response.DMLanguage
import com.digimox.models.response.DMUserDetails
import com.digimox.utils.DMDataBaseHelper
import com.digimox.utils.DMUtils
import com.google.gson.Gson
import com.loopj.android.http.JsonHttpResponseHandler

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

import cz.msebera.android.httpclient.Header

class DMLoginActivity : DMBaseActivity(), View.OnClickListener {
    private var userName: EditText? = null
    private var password: EditText? = null
    private var dmDataBaseHelper: DMDataBaseHelper? = null

    var listener: PopupMenu.OnMenuItemClickListener = PopupMenu.OnMenuItemClickListener { menuItem ->
        when (menuItem.itemId) {
            R.id.english -> selectEnglish()
            R.id.arabic -> selectArabic()
        }

        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dm_login)
        dmDataBaseHelper = DMDataBaseHelper(this)
        initViews()
        setOnClickListener()
    }

    private fun initViews() {
        userName = findViewById<View>(R.id.user_name) as EditText
        password = findViewById<View>(R.id.password) as EditText
    }

    private fun setOnClickListener() {
        findViewById<View>(R.id.language_select).setOnClickListener(this)
        findViewById<View>(R.id.sign_in).setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.language_select -> showLanguagePopUp()
            R.id.sign_in -> signIn()
            else -> {
            }
        }
    }

    private fun signIn() {
        if (DMUtils.isOnline()) {
            val userNameString = DMUtils.getValueFromView(userName)
            val passwordString = DMUtils.getValueFromView(password)
            if (null != userNameString) {
                if (null != passwordString) {
                    requestDidStart()
                    val url = DMApiManager.METHOD_LOGIN + "username=" + userNameString + "&password=" + passwordString
                    val dmApiManager = DMApiManager(this)
                    dmApiManager.get(url, object : JsonHttpResponseHandler() {

                        override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseArray: JSONArray?) {
                            super.onSuccess(statusCode, headers, responseArray)
                            try {
                                val responseJson = responseArray!!.getJSONObject(0)
                                val gson = Gson()
                                val dmUserDetails = gson.fromJson(responseJson.toString(), DMUserDetails::class.java)
                                DMUtils.setUserId(this@DMLoginActivity, dmUserDetails.userDetailsId)
                                dmDataBaseHelper!!.openDataBase()
                                dmDataBaseHelper!!.insertUserData(dmUserDetails)
                                dmDataBaseHelper!!.close()
                                getLanguageList()
                            } catch (e: JSONException) {
                                requestDidFinish()
                                e.printStackTrace()
                                showToast(resources.getString(R.string.login_error))
                            }

                        }

                        override fun onFailure(statusCode: Int, headers: Array<Header>?, responseString: String?, throwable: Throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable)
                            requestDidFinish()
                            showToast(resources.getString(R.string.login_error))
                        }

                        override fun onFailure(statusCode: Int, headers: Array<Header>?, throwable: Throwable, errorResponse: JSONObject?) {
                            super.onFailure(statusCode, headers, throwable, errorResponse)
                            requestDidFinish()
                            showToast(resources.getString(R.string.login_error))
                        }

                        override fun onFailure(statusCode: Int, headers: Array<Header>?, throwable: Throwable, errorResponse: JSONArray?) {
                            super.onFailure(statusCode, headers, throwable, errorResponse)
                            requestDidFinish()
                            showToast(resources.getString(R.string.login_error))
                        }
                    })
                } else {
                    showToast(resources.getString(R.string.please_enter_password))
                }
            } else {
                showToast(resources.getString(R.string.please_enter_user_name))
            }
        } else {
            showToast(resources.getString(R.string.api_error_no_network))
        }
    }

    private fun getLanguageList() {
        if (DMUtils.isOnline()) {
            requestDidStart()
            val url = DMApiManager.METHOD_LANGUAGE + "uid=" + DMUtils.getUserId(this)
            val dmApiManager = DMApiManager(this)
            dmApiManager.get(url, object : JsonHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONArray?) {
                    super.onSuccess(statusCode, headers, response)

                    try {
                        val dmLanguages = ArrayList<DMLanguage>()
                        val gson = Gson()
                        for (i in 0 until response!!.length() - 1) {
                            val responseJSon = response.getJSONObject(i)
                            val dmLanguage = gson.fromJson(responseJSon.toString(), DMLanguage::class.java)
                            dmLanguages.add(dmLanguage)
                        }
                        getCurrencyList(dmLanguages)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            })
        } else {
            showToast(resources.getString(R.string.api_error_no_network))
        }
    }

    private fun getCurrencyList(dmLanguages: ArrayList<DMLanguage>) {
        val url = DMApiManager.METHOD_CURRENCY + "uid=" + DMUtils.getUserId(this)
        val dmApiManager = DMApiManager(this)
        dmApiManager.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONArray?) {
                super.onSuccess(statusCode, headers, response)
                requestDidFinish()
                try {
                    val dmCurrencies = ArrayList<DMCurrency>()
                    val gson = Gson()
                    for (i in 0 until response!!.length() - 1) {
                        val responseJSon = response.getJSONObject(i)
                        val dmCurrency = gson.fromJson(responseJSon.toString(), DMCurrency::class.java)
                        dmCurrencies.add(dmCurrency)
                    }
                    startHomeView(dmLanguages, dmCurrencies)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })
    }

    private fun startHomeView(dmLanguages: ArrayList<DMLanguage>, dmCurrencies: ArrayList<DMCurrency>) {
        dmDataBaseHelper!!.openDataBase()
        dmDataBaseHelper!!.deleteCurrencyTable()
        dmDataBaseHelper!!.deleteLanguageTable()
        dmDataBaseHelper!!.insertCurrencyData(dmCurrencies)
        dmDataBaseHelper!!.insertLanguageData(dmLanguages)
        dmDataBaseHelper!!.close()
        val homeIntent = Intent(this, DMMainHomeActivity::class.java)
        finish()
        startActivity(homeIntent)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_language, menu)
        return true
    }

    private fun showLanguagePopUp() {
        val pum = PopupMenu(this, findViewById(R.id.language_select))
        pum.inflate(R.menu.menu_language)
        pum.show()
        pum.setOnMenuItemClickListener(listener)
    }

    private fun setLanguage(language: String, languageName: String) {
        (findViewById<View>(R.id.language_select) as TextView).text = languageName
    }


    fun selectEnglish() {
        setLanguage("", resources.getString(R.string.english))
    }

    fun selectArabic() {
        setLanguage("", resources.getString(R.string.arabic))
    }
}
