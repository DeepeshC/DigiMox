package com.digimox.adapters

import android.content.Context
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.digimox.R
import com.digimox.fragments.DMMainCategorySubFragment
import com.digimox.models.response.DMMainCategory
import com.digimox.utils.DMUtils
import java.util.*

/**
 * Created by Deepesh on 23-Aug-15.
 */
class DMCategorySubAdapter(private val context: Context, private val dmMainCategorySubFragment: DMMainCategorySubFragment, private val dmMainCategories: ArrayList<DMMainCategory>) : RecyclerView.Adapter<DMCategorySubAdapter.ViewHolder>() {
    private val mTypedValue = TypedValue()
    private var mItemClickListener: OnItemClickListener? = null

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView), View.OnClickListener {
        val categoryImage: ImageView
        val categoryName: TextView
        val mainCategoryProgress: ProgressBar

        init {
            mView.setOnClickListener(this)
            categoryImage = mView.findViewById<View>(R.id.categoryImage) as ImageView
            categoryName = mView.findViewById<View>(R.id.category_name) as TextView
            mainCategoryProgress = mView.findViewById<View>(R.id.progress_main_category) as ProgressBar

        }

        override fun onClick(view: View) {
            if (mItemClickListener != null) {
                mItemClickListener!!.onItemClick(view, position)
            }
        }
    }


    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.mItemClickListener = mItemClickListener
    }

    init {
        context.theme.resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mInflater = LayoutInflater.from(parent.context)
        val defaultGroup = mInflater.inflate(R.layout.category_list_item, parent, false) as ViewGroup
        return ViewHolder(defaultGroup)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dmMainCategory = dmMainCategories[position]
        if (!TextUtils.isEmpty(dmMainCategory.groupImage)) {
            DMUtils.setImageUrlToView(context, holder.categoryImage, dmMainCategory.groupImage, holder.mainCategoryProgress)
        }
        holder.categoryName.text = dmMainCategory.groupName
        holder.mView.setOnClickListener { goToSubCategory(dmMainCategory, position) }

    }

    private fun goToSubCategory(dmMainCategory: DMMainCategory, position: Int) {
        dmMainCategorySubFragment.clickMainCategory(dmMainCategories, dmMainCategory, position)
    }

    override fun getItemCount(): Int {
        return dmMainCategories.size
    }

}

