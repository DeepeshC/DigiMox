package com.digimox.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.digimox.R
import com.digimox.activity.DMHomeActivity
import com.digimox.adapters.DMSubCategoryAdapter
import com.digimox.api.DMApiManager
import com.digimox.app.DMAppConstants
import com.digimox.models.response.DMCurrency
import com.digimox.models.response.DMLanguage
import com.digimox.models.response.DMMainCategory
import com.digimox.models.response.DMSubCategory
import com.digimox.utils.DMDataBaseHelper
import com.digimox.utils.DMDividerItemDecoration
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
class DMSubCategoryFragment : DMBaseFragment {
    private var itemView: View? = null
    private var subCategoryList: RecyclerView? = null
    private var dmMainCategory: DMMainCategory? = null
    private var fragmentActivity: FragmentActivity? = null
    private var dmDataBaseHelper: DMDataBaseHelper? = null
    private var mIndex: Int = 0
    private var swipeRefreshLayout: SwipeRefreshLayout? = null


    @SuppressLint("NewApi", "ValidFragment")
    constructor(index: Int) {
        mIndex = index
    }

    constructor() {}

    fun setDmMainCategory(dmMainCategory: DMMainCategory) {
        this.dmMainCategory = dmMainCategory
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        itemView = inflater.inflate(R.layout.fragment_dish_list, container, false)
        initViews()
        return itemView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSubCategory()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentActivity = context as FragmentActivity
        dmDataBaseHelper = DMDataBaseHelper(fragmentActivity)
        dmDataBaseHelper!!.openDataBase()
    }

