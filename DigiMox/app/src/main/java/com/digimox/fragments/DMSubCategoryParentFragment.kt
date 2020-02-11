package com.digimox.fragments

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.digimox.R
import com.digimox.adapters.DMMainCategoryPagerAdapter
import com.digimox.models.response.DMMainCategory
import com.digimox.utils.DMUtils

import java.util.ArrayList

/**
 * Created by Deepesh on 10-Dec-15.
 */
class DMSubCategoryParentFragment : DMBaseFragment(), View.OnClickListener {
    private var itemView: View? = null
    private var dmMainCategories: ArrayList<DMMainCategory>? = null
    private var dmMainCategory: DMMainCategory? = null
    private var position: Int = 0
    private var categoryPager: ViewPager? = null
    private var dmMainCategoryPagerAdapter: DMMainCategoryPagerAdapter? = null

    fun setPosition(position: Int) {
        this.position = position
    }

    fun setDmMainCategory(dmMainCategory: DMMainCategory) {
        this.dmMainCategory = dmMainCategory
    }

    fun setDmMainCategories(dmMainCategories: ArrayList<DMMainCategory>) {
        this.dmMainCategories = dmMainCategories
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        itemView = inflater.inflate(R.layout.fragment_sub_category_parent, container, false)
        initViews()

        return itemView
    }

    fun initViews() {
        setOnClickListener()
        //        ((DMHomeActivity) getActivity()).showBackButton();
        DMUtils.setValueToView(itemView!!.findViewById(R.id.sub_category_header), dmMainCategory!!.groupName)
        categoryPager = itemView!!.findViewById<View>(R.id.main_category_pager) as ViewPager
        if (position == dmMainCategories!!.size - 1) {
            itemView!!.findViewById<View>(R.id.sub_category_header_next).visibility = View.INVISIBLE
            itemView!!.findViewById<View>(R.id.sub_category_header_next_arrow).visibility = View.INVISIBLE
        } else {
            DMUtils.setValueToView(itemView!!.findViewById(R.id.sub_category_header_next), dmMainCategories!![position + 1].groupName)
        }
        if (0 == position) {
            itemView!!.findViewById<View>(R.id.sub_category_header_previous_arrow).visibility = View.INVISIBLE
        }

        dmMainCategoryPagerAdapter = DMMainCategoryPagerAdapter(childFragmentManager)
        for (i in dmMainCategories!!.indices) {
            dmMainCategory = dmMainCategories!![i]
            val dmSubCategoryFragment = DMSubCategoryFragment(Integer.parseInt(dmMainCategory!!.groupId))
            dmSubCategoryFragment.setDmMainCategory(dmMainCategory!!)
            dmMainCategoryPagerAdapter!!.addFragment(i, dmSubCategoryFragment)
        }
        categoryPager!!.adapter = dmMainCategoryPagerAdapter
        categoryPager!!.currentItem = position
        categoryPager!!.offscreenPageLimit = dmMainCategories!!.size
        viewpagerPageListener()
    }

    private fun setOnClickListener() {
        itemView!!.findViewById<View>(R.id.sub_category_header_next_arrow).setOnClickListener(this)
        itemView!!.findViewById<View>(R.id.sub_category_header_previous_arrow).setOnClickListener(this)
        itemView!!.findViewById<View>(R.id.sub_category_header_next).setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.sub_category_header_next_arrow -> showNext()
            R.id.sub_category_header_next -> showNext()
            R.id.sub_category_header_previous_arrow -> showPrevious()
        }
    }

    private fun showNext() {
        position = position + 1
        if (position < dmMainCategories!!.size) {
            categoryPager!!.currentItem = position
        }
    }

    private fun showPrevious() {
        position = position - 1
        if (position >= 0) {
            categoryPager!!.currentItem = position
        }
    }

    private fun viewpagerPageListener() {
        categoryPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                setPosition(position)
                if (position == 0) {
                    itemView!!.findViewById<View>(R.id.sub_category_header_previous_arrow).visibility = View.INVISIBLE
                    itemView!!.findViewById<View>(R.id.sub_category_header_next_arrow).visibility = View.VISIBLE
                    itemView!!.findViewById<View>(R.id.sub_category_header_next).visibility = View.VISIBLE
                    itemView!!.findViewById<View>(R.id.sub_category_header).visibility = View.VISIBLE
                    DMUtils.setValueToView(itemView!!.findViewById(R.id.sub_category_header_next), dmMainCategories!![position + 1].groupName)
                    DMUtils.setValueToView(itemView!!.findViewById(R.id.sub_category_header), dmMainCategories!![position].groupName)
                } else if (position == dmMainCategories!!.size - 1) {
                    itemView!!.findViewById<View>(R.id.sub_category_header_next).visibility = View.INVISIBLE
                    itemView!!.findViewById<View>(R.id.sub_category_header_next_arrow).visibility = View.INVISIBLE
                    itemView!!.findViewById<View>(R.id.sub_category_header_previous_arrow).visibility = View.VISIBLE
                    DMUtils.setValueToView(itemView!!.findViewById(R.id.sub_category_header), dmMainCategories!![position].groupName)
                } else {
                    itemView!!.findViewById<View>(R.id.sub_category_header_next_arrow).visibility = View.VISIBLE
                    itemView!!.findViewById<View>(R.id.sub_category_header_previous_arrow).visibility = View.VISIBLE
                    itemView!!.findViewById<View>(R.id.sub_category_header_next).visibility = View.VISIBLE
                    itemView!!.findViewById<View>(R.id.sub_category_header).visibility = View.VISIBLE
                    DMUtils.setValueToView(itemView!!.findViewById(R.id.sub_category_header_next), dmMainCategories!![position + 1].groupName)
                    DMUtils.setValueToView(itemView!!.findViewById(R.id.sub_category_header), dmMainCategories!![position].groupName)
                }
                //                if (null != dmMainCategoryPagerAdapter) {
                //                    DMSubCategoryFragment dmSubCategoryFragment = (DMSubCategoryFragment) dmMainCategoryPagerAdapter.getFragment(position);
                //                    dmSubCategoryFragment.getSubCategory();
                //                }

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }
}
