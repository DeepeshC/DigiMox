package com.digimox.activity

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.digimox.R
import com.digimox.adapters.DMSelectedItemAdapter
import com.digimox.app.DMAppConstants
import com.digimox.models.response.DMSubCategory
import com.digimox.utils.DMDataBaseHelper
import com.digimox.utils.DMDividerItemDecoration
import com.digimox.utils.DMUtils
import java.util.*

/**
 * Created by Deepesh on 20-Dec-15.
 */
class DMSelectedItemActivity : DMBaseActivity(), View.OnClickListener {
    private var subCategoryList: RecyclerView? = null
    private var dmSubCategories: ArrayList<DMSubCategory>? = null
    private var dmDataBaseHelper: DMDataBaseHelper? = null
    private val dmSelectedItemAdapter: DMSelectedItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_list)
        dmSubCategories = intent.getSerializableExtra("SELECTED_ITEM") as ArrayList<DMSubCategory>
        initViews()
        setOnClickListener()
        if (null != dmSubCategories && dmSubCategories!!.size > 0) {
            initList(dmSubCategories)
        }

    }

    private fun initViews() {
        subCategoryList = findViewById<View>(R.id.sub_category_list) as RecyclerView
        subCategoryList!!.setHasFixedSize(true)
        checkOrientation()
        subCategoryList!!.addItemDecoration(
                DMDividerItemDecoration(this, R.drawable.sublist_divider))
    }

    private fun checkOrientation() {
        val orientation = DMUtils.getScreenOrientation(this)
        if (DMAppConstants.PORTRAIT == orientation) {
            subCategoryList!!.layoutManager = GridLayoutManager(this, 1)
        } else {
            subCategoryList!!.layoutManager = GridLayoutManager(this, 2)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val orientation = newConfig.orientation
        when (orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> subCategoryList!!.layoutManager = GridLayoutManager(this, 2)
            Configuration.ORIENTATION_PORTRAIT -> subCategoryList!!.layoutManager = GridLayoutManager(this, 1)
        }
    }

    private fun initList(dmSubCategories: ArrayList<DMSubCategory>?) {
        //        dmSelectedItemAdapter = new DMSelectedItemAdapter(this, dmSubCategories);
        //        subCategoryList.setAdapter(dmSelectedItemAdapter);
    }

    fun clearData() {
        dmDataBaseHelper = DMDataBaseHelper(this)
        dmDataBaseHelper!!.openDataBase()
        dmDataBaseHelper!!.deleteTable()
    }

    private fun setOnClickListener() {
        findViewById<View>(R.id.clear_list).setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.clear_list -> if (null != dmSelectedItemAdapter) {
                clearData()
                dmSelectedItemAdapter.remove()
                findViewById<View>(R.id.clear_list).visibility = View.GONE
                findViewById<View>(R.id.no_item).visibility = View.VISIBLE
            }
        }

    }
}
