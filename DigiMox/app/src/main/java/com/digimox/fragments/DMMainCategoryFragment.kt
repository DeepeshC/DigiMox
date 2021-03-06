package com.digimox.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.digimox.R
import com.digimox.activity.DMHomeActivity
import com.digimox.adapters.DMCategoryAdapter
import com.digimox.api.DMApiManager
import com.digimox.app.DMAppConstants
import com.digimox.models.response.DMMainCategory
import com.digimox.models.response.DMUserDetails
import com.digimox.utils.DMUtils
import com.google.gson.Gson
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONException
import java.util.*

/**
 * Created by Deepesh on 26-Nov-15.
 */
class DMMainCategoryFragment : DMBaseFragment() {
    private var mainCategoryList: RecyclerView? = null
    private var itemView: View? = null
    var dmUserDetails: DMUserDetails? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var pullLayout: RelativeLayout? = null
    private val pullHandler = Handler()

    //Here's a runnable/handler combo
    private val pullRunnable = Runnable {
        if (null != activity) {
            pullLayout!!.clearAnimation()
            val animation = AnimationUtils.loadAnimation(activity, R.anim.slide_out_up)
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
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        itemView = inflater.inflate(R.layout.fragment_main_category, container, false)
        initViews()
        return itemView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMainCategory()
    }

    private fun getMainCategory() {
        if (DMUtils.isOnline()) {
            requestDidStart()
            val url = DMApiManager.METHOD_MAIN_CATEGORY + "gid=0&lid=" + DMUtils.getLanguageId(activity!!) + "&uid=" + DMUtils.getUserId(activity!!)
            val dmApiManager = DMApiManager(activity!!)
            dmApiManager[url, object : JsonHttpResponseHandler() {
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
                        initViews(dmMainCategories)

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            }]
        } else {
            if (isAdded)
                showToast(resources.getString(R.string.api_error_no_network))
        }

    }

    private fun initViews() {
        mainCategoryList = itemView!!.findViewById<View>(R.id.main_category_list) as RecyclerView
        swipeRefreshLayout = itemView!!.findViewById<View>(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        pullLayout = itemView!!.findViewById<View>(R.id.pull_down_layout) as RelativeLayout
        mainCategoryList!!.setHasFixedSize(true)
        val animation = AnimationUtils.loadAnimation(activity, R.anim.slide_in_up)
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

    private fun initViews(dmMainCategories: ArrayList<DMMainCategory>) {
        mainCategoryList!!.adapter = null
        checkOrientation()
        val dmCategoryAdapter = DMCategoryAdapter(activity!!, this, dmMainCategories)
        mainCategoryList!!.adapter = dmCategoryAdapter
        swipeRefreshLayout!!.setOnRefreshListener { getMainCategory() }
        swipeRefreshLayout!!.isRefreshing = false


    }

    private fun checkOrientation() {
        val orientation = DMUtils.getScreenOrientation(activity)
        if (DMAppConstants.PORTRAIT == orientation) {
            mainCategoryList!!.layoutManager = GridLayoutManager(activity, 2)
        } else {
            mainCategoryList!!.layoutManager = GridLayoutManager(activity, 4)
        }
    }

    fun clickMainCategory(dmMainCategories: ArrayList<DMMainCategory>, dmMainCategory: DMMainCategory, position: Int) {
        if (0 < Integer.parseInt(dmMainCategory.hasChild)) {
            showMainCategory(dmMainCategory)
        } else {
            showSubCategoryMainFragment(dmMainCategories, dmMainCategory, position)
        }
    }

    private fun showMainCategory(dmMainCategory: DMMainCategory) {
        val fm = activity!!.supportFragmentManager
        if (null != fm) {
            val dmMainCategorySubFragment = DMMainCategorySubFragment()
            dmMainCategorySubFragment.setMainCategory(dmMainCategory)
            val ft = fm.beginTransaction()
            ft.add(R.id.fragment_container, dmMainCategorySubFragment, "mainCategorySubFragment")
                    .addToBackStack("mainCategorySubFragment")
                    .commit()
        }
    }

    private fun showSubCategoryMainFragment(dmMainCategories: ArrayList<DMMainCategory>, dmMainCategory: DMMainCategory, position: Int) {
        val fm = activity!!.supportFragmentManager
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
            (activity as DMHomeActivity).showBackArrow()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val orientation = newConfig.orientation
        when (orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> mainCategoryList!!.layoutManager = GridLayoutManager(activity, 4)
            Configuration.ORIENTATION_PORTRAIT -> mainCategoryList!!.layoutManager = GridLayoutManager(activity, 2)
        }
    }
}
