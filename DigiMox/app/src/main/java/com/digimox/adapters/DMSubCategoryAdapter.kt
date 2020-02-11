package com.digimox.adapters

import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.digimox.R
import com.digimox.fragments.DMSubCategoryFragment
import com.digimox.models.response.DMSubCategory
import com.digimox.utils.DMDataBaseHelper
import com.digimox.utils.DMUtils
import java.util.*

/**
 * Created by Deepesh on 23-Aug-15.
 */
class DMSubCategoryAdapter(//    private ArrayList<AQProductModel> aqProductModels;
        private val context: Context, private val dmSubCategoryFragment: DMSubCategoryFragment, private val dmSubCategories: ArrayList<DMSubCategory>) : RecyclerView.Adapter<DMSubCategoryAdapter.ViewHolder>() {
    private val mTypedValue = TypedValue()
    private val mBackground: Int
    private var mItemClickListener: OnItemClickListener? = null
    private val lastPosition = -1
    private val dmSubCategoriesDB: ArrayList<DMSubCategory>
    private val dmDataBaseHelper: DMDataBaseHelper

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView), View.OnClickListener {
        val productImage: ImageView
        val backImage: ImageView
        val addList: TextView
        val addListImage: ImageView
        val productName: TextView
        val animationText: TextView
        val productDescription: TextView
        val productPrice: TextView
        val subactegoryprogress: ProgressBar

        init {
            mView.setOnClickListener(this)
            productImage = mView.findViewById<View>(R.id.sub_category_image) as ImageView
            backImage = mView.findViewById<View>(R.id.back_list) as ImageView
            addList = mView.findViewById<View>(R.id.add_list) as TextView
            addListImage = mView.findViewById<View>(R.id.add_list_img) as ImageView
            productName = mView.findViewById<View>(R.id.sub_category_product_name) as TextView
            productDescription = mView.findViewById<View>(R.id.sub_category_product_description) as TextView
            productPrice = mView.findViewById<View>(R.id.sub_category_product_price) as TextView
            animationText = mView.findViewById<View>(R.id.animation_text) as TextView
            subactegoryprogress = mView.findViewById<View>(R.id.progress_subcategory) as ProgressBar

        }

        override fun onClick(view: View) {
            if (mItemClickListener != null) {
                mItemClickListener!!.onItemClick(view, position)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val viewType: Int
        if (position == dmSubCategories.size) {
            viewType = FOOTER
        } else {
            viewType = NORMAL
        }
        return viewType
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.mItemClickListener = mItemClickListener
    }

    init {
        context.theme.resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true)
        mBackground = mTypedValue.resourceId
        dmDataBaseHelper = DMDataBaseHelper(context)
        dmDataBaseHelper.openDataBase()
        dmSubCategoriesDB = dmDataBaseHelper.addedList
        dmDataBaseHelper.close()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mInflater = LayoutInflater.from(parent.context)
        when (viewType) {
            FOOTER -> {
                val footerView = mInflater.inflate(R.layout.layout_close, parent, false) as ViewGroup
                return ViewHolder(footerView)
            }
            NORMAL -> {
                val normalGroup = mInflater.inflate(R.layout.category_sub_list_item, parent, false) as ViewGroup
                return ViewHolder(normalGroup)
            }
            else -> {
                val defaultGroup = mInflater.inflate(R.layout.category_sub_list_item, parent, false) as ViewGroup
                return ViewHolder(defaultGroup)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (getItemViewType(position) == FOOTER) {
            holder.backImage.setOnClickListener { dmSubCategoryFragment.back() }
        } else {
            val dmSubCategory = dmSubCategories[position]
            for (subCategory in dmSubCategoriesDB) {
                if (subCategory.itemId.equals(dmSubCategory.itemId, ignoreCase = true)) {
                    holder.addListImage.setImageResource(R.drawable.select_menu_icon_selected)
                    holder.addList.text = context.resources.getString(R.string.selected)
                    holder.addList.setTextColor(getColor(context, R.color.green_bg))
                }
            }
            holder.animationText.visibility = View.GONE
            DMUtils.setValueToView(holder.productName, dmSubCategory.itemName)
            DMUtils.setValueToView(holder.productDescription, dmSubCategory.itemDesc)
            val price = Integer.parseInt(dmSubCategory.itemPriceUsd)
            DMUtils.setValueToView(holder.productPrice, DMUtils.getFormattedCurrencyString(DMUtils.getCurrencyCode(context), price.toDouble()))
            if (!TextUtils.isEmpty(dmSubCategory.itemImage)) {
                DMUtils.setImageUrlToView(context, holder.productImage, dmSubCategory.itemImage, R.drawable.product_place_holder, holder.subactegoryprogress)
            }
            holder.mView.setOnClickListener { dmSubCategoryFragment.showItemDetails(dmSubCategory, dmSubCategories, position) }
            holder.addList.setOnClickListener { dmSubCategoryFragment.addToDb(holder.addListImage, holder.addList, dmSubCategory) }
            holder.addListImage.setOnClickListener { dmSubCategoryFragment.addToDb(holder.addListImage, holder.addList, dmSubCategory) }
            holder.productImage.setOnClickListener { DMUtils.showImageDialog(context, dmSubCategory.itemImage) }
        }


    }

    override fun getItemCount(): Int {
        return dmSubCategories.size
    }

    companion object {
        //    private int[] categoryImage = {R.drawable.sub_item, R.drawable.cat_2, R.drawable.cat_3, R.drawable.cat_4, R.drawable.cat_5, R.drawable.cat_6, R.drawable.cat_7, R.drawable.cat_6, R.drawable.cat_7};
        private val FOOTER = 0
        private val NORMAL = 1

        fun getColor(context: Context, id: Int): Int {
            val version = Build.VERSION.SDK_INT
            return if (version >= 23) {
                ContextCompat.getColor(context, id)
            } else {
                context.resources.getColor(id)
            }
        }
    }

}

