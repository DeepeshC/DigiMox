package com.digimox.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.core.content.ContextCompat
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView

import com.digimox.R
import com.digimox.activity.DMHomeActivity
import com.digimox.api.DMApiManager
import com.digimox.models.response.DMMenuDetail
import com.digimox.models.response.DMSubCategory
import com.digimox.utils.DMDataBaseHelper
import com.digimox.utils.DMUtils
import com.google.gson.Gson
import com.loopj.android.http.JsonHttpResponseHandler

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

import cz.msebera.android.httpclient.Header

/**
 * Created by Deepesh on 26-Nov-15.
 */
class DMSelectedItemDetailFragment : DMBaseFragment(), View.OnClickListener {
    private var detailView: View? = null
    private var fragmentActivity: FragmentActivity? = null
    private var dmSubCategory: DMSubCategory? = null
    private var dmSubCategoriesDB: ArrayList<DMSubCategory>? = null
    private var dmDataBaseHelper: DMDataBaseHelper? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        detailView = inflater.inflate(R.layout.fragment_selected_item_details, container, false)
        setOnClickListener()
        return detailView
    }

    fun setDmSubCategory(dmSubCategory: DMSubCategory?) {
        this.dmSubCategory = dmSubCategory
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.fragmentActivity = context as FragmentActivity

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DMUtils.setValueToView(view.findViewById(R.id.menu_detail_header), dmSubCategory!!.itemName)
        getMenuDetail()
    }

    private fun getMenuDetail() {
        if (DMUtils.isOnline()) {
            detailView!!.findViewById<View>(R.id.progress_pager_menu).visibility = View.VISIBLE
            val url = (DMApiManager.METHOD_DETAIL + "itemid=" + dmSubCategory!!.itemId
                    + "&lid=" + DMUtils.getLanguageId(activity!!) + "&curid=" + DMUtils.getCurrencyId(fragmentActivity!!))
            val dmApiManager = DMApiManager(fragmentActivity!!)
            dmApiManager[url, object : JsonHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONArray?) {
                    super.onSuccess(statusCode, headers, response)
                    detailView!!.findViewById<View>(R.id.progress_pager_menu).visibility = View.GONE
                    try {
                        val dmMenuDetails = ArrayList<DMMenuDetail>()
                        val gson = Gson()
                        for (i in 0 until response!!.length() - 1) {
                            val responseJSon = response.getJSONObject(i)
                            val dmMenuDetail = gson.fromJson(responseJSon.toString(), DMMenuDetail::class.java)
                            dmMenuDetails.add(dmMenuDetail)
                        }
                        initViews(dmMenuDetails)
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

    private fun initViews(dmMenuDetails: ArrayList<DMMenuDetail>) {
        dmDataBaseHelper = DMDataBaseHelper(fragmentActivity)
        dmDataBaseHelper!!.openDataBase()
        dmSubCategoriesDB = dmDataBaseHelper!!.addedList
        val dmMenuDetail = dmMenuDetails[0]
        for (menuDetail in dmMenuDetails) {
            for (subCategory in dmSubCategoriesDB!!) {
                if (subCategory.itemId.equals(menuDetail.itemId, ignoreCase = true)) {
                    if (isAdded) {
                        (detailView!!.findViewById<View>(R.id.add_list_img) as ImageView).setImageResource(R.drawable.select_menu_icon_selected)
                        (detailView!!.findViewById<View>(R.id.add_list) as TextView).text = resources.getString(R.string.selected)
                        (detailView!!.findViewById<View>(R.id.add_list) as TextView).setTextColor(getColor(activity, R.color.green_bg))
                    }
                }
            }
        }
        DMUtils.setValueToView(detailView!!.findViewById(R.id.menu_item_name), dmMenuDetail.itemName)
        DMUtils.setValueToView(detailView!!.findViewById(R.id.menu_item_description), dmMenuDetail.itemDesc)
        if (isAdded) {
            val price = Integer.parseInt(dmSubCategory!!.itemPriceUsd)
            DMUtils.setValueToView(detailView!!.findViewById(R.id.menu_item_aed), DMUtils.getFormattedCurrencyString(DMUtils.getCurrencyCode(activity!!), price.toDouble()))
        }
        if (!TextUtils.isEmpty(dmMenuDetail.itemImage)) {
            DMUtils.setImageUrlToView(fragmentActivity, detailView!!.findViewById<View>(R.id.menu_item_image) as ImageView, dmMenuDetail.itemImage, detailView!!.findViewById<View>(R.id.progress_menu_image) as ProgressBar)
        }
        detailView!!.findViewById<View>(R.id.progress_layout).visibility = View.GONE
        detailView!!.findViewById<View>(R.id.menu_item_image).setOnClickListener { DMUtils.showImageDialog(activity, dmMenuDetail.itemImage) }
    }

    private fun setOnClickListener() {
        detailView!!.findViewById<View>(R.id.back_list).setOnClickListener(this)
        detailView!!.findViewById<View>(R.id.add_list_layout).setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.back_list -> (activity as DMHomeActivity).popStack()
            R.id.add_list_layout -> addToDb(dmSubCategory)
        }
    }

    fun addToDb(dmSubCategory: DMSubCategory?) {
        //        if (dmDataBaseHelper.hasObject(dmSubCategory.getItemId())) {
        //            if (isAdded()) {
        //                showToast(getResources().getString(R.string.already_added));
        //            }
        //        } else {
        try {
//            (activity as DMHomeActivity).setIsNeedRefresh(true)
            if (isAdded) {
                (detailView!!.findViewById<View>(R.id.add_list_img) as ImageView).setImageResource(R.drawable.select_menu_icon_selected)
                (detailView!!.findViewById<View>(R.id.add_list) as TextView).text = resources.getString(R.string.selected)
                (detailView!!.findViewById<View>(R.id.add_list) as TextView).setTextColor(getColor(activity, R.color.green_bg))
            }
            dmDataBaseHelper!!.insertData(dmSubCategory!!)
            (fragmentActivity as DMHomeActivity).showListButton()
        } catch (e: Exception) {
            e.printStackTrace()

        }

        //        }
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
