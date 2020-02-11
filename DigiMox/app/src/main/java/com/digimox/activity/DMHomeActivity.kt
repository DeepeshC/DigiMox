package com.digimox.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import android.view.KeyEvent
import android.view.View
import android.view.animation.OvershootInterpolator
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.widget.RelativeLayout
import android.widget.TextView

import com.digimox.R
import com.digimox.api.DMApiManager
import com.digimox.fragments.DMBaseFragment
import com.digimox.fragments.DMFeedBackFragment
import com.digimox.fragments.DMMainCategoryFragment
import com.digimox.fragments.DMMainCategorySubFragment
import com.digimox.fragments.DMMenuDetailParentFragment
import com.digimox.fragments.DMSelectedMenuFragment
import com.digimox.fragments.DMSubCategoryParentFragment
import com.digimox.models.response.DMMainCategory
import com.digimox.models.response.DMSubCategory
import com.digimox.utils.DMDataBaseHelper
import com.digimox.utils.DMUtils
import com.google.gson.Gson
import com.loopj.android.http.JsonHttpResponseHandler

import org.json.JSONArray
import org.json.JSONException

import java.util.ArrayList

import cz.msebera.android.httpclient.Header

/**
 * Created by Deepesh on 17-Nov-15.
 */
class DMHomeActivity : DMBaseActivity() {
    private var actionBar: ActionBar? = null
    private var actionBarLayout: RelativeLayout? = null
    private var isSub = false
    private var isFeedback = false
    private var dmDataBaseHelper: DMDataBaseHelper? = null
    private var dmSubCategories: ArrayList<DMSubCategory?>? = null
    var isNeedRefresh = false



     var broadcastReciever: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(arg0: Context, intent: Intent) {
            val action = intent.action
            if (action == "finish_activity") {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        isSub = intent.getBooleanExtra("SUB", false)
        isFeedback = intent.getBooleanExtra("FEEDBACK", false)
        initViews()
        if (isFeedback) {
            showFeedback()
        } else if (isSub) {
            getMainCategory()
        } else {
            showMainCategory()
        }
    }


    private fun initViews() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        actionBar = supportActionBar
        actionBar!!.setDisplayShowHomeEnabled(false)
        actionBar!!.setDisplayShowCustomEnabled(true)
        actionBar!!.setDisplayShowTitleEnabled(false)
        actionBarLayout = layoutInflater.inflate(R.layout.action_bar, null) as RelativeLayout
        actionBar!!.customView = actionBarLayout
        toolbar.setContentInsetsAbsolute(0, 0)

        actionBarLayout!!.findViewById<View>(R.id.action_bar_back).setOnClickListener { onBackPress() }
        actionBarLayout!!.findViewById<View>(R.id.added_list).setOnClickListener { showSelectedListView() }
        findViewById<View>(R.id.actionbar_home).setOnClickListener { startActivityClass(DMMainHomeActivity::class.java) }
        dmDataBaseHelper = DMDataBaseHelper(this)
        dmDataBaseHelper!!.openDataBase()
        dmSubCategories = dmDataBaseHelper!!.addedList
        if (null == dmSubCategories || dmSubCategories!!.size == 0) {
            DMUtils.setValueToView(findViewById(R.id.added_list_count), 0.toString())
        } else {
            DMUtils.setValueToView(findViewById(R.id.added_list_count), dmSubCategories!!.size.toString())
        }
        //        findViewById(R.id.back_list).setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View view) {
        //                onBackPress();
        //            }
        //        });
        supportFragmentManager.addOnBackStackChangedListener {
            val fr = supportFragmentManager.findFragmentById(R.id.fragment_container)
            if (fr != null) {
                if (fr is DMBaseFragment) {
                    if (fr is DMMainCategoryFragment || fr is DMMainCategorySubFragment || fr is DMFeedBackFragment) {
                        //                            hideBackButton();
                    }
                }
            }
        }
    }

    fun showListButton() {
        dmSubCategories = dmDataBaseHelper!!.addedList
        if (null != dmSubCategories && dmSubCategories!!.size > 0) {
            var scale = ScaleAnimation(0f, 2f, 0f, 2f, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f)
            scale.duration = 1000
            scale.interpolator = OvershootInterpolator()
            findViewById<View>(R.id.added_list).startAnimation(scale)
            findViewById<View>(R.id.added_list).clearAnimation()
            scale = ScaleAnimation(2f, 1f, 2f, 1f, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f)
            scale.duration = 1000
            scale.interpolator = OvershootInterpolator()
            findViewById<View>(R.id.added_list).startAnimation(scale)
            findViewById<View>(R.id.added_list_count).startAnimation(scale)
            DMUtils.setValueToView(findViewById(R.id.added_list_count), dmSubCategories!!.size.toString())
        }
    }

    fun moveViewToNextView(view: View) {
        val anim = TranslateAnimation(0f, getRelativeLeft(view).toFloat(), 0f, getRelativeTop(findViewById(R.id.added_list)).toFloat())
        anim.duration = 1000
        anim.fillAfter = true
        view.startAnimation(anim)
    }

    private fun getRelativeLeft(myView: View): Int {
        return if (myView.parent === myView.rootView)
            myView.left
        else
            myView.left + getRelativeLeft(myView.parent as View)
    }

    private fun getRelativeTop(myView: View): Int {
        return if (myView.parent === myView.rootView)
            myView.top
        else
            myView.top + getRelativeTop(myView.parent as View)
    }

