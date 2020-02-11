package com.digimox.fragments

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.digimox.R
import com.digimox.activity.DMHomeActivity
import com.digimox.adapters.DMSelectedItemAdapter
import com.digimox.app.DMAppConstants
import com.digimox.models.response.DMSubCategory
import com.digimox.utils.DMDataBaseHelper
import com.digimox.utils.DMUtils

import java.util.ArrayList
import java.util.Collections

/**
 * Created by Deepesh on 31-Dec-15.
 */
class DMSelectedMenuFragment : DMBaseFragment(), View.OnClickListener {
    private var selectedView: View? = null
    private var subCategoryList: RecyclerView? = null
    private var dmSubCategories: ArrayList<DMSubCategory?>? = null
    private var dmDataBaseHelper: DMDataBaseHelper? = null
    private var dmSelectedItemAdapter: DMSelectedItemAdapter? = null

    fun setDmSubCategories(dmSubCategories: ArrayList<DMSubCategory?>?) {
        this.dmSubCategories = dmSubCategories
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        selectedView = inflater.inflate(R.layout.activity_selected_list, container, false)
        initViews()
        setOnClickListener()
        //        ((DMHomeActivity) getActivity()).showBackButton();
        if (null != dmSubCategories && dmSubCategories!!.size > 0) {
            initList(dmSubCategories!!)
        }
        return selectedView
    }


    private fun initViews() {
        subCategoryList = selectedView!!.findViewById<View>(R.id.sub_category_list) as RecyclerView
        subCategoryList!!.setHasFixedSize(true)
        checkOrientation()
        //        subCategoryList.addItemDecoration(
        //                new DMDividerItemDecoration(getActivity(), R.drawable.sublist_divider));
        setMenuCount()
    }

    private fun checkOrientation() {
        val orientation = DMUtils.getScreenOrientation(activity)
        if (DMAppConstants.PORTRAIT == orientation) {
            subCategoryList!!.layoutManager = GridLayoutManager(activity, 1)
        } else {
            subCategoryList!!.layoutManager = GridLayoutManager(activity, 2)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val orientation = newConfig.orientation
        when (orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> subCategoryList!!.layoutManager = GridLayoutManager(activity, 2)
            Configuration.ORIENTATION_PORTRAIT -> subCategoryList!!.layoutManager = GridLayoutManager(activity, 1)
        }
    }

    private fun initList(dmSubCategories: ArrayList<DMSubCategory?>) {
        dmSubCategories.filterNotNull()
        dmSubCategories.add(null)
        dmSelectedItemAdapter = DMSelectedItemAdapter(activity!!, dmSubCategories, this)
        subCategoryList!!.adapter = dmSelectedItemAdapter
    }

    fun clearData() {
        dmDataBaseHelper = DMDataBaseHelper(activity)
        dmDataBaseHelper!!.openDataBase()
        dmDataBaseHelper!!.deleteTable()
        dmDataBaseHelper!!.close()
        (activity as DMHomeActivity).setMeuCount("0")
    }

    fun setMenuCount() {
        dmDataBaseHelper = DMDataBaseHelper(activity)
        dmDataBaseHelper!!.openDataBase()
        dmSubCategories = dmDataBaseHelper!!.addedList
        val count = dmSubCategories!!.size
        (activity as DMHomeActivity).setMeuCount(count.toString())
        dmDataBaseHelper!!.close()
        if (count == 0) {
            selectedView!!.findViewById<View>(R.id.no_item).visibility = View.VISIBLE
        }
    }

    private fun setOnClickListener() {
        selectedView!!.findViewById<View>(R.id.clear_list).setOnClickListener(this)

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.clear_list -> if (null != dmSelectedItemAdapter) {
                clearData()
                dmSelectedItemAdapter!!.remove()
                selectedView!!.findViewById<View>(R.id.no_item).visibility = View.VISIBLE
            }
        }

    }

    fun showItemDetails(dmSubCategory: DMSubCategory?) {
        if (null != activity) {
            val fm = activity!!.supportFragmentManager
            if (null != fm) {
                val dmSelectedItemDetailFragment = DMSelectedItemDetailFragment()
                dmSelectedItemDetailFragment.setDmSubCategory(dmSubCategory)
                val ft = fm.beginTransaction()
                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_left)
                        .add(R.id.fragment_container, dmSelectedItemDetailFragment, "selectedItemDetailsFragment")
                        .addToBackStack("selectedItemDetailsFragment")
                        .commit()
            }
        }
    }
}