    fun getSubCategory() {
        if (DMUtils.isOnline()) {
            itemView!!.findViewById<View>(R.id.progress_pager).visibility = View.VISIBLE
            val url = (DMApiManager.METHOD_SUB_CATEGORY + "gid=" + dmMainCategory!!.groupId
                    + "&lid=" + DMUtils.getLanguageId(fragmentActivity!!) + "&curid=" + DMUtils.getCurrencyId(fragmentActivity!!))
            val dmApiManager = DMApiManager(fragmentActivity!!)
            dmApiManager[url, object : JsonHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONArray?) {
                    super.onSuccess(statusCode, headers, response)
                    itemView!!.findViewById<View>(R.id.progress_pager).visibility = View.GONE
                    try {
                        val dmSubCategories = ArrayList<DMSubCategory>()
                        val gson = Gson()
                        for (i in 0 until response!!.length() - 1) {
                            val responseJSon = response.getJSONObject(i)
                            val dmSubCategory = gson.fromJson(responseJSon.toString(), DMSubCategory::class.java)
                            dmSubCategories.add(dmSubCategory)
                        }
                        initList(dmSubCategories)
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

    private fun initList(dmSubCategories: ArrayList<DMSubCategory>?) {
        if (null != fragmentActivity && null != dmSubCategories && dmSubCategories.size > 0) {
            itemView!!.findViewById<View>(R.id.no_item_found).visibility = View.GONE
            subCategoryList!!.adapter = null
            val dmSubCategoryAdapter = DMSubCategoryAdapter(fragmentActivity!!, this, dmSubCategories)
            subCategoryList!!.adapter = dmSubCategoryAdapter
            swipeRefreshLayout!!.isRefreshing = false
        } else {
            itemView!!.findViewById<View>(R.id.no_item_found).visibility = View.VISIBLE
        }
    }

    private fun initViews() {
        subCategoryList = itemView!!.findViewById<View>(R.id.sub_category_list) as RecyclerView
        swipeRefreshLayout = itemView!!.findViewById<View>(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        subCategoryList!!.setHasFixedSize(true)
        checkOrientation()
        subCategoryList!!.addItemDecoration(
                DMDividerItemDecoration(fragmentActivity, R.drawable.sublist_divider))
        swipeRefreshLayout!!.setOnRefreshListener { getSubCategory() }
    }

    //    private void getLanguageList() {
    //        if (DMUtils.isOnline()) {
    //            requestDidStart();
    //            String url = DMApiManager.METHOD_LANGUAGE + "uid=" + DMUtils.getUserId(getActivity());
    //            DMApiManager dmApiManager = new DMApiManager(getActivity());
    //            dmApiManager.get(url, new JsonHttpResponseHandler() {
    //                @Override
    //                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
    //                    super.onSuccess(statusCode, headers, response);
    //
    //                    try {
    //                        ArrayList<DMLanguage> dmLanguages = new ArrayList<DMLanguage>();
    //                        Gson gson = new Gson();
    //                        for (int i = 0; i < response.length() - 1; i++) {
    //                            JSONObject responseJSon = response.getJSONObject(i);
    //                            DMLanguage dmLanguage = gson.fromJson(responseJSon.toString(), DMLanguage.class);
    //                            dmLanguages.add(dmLanguage);
    //                        }
    //                        getCurrencyList(dmLanguages);
    //                    } catch (JSONException e) {
    //                        e.printStackTrace();
    //                    }
    //                }
    //            });
    //        } else {
    //            showToast(getResources().getString(R.string.api_error_no_network));
    //        }
    //    }
    //
    //    private void getCurrencyList(final ArrayList<DMLanguage> dmLanguages) {
    //        String url = DMApiManager.METHOD_CURRENCY + "uid=" + DMUtils.getUserId(getActivity());
    //        DMApiManager dmApiManager = new DMApiManager(getActivity());
    //        dmApiManager.get(url, new JsonHttpResponseHandler() {
    //            @Override
    //            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
    //                super.onSuccess(statusCode, headers, response);
    //                requestDidFinish();
    //                try {
    //                    ArrayList<DMCurrency> dmCurrencies = new ArrayList<DMCurrency>();
    //                    Gson gson = new Gson();
    //                    for (int i = 0; i < response.length() - 1; i++) {
    //                        JSONObject responseJSon = response.getJSONObject(i);
    //                        DMCurrency dmCurrency = gson.fromJson(responseJSon.toString(), DMCurrency.class);
    //                        dmCurrencies.add(dmCurrency);
    //                    }
    //                } catch (JSONException e) {
    //                    e.printStackTrace();
    //                }
    //            }
    //        });
    //    }

    private fun startHomeView(dmLanguages: ArrayList<DMLanguage>, dmCurrencies: ArrayList<DMCurrency>) {
        dmDataBaseHelper = DMDataBaseHelper(activity)
        dmDataBaseHelper!!.openDataBase()
        dmDataBaseHelper!!.deleteCurrencyTable()
        dmDataBaseHelper!!.deleteLanguageTable()
        dmDataBaseHelper!!.insertCurrencyData(dmCurrencies)
        dmDataBaseHelper!!.insertLanguageData(dmLanguages)
        dmDataBaseHelper!!.close()
    }

    fun setRefresh() {
        getSubCategory()
    }

    private fun checkOrientation() {
        val orientation = DMUtils.getScreenOrientation(fragmentActivity)
        if (DMAppConstants.PORTRAIT == orientation) {
            subCategoryList!!.layoutManager = GridLayoutManager(fragmentActivity, 1)
        } else {
            subCategoryList!!.layoutManager = GridLayoutManager(fragmentActivity, 2)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val orientation = newConfig.orientation
        when (orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> subCategoryList!!.layoutManager = GridLayoutManager(fragmentActivity, 2)
            Configuration.ORIENTATION_PORTRAIT -> subCategoryList!!.layoutManager = GridLayoutManager(fragmentActivity, 1)
        }
    }

    fun showItemDetails(dmSubCategory: DMSubCategory, dmSubCategories: ArrayList<DMSubCategory>, position: Int) {
        if (null != fragmentActivity) {
            val fm = fragmentActivity!!.supportFragmentManager
            if (null != fm) {
                val dmMenuDetailParentFragment = DMMenuDetailParentFragment()
                dmMenuDetailParentFragment.setDmSubCategories(dmSubCategories)
                dmMenuDetailParentFragment.setDmSubCategory(dmSubCategory)
                dmMenuDetailParentFragment.setPosition(position)
                val ft = fm.beginTransaction()
                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_left)
                        .add(R.id.fragment_container, dmMenuDetailParentFragment, "itemDetailsParentFragment")
                        .addToBackStack("itemDetailsParentFragment")
                        .commit()
            }
        }
    }

    fun addToDb(imageView: ImageView, textView: TextView, dmSubCategory: DMSubCategory) {
        //        if (dmDataBaseHelper.hasObject(dmSubCategory.getItemId())) {
        //            if (isAdded()) {
        //                showToast(getResources().getString(R.string.already_added));
        //            }
        //        } else {
        try {
            dmDataBaseHelper!!.insertData(dmSubCategory)
            if (isAdded) {
                imageView.setImageResource(R.drawable.select_menu_icon_selected)
                textView.text = resources.getString(R.string.selected)
                textView.setTextColor(getColor(activity, R.color.green_bg))
            }
            (fragmentActivity as DMHomeActivity).showListButton()
        } catch (e: Exception) {
            e.printStackTrace()
            if (isAdded) {
                showToast(resources.getString(R.string.already_added))
            }
        }

        //        }
    }

    fun back() {
        (fragmentActivity as DMHomeActivity).popStack()
    }

    companion object {

        fun getColor(context: Context?, id: Int): Int {
            val version = Build.VERSION.SDK_INT
            return if (version >= 23) {
                ContextCompat.getColor(context!!, id)
            } else {
                context!!.resources.getColor(id)
            }
        }
    }
}