    private fun showSelectedListView() {
        val fm = supportFragmentManager
        val baseFragment = activeFragment

        if (null != fm && baseFragment !is DMSelectedMenuFragment) {
            isNeedRefresh = true
            val dmSelectedMenuFragment = DMSelectedMenuFragment()
            dmSelectedMenuFragment.setDmSubCategories(dmSubCategories)
            val ft = fm.beginTransaction()
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_left)
                    .add(R.id.fragment_container, dmSelectedMenuFragment, "selectedMenuFragment")
                    .addToBackStack("selectedMenuFragment")
                    .commit()
        }

    }

    fun setMeuCount(count: String) {
        (findViewById<View>(R.id.added_list_count) as TextView).text = count
    }


    fun hideBackArrow() {
        actionBarLayout!!.findViewById<View>(R.id.action_bar_back).visibility = View.INVISIBLE
    }

    fun showBackArrow() {
        actionBarLayout!!.findViewById<View>(R.id.action_bar_back).visibility = View.VISIBLE
    }

    private fun showMainCategory() {
        val fm = supportFragmentManager
        if (null != fm) {
            val ft = fm.beginTransaction()
            ft.add(R.id.fragment_container, DMMainCategoryFragment(), "mainCategoryFragment")
                    .addToBackStack("mainCategoryFragment")
                    .commit()
            hideBackArrow()
        }
    }

    private fun showFeedback() {
        val fm = supportFragmentManager
        if (null != fm) {
            val ft = fm.beginTransaction()
            ft.add(R.id.fragment_container, DMFeedBackFragment(), "feedbackFragment")
                    .addToBackStack("feedbackFragment")
                    .commit()
        }
    }

    private fun getMainCategory() {
        if (DMUtils.isOnline()) {
            requestDidStart()
            val url = DMApiManager.METHOD_MAIN_CATEGORY + "gid=0&lid=" + DMUtils.getLanguageId(this)
            val dmApiManager = DMApiManager(this)
            dmApiManager.get(url, object : JsonHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONArray?) {
                    super.onSuccess(statusCode, headers, response)
                    requestDidFinish()
                    try {
                        val dmMainCategories = ArrayList<DMMainCategory>()
                        val gson = Gson()
                        for (i in 0 until response!!.length() - 1) {
                            val responseJSon = response.getJSONObject(i)
                            val dmMainCategory = gson.fromJson(responseJSon.toString(), DMMainCategory::class.java)
                            dmMainCategories.add(dmMainCategory)
                        }
                        showSubCategoryMainFragment(dmMainCategories, dmMainCategories[0], 0)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            })
        } else {
            showToast(resources.getString(R.string.api_error_no_network))
        }

    }

    fun showSubCategoryMainFragment(dmMainCategories: ArrayList<DMMainCategory>, dmMainCategory: DMMainCategory, position: Int) {
        val fm = supportFragmentManager
        if (null != fm) {
            val dmSubCategoryParentFragment = DMSubCategoryParentFragment()
            dmSubCategoryParentFragment.setDmMainCategory(dmMainCategory)
            dmSubCategoryParentFragment.setDmMainCategories(dmMainCategories)
            dmSubCategoryParentFragment.setPosition(position)
            val ft = fm.beginTransaction()
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_left)
                    .add(R.id.fragment_container, dmSubCategoryParentFragment, "subCategoryMainFragment")
                    .addToBackStack("subCategoryMainFragment")
                    .commit()
            showBackArrow()
        }
    }

    private fun onBackPress() {
        val baseFragment = fragment
        if (null != baseFragment && baseFragment is DMSubCategoryParentFragment && isNeedRefresh) {
            val dmSubCategoryFragment = baseFragment as DMSubCategoryParentFragment?
            dmSubCategoryFragment!!.initViews()
            isNeedRefresh = false
            popStack()
        } else if (null != baseFragment && baseFragment is DMMenuDetailParentFragment) {
            val dmMenuDetailParentFragment = baseFragment as DMMenuDetailParentFragment?
            dmMenuDetailParentFragment!!.initViews()
            popStack()
        } else {
            val dmBaseFragment = activeFragment
            if (fragmentCount == 1) {
                if (isTaskRoot) {
                    DMUtils.setLanguageId(this, "")
                    DMUtils.setExchangeRate(this, "")
                    DMUtils.setCurrencyCode(this, "")
                    DMUtils.setLanguageName(this, "")
                    DMUtils.setLanguage(this, "")
                }
                finish()
            } else if (dmBaseFragment is DMMainCategoryFragment) {
                finish()
            } else if (dmBaseFragment is DMSubCategoryParentFragment && isSub) {
                finish()
            } else if (dmBaseFragment is DMFeedBackFragment && isFeedback) {
                finish()
            } else {
                popStack()
            }
        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPress()
            return true
        } else {
            return super.onKeyDown(keyCode, event)
        }

    }

    override fun onResume() {
        super.onResume()
        registerReceiver(broadcastReciever, IntentFilter("finish_activity"))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReciever)

    }

    fun popStack() {
        supportFragmentManager.popBackStackImmediate()
        if (fragmentCount == 1) {
            hideBackArrow()
        }

    }

    override fun onBackPressed() {
        onBackPress()
    }

    fun showBackButton() {
        findViewById<View>(R.id.back_list).visibility = View.VISIBLE
    }

    fun hideBackButton() {
        findViewById<View>(R.id.back_list).visibility = View.GONE
    }
}
