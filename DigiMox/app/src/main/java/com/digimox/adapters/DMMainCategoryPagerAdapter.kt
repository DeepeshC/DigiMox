package com.digimox.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

import java.util.ArrayList

/**
 * Created by Deepesh on 10-Dec-15.
 */
class DMMainCategoryPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    private val mFragmentList: MutableList<Fragment>

    init {
        mFragmentList = ArrayList()
    }

    fun addFragment(position: Int, fragment: Fragment) {
        mFragmentList.add(position, fragment)
    }

    fun getFragment(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return ""
    }
}
