package com.digimox.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.digimox.R
import com.digimox.api.DMApiManager
import com.digimox.models.response.DMCurrency
import com.digimox.models.response.DMLanguage
import com.digimox.utils.DMDataBaseHelper
import com.digimox.utils.DMUtils
import com.google.gson.Gson
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * Created by Deepesh on 10-Dec-15.
 */
class DMSplashActivity : DMBaseActivity() {
    private var dmDataBaseHelper: DMDataBaseHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        checkIsLogin()
    }

    private fun checkIsLogin() {
        if (TextUtils.isEmpty(DMUtils.getUserId(this))) {
            val timerThread = object : Thread() {
                override fun run() {
                    try {
                        sleep(3000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    } finally {
                        startActivityClass(DMLoginActivity::class.java)
                    }
                }
            }
            timerThread.start()

        } else {
            getLanguageList()

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

                override fun onFailure(statusCode: Int, headers: Array<Header>?, throwable: Throwable, errorResponse: JSONObject?) {
                    super.onFailure(statusCode, headers, throwable, errorResponse)
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
        dmDataBaseHelper = DMDataBaseHelper(this)
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
}
