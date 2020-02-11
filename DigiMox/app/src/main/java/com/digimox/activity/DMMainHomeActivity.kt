package com.digimox.activity

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.digimox.R
import com.digimox.api.DMApiManager
import com.digimox.app.DMAppConstants
import com.digimox.models.response.DMCurrency
import com.digimox.models.response.DMLanguage
import com.digimox.models.response.DMUserDetails
import com.digimox.utils.DMDataBaseHelper
import com.digimox.utils.DMUtils
import com.google.gson.Gson
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Deepesh on 10-Dec-15.
 */
class DMMainHomeActivity : DMBaseActivity(), View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    private var popupWindowLanguage: PopupWindow? = null
    private var popupWindowCurrency: PopupWindow? = null
    private var popupMenuLanguage: PopupMenu? = null
    private var popupMenuCurrency: PopupMenu? = null
    private var dmDataBaseHelper: DMDataBaseHelper? = null
    private var dmUserDetails: DMUserDetails? = null
    private var dmLanguages: ArrayList<DMLanguage>? = null
    private var dmCurrencies: ArrayList<DMCurrency>? = null
    private var selectedLanguage: String? = null
    private var selectedLanguageId: String? = null
    private var selectedCurrency: String? = null
    private var selectedCurrencyRate: String? = null
    private var currencyId: String? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var pullLayout: RelativeLayout? = null
    private val pullHandler = Handler()

    //Here's a runnable/handler combo
    private val pullRunnable = Runnable {
        pullLayout!!.clearAnimation()
        val animation = AnimationUtils.loadAnimation(this@DMMainHomeActivity, R.anim.slide_out_up)
        animation.duration = 1000
        pullLayout!!.animation = animation
        pullLayout!!.animate()
        animation.start()
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                pullLayout!!.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {

            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.initial_page)
        dmDataBaseHelper = DMDataBaseHelper(this)
        dmDataBaseHelper!!.openDataBase()
        dmUserDetails = dmDataBaseHelper!!.userData
        dmLanguages = dmDataBaseHelper!!.languageList
        dmCurrencies = dmDataBaseHelper!!.currencyList
        initViews()
        showPullAnimation()
        setOnClickListener()
        //        showLanguagePopup();
        //        showCurrencyPopup();
        createLanguageSpinner()
        createCurrencyPopUp()
    }

    private fun showPullAnimation() {
        pullLayout = findViewById<View>(R.id.pull_down_layout) as RelativeLayout
        val animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_up)
        animation.duration = 1000
        pullLayout!!.visibility = View.VISIBLE
        pullLayout!!.animation = animation
        pullLayout!!.animate()
        animation.start()
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                pullHandler.postDelayed(pullRunnable, 2000)
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
    }

    private fun initViews() {
        swipeRefreshLayout = findViewById<View>(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        swipeRefreshLayout!!.isRefreshing = false
        if (!TextUtils.isEmpty(dmUserDetails!!.userDetailsLogo)) {
            DMUtils.setImageUrlToView(this, findViewById<View>(R.id.restaurant_logo) as ImageView,
                    dmUserDetails!!.userDetailsLogo, findViewById<View>(R.id.progress_restaurant_logo) as ProgressBar)


        }
        if (!TextUtils.isEmpty(DMUtils.getLanguageName(this))) {
            selectedLanguage = DMUtils.getLanguage(this)
            DMUtils.setValueToView(findViewById(R.id.lang_selector), DMUtils.getLanguageName(this))
        } else {
            for (dmLanguage in dmLanguages!!) {
                if (dmLanguage.languageId.equals(dmUserDetails!!.user_default_language, ignoreCase = true)) {
                    selectedLanguage = dmLanguage.languageShort
                    DMUtils.setValueToView(findViewById(R.id.lang_selector), dmLanguage.languageName)
                }
            }
        }
        if (!TextUtils.isEmpty(DMUtils.getCurrencyCode(this))) {
            selectedCurrency = DMUtils.getCurrencyCode(this)
            selectedCurrencyRate = DMUtils.getExchangeRate(this)
            currencyId = DMUtils.getCurrencyId(this)
            DMUtils.setValueToView(findViewById(R.id.currency_selector), selectedCurrency)
        } else {
            for (dmCurrency in dmCurrencies!!) {
                if (dmCurrency.currencyId.equals(dmUserDetails!!.user_default_currency, ignoreCase = true)) {
                    selectedCurrency = dmCurrency.currencyCode
                    selectedCurrencyRate = dmCurrency.currencyExchangeRate
                    currencyId = dmCurrency.currencyId
                    DMUtils.setValueToView(findViewById(R.id.currency_selector), dmCurrency.currencyCode)
                }
            }
        }
        selectedLanguageId = dmUserDetails!!.user_default_language
        DMUtils.setValueToView(findViewById(R.id.restaurant_name), dmUserDetails!!.userDetailsRestaurantName)
        DMUtils.setValueToView(findViewById(R.id.title_header), dmUserDetails!!.userDetailsRestaurantName)
        DMUtils.setValueToView(findViewById(R.id.restaurant_description), dmUserDetails!!.restaurantAbout)
        // DMUtils.setValueToView(findViewById(R.id.restaurant_description), dmUserDetails.getRestaurantAbout() + "\n" + dmUserDetails.getUserDetailsAddress() + "\n" + dmUserDetails.getUserDetailsCity()
        //                + "\n" + dmUserDetails.getUserDetailsState() + "\n" + dmUserDetails.getUserDetailsWebsite() + "\n" + dmUserDetails.getUserDetailsTiming());
        swipeRefreshLayout!!.setOnRefreshListener { getLanguageList() }
    }

    private fun getLanguageList() {
        if (DMUtils.isOnline()) {
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
        initViews()

    }

    private fun setOnClickListener() {
        findViewById<View>(R.id.lang_selector).setOnClickListener(this)
        findViewById<View>(R.id.currency_selector).setOnClickListener(this)
        findViewById<View>(R.id.go_to_next).setOnClickListener(this)
        findViewById<View>(R.id.feed_back_text).setOnClickListener(this)
        findViewById<View>(R.id.feed_back_img).setOnClickListener(this)
        findViewById<View>(R.id.home_logout).setOnClickListener(this)
    }

    override fun onMenuItemClick(menuItem: MenuItem): Boolean {
        return false
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.lang_selector ->
                //                popupMenuLanguage.show();
                popupWindowLanguage!!.showAsDropDown(view, -5, 0)
            R.id.currency_selector ->
                //                popupMenuCurrency.show();
                popupWindowCurrency!!.showAsDropDown(view, -5, 0)
            R.id.go_to_next -> goNext()
            R.id.feed_back_text -> goToFeedback()
            R.id.feed_back_img -> goToFeedback()
            R.id.home_logout -> logout()
            else -> {
            }
        }
    }

    private fun logout() {
        dmDataBaseHelper!!.openDataBase()
        dmDataBaseHelper!!.deleteUserTable()
        dmDataBaseHelper!!.deleteCurrencyTable()
        dmDataBaseHelper!!.deleteLanguageTable()
        dmDataBaseHelper!!.deleteTable()
        DMUtils.setUserId(this, "")
        DMUtils.setLanguageId(this, "")
        val intent = Intent(this, DMLoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun goToFeedback() {
        DMUtils.setLanguageId(this, selectedLanguageId)
        DMUtils.setCurrencyCode(this, selectedCurrency)
        val homeIntent = Intent(this, DMHomeActivity::class.java)
        homeIntent.putExtra("FEEDBACK", true)
        startActivity(homeIntent)
    }

    private fun goNext() {
        if (null != DMUtils.getValueFromView(findViewById(R.id.lang_selector))) {
            if (null != DMUtils.getValueFromView(findViewById(R.id.currency_selector))) {
                DMUtils.setLanguageId(this, selectedLanguageId)
                DMUtils.setCurrencyId(this, currencyId)
                DMUtils.setExchangeRate(this, selectedCurrencyRate)
                DMUtils.setCurrencyCode(this, selectedCurrency)
                DMUtils.setLanguageName(this, DMUtils.getValueFromView(findViewById(R.id.lang_selector)))
                setLanguageLocate(this, selectedLanguage)
                startActivityClass(DMHomeActivity::class.java)
            } else {
                showToast(resources.getString(R.string.please_select_currency))
            }
        } else {
            showToast(resources.getString(R.string.please_select_language))
        }
    }

    private fun createLanguageSpinner() {
        popupWindowLanguage = popupWindowLanguage()
    }

    private fun createCurrencyPopUp() {
        popupWindowCurrency = popupWindowCurrency()
    }

    private fun showLanguagePopup() {

        popupMenuLanguage = PopupMenu(this, findViewById(R.id.lang_selector))
        for (i in dmLanguages!!.indices) {
            popupMenuLanguage!!.menu.add(i, i, i, dmLanguages!![i].languageName)
        }
        popupMenuLanguage!!.setOnMenuItemClickListener { menuItem ->
            selectedLanguage = dmLanguages!![menuItem.itemId].languageShort
            selectedLanguageId = dmLanguages!![menuItem.itemId].languageId

            DMUtils.setValueToView(findViewById(R.id.lang_selector), dmLanguages!![menuItem.itemId].languageName)
            false
        }
    }

    private fun showCurrencyPopup() {

        popupMenuCurrency = PopupMenu(this, findViewById(R.id.currency_selector))
        for (i in dmCurrencies!!.indices) {
            popupMenuCurrency!!.menu.add(i, i, i, dmCurrencies!![i].currencyCode)
        }
        popupMenuCurrency!!.setOnMenuItemClickListener { menuItem ->
            selectedCurrency = dmCurrencies!![menuItem.itemId].currencyCode
            selectedCurrencyRate = dmCurrencies!![menuItem.itemId].currencyExchangeRate

            DMUtils.setValueToView(findViewById(R.id.currency_selector), dmCurrencies!![menuItem.itemId].currencyCode)
            false
        }
    }

    private fun popupWindowLanguage(): PopupWindow {
        val popupWindow = PopupWindow(this)
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.pop_up_view, null)
        popupWindow.contentView = view
        val listViewLanguage = view.findViewById<View>(R.id.pop_list) as ListView
        listViewLanguage.divider = ColorDrawable(resources.getColor(R.color.green_bg))
        listViewLanguage.dividerHeight = 1
        listViewLanguage.adapter = ArrayLanguageAdapter(this@DMMainHomeActivity, dmLanguages)
        popupWindow.isFocusable = true
        popupWindow.width = WindowManager.LayoutParams.WRAP_CONTENT
        popupWindow.height = WindowManager.LayoutParams.WRAP_CONTENT
        popupWindow.setBackgroundDrawable(ColorDrawable(0))
        listViewLanguage.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, l ->
            DMUtils.setValueToView(findViewById(R.id.lang_selector), dmLanguages!![position].languageName)
            selectedLanguage = dmLanguages!![position].languageShort
            selectedLanguageId = dmLanguages!![position].languageId
            popupWindow.dismiss()
        }
        return popupWindow
    }

    inner class ArrayLanguageAdapter(context: Context, dmLanguages: ArrayList<DMLanguage>?) : ArrayAdapter<DMLanguage>(context, 0, dmLanguages
            ?: ArrayList()) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            val item = getItem(position)
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.spinner_list, parent, false)
            }
            val tvName = convertView!!.findViewById<View>(R.id.spinner_list) as TextView
            tvName.text = item!!.languageName
            return convertView
        }
    }

    inner class ArrayCurrencyAdapter(context: Context, dmCurrencies: ArrayList<DMCurrency>?) : ArrayAdapter<DMCurrency>(context, 0, dmCurrencies
            ?: ArrayList()) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            val item = getItem(position)
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.spinner_list_currency, parent, false)
            }
            val tvName = convertView!!.findViewById<View>(R.id.spinner_list) as TextView
            tvName.text = item!!.currencyCode
            return convertView
        }
    }


    private fun popupWindowCurrency(): PopupWindow {
        val popupWindow = PopupWindow(this)
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.pop_up_view, null)
        popupWindow.contentView = view
        val listViewCurrency = view.findViewById<View>(R.id.pop_list) as ListView
        listViewCurrency.divider = ColorDrawable(resources.getColor(R.color.green_bg))
        listViewCurrency.dividerHeight = 1
        listViewCurrency.adapter = ArrayCurrencyAdapter(this@DMMainHomeActivity, dmCurrencies)
        popupWindow.isFocusable = true
        popupWindow.width = WindowManager.LayoutParams.WRAP_CONTENT
        popupWindow.height = WindowManager.LayoutParams.WRAP_CONTENT
        popupWindow.setBackgroundDrawable(ColorDrawable(0))
        listViewCurrency.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, l ->
            DMUtils.setValueToView(findViewById(R.id.currency_selector), dmCurrencies!![position].currencyCode)
            selectedCurrency = dmCurrencies!![position].currencyCode
            selectedCurrencyRate = dmCurrencies!![position].currencyExchangeRate
            currencyId = dmCurrencies!![position].currencyId
            popupWindow.dismiss()
        }
        return popupWindow
    }

    override fun onBackPressed() {
        if (isTaskRoot) {
            DMUtils.setLanguageId(this, "")
            DMUtils.setExchangeRate(this, "")
            DMUtils.setCurrencyCode(this, "")
            DMUtils.setLanguageName(this, "")
            DMUtils.setLanguage(this, "")
            finish()
        }
    }

    companion object {

        fun setLanguageLocate(context: Context, languageToLoad: String?) {
            val localeCode = getLocaleCode(languageToLoad)
            DMUtils.setLanguage(context, localeCode)
            val locale = Locale(localeCode)
            Locale.setDefault(locale)
            val config = android.content.res.Configuration()
            config.locale = locale
            context.resources.updateConfiguration(config, null)
        }

        private fun getLocaleCode(languageToLoad: String?): String {
            var localeCode = "en"
            if (DMAppConstants.ARABIC.equals(languageToLoad!!, ignoreCase = true)) {
                localeCode = "ar"
            } else if (DMAppConstants.FRENCH.equals(languageToLoad, ignoreCase = true)) {
                localeCode = "fr"
            } else if (DMAppConstants.SWEDISH.equals(languageToLoad, ignoreCase = true)) {
                localeCode = "sv"
            } else if (DMAppConstants.ITALIAN.equals(languageToLoad, ignoreCase = true)) {
                localeCode = "it"
            } else if (DMAppConstants.ENGLISH.equals(languageToLoad, ignoreCase = true)) {
                localeCode = "en"
            } else if (DMAppConstants.SPANISH.equals(languageToLoad, ignoreCase = true)) {
                localeCode = "es"
            }
            return localeCode
        }
    }
}
