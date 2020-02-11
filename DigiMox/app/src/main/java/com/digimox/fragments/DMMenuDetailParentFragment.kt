package com.digimox.fragments

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.digimox.R
import com.digimox.adapters.DMDetailParentPagerAdapter
import com.digimox.models.response.DMSubCategory
import com.digimox.utils.DMUtils

import java.util.ArrayList

/**
 * Created by Deepesh on 13-Dec-15.
 */
class DMMenuDetailParentFragment : DMBaseFragment(), View.OnClickListener {
    private var itemView: View? = null
    private var dmSubCategory: DMSubCategory? = null
    private var dmSubCategories: ArrayList<DMSubCategory>? = null
    private var position: Int = 0
    private var detailPager: ViewPager? = null
    private var dmDetailParentPagerAdapter: DMDetailParentPagerAdapter? = null

    fun setPosition(position: Int) {
        this.position = position
    }

    fun setDmSubCategory(dmSubCategory: DMSubCategory) {
        this.dmSubCategory = dmSubCategory
    }

    fun setDmSubCategories(dmSubCategories: ArrayList<DMSubCategory>) {
        this.dmSubCategories = dmSubCategories
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        itemView = inflater.inflate(R.layout.menu_detail, container, false)
        initViews()
        return itemView
    }

    fun initViews() {
        setOnClickListener()
        //        ((DMHomeActivity) getActivity()).showBackButton();
        DMUtils.setValueToView(itemView!!.findViewById(R.id.menu_detail_header), dmSubCategory!!.itemName)
        detailPager = itemView!!.findViewById<View>(R.id.menu_detail_pager) as ViewPager
        if (position == dmSubCategories!!.size - 1) {
            itemView!!.findViewById<View>(R.id.menu_detail_header_next_arrow).visibility = View.INVISIBLE
        }
        if (0 == position) {
            itemView!!.findViewById<View>(R.id.menu_detail_header_previous_arrow).visibility = View.INVISIBLE
        }

        dmDetailParentPagerAdapter = DMDetailParentPagerAdapter(childFragmentManager)
        for (i in dmSubCategories!!.indices) {
            dmSubCategory = dmSubCategories!![i]
            val dmItemDetailFragment = DMItemDetailFragment()
            dmItemDetailFragment.setDmSubCategory(dmSubCategory!!)
            dmDetailParentPagerAdapter!!.addFragment(dmItemDetailFragment)
        }
        detailPager!!.adapter = dmDetailParentPagerAdapter
        detailPager!!.currentItem = position
        detailPager!!.offscreenPageLimit = dmSubCategories!!.size
        viewpagerPageListener()

    }

    private fun setOnClickListener() {
        itemView!!.findViewById<View>(R.id.menu_detail_header_next_arrow).setOnClickListener(this)
        itemView!!.findViewById<View>(R.id.menu_detail_header_previous_arrow).setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.menu_detail_header_next_arrow -> showNext()
            R.id.menu_detail_header_previous_arrow -> showPrevious()
        }
    }

    private fun showNext() {
        position = position + 1
        if (position < dmSubCategories!!.size) {
            detailPager!!.currentItem = position
        }
    }

    private fun showPrevious() {
        position = position - 1
        if (position >= 0) {
            detailPager!!.currentItem = position
        }
    }

    private fun viewpagerPageListener() {
        detailPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                setPosition(position)
                if (position == 0) {
                    itemView!!.findViewById<View>(R.id.menu_detail_header_previous_arrow).visibility = View.INVISIBLE
                    itemView!!.findViewById<View>(R.id.menu_detail_header_next_arrow).visibility = View.VISIBLE
                    itemView!!.findViewById<View>(R.id.menu_detail_header).visibility = View.VISIBLE
                    DMUtils.setValueToView(itemView!!.findViewById(R.id.sub_category_header_next), dmSubCategories!![position + 1].itemName)

                } else if (position == dmSubCategories!!.size - 1) {
                    itemView!!.findViewById<View>(R.id.menu_detail_header_next_arrow).visibility = View.INVISIBLE
                    itemView!!.findViewById<View>(R.id.menu_detail_header_previous_arrow).visibility = View.VISIBLE
                    DMUtils.setValueToView(itemView!!.findViewById(R.id.menu_detail_header), dmSubCategories!![position].itemName)
                } else {
                    itemView!!.findViewById<View>(R.id.menu_detail_header_next_arrow).visibility = View.VISIBLE
                    itemView!!.findViewById<View>(R.id.menu_detail_header_previous_arrow).visibility = View.VISIBLE
                    itemView!!.findViewById<View>(R.id.menu_detail_header).visibility = View.VISIBLE
                    DMUtils.setValueToView(itemView!!.findViewById(R.id.menu_detail_header), dmSubCategories!![position].itemName)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }
}
