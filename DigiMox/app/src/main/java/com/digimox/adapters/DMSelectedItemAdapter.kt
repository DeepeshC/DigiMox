package com.digimox.adapters

import android.content.Context
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.digimox.R
import com.digimox.activity.DMSelectedItemActivity
import com.digimox.fragments.DMSelectedMenuFragment
import com.digimox.models.response.DMSubCategory
import com.digimox.utils.DMDataBaseHelper
import com.digimox.utils.DMUtils
import java.util.*

/**
 * Created by Deepesh on 23-Aug-15.
 */
class DMSelectedItemAdapter(private val context: Context, private val dmSubCategories: ArrayList<DMSubCategory?>, private val dmSelectedMenuFragment: DMSelectedMenuFragment) : RecyclerView.Adapter<DMSelectedItemAdapter.ViewHolder>() {
    private val mTypedValue = TypedValue()
    private var mItemClickListener: OnItemClickListener? = null
    private val lastPosition = -1
    private val dmSelectedItemActivity: DMSelectedItemActivity? = null
    private var priceTotal = 0
    private var viewHolder: ViewHolder? = null

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView), View.OnClickListener {
        val productImage: ImageView
        val clearList: TextView
        val productName: TextView
        val productDescription: TextView
        val productPrice: TextView
        val subactegoryprogress: ProgressBar
        val removeOrder: LinearLayout
        val grandTotal: TextView
        val grandTotalText: TextView

        init {
            mView.setOnClickListener(this)
            productImage = mView.findViewById<View>(R.id.sub_category_image) as ImageView
            clearList = mView.findViewById<View>(R.id.clear_list) as TextView
            productName = mView.findViewById<View>(R.id.sub_category_product_name) as TextView
            productDescription = mView.findViewById<View>(R.id.sub_category_product_description) as TextView
            productPrice = mView.findViewById<View>(R.id.sub_category_product_price) as TextView
            subactegoryprogress = mView.findViewById<View>(R.id.progress_subcategory) as ProgressBar
            removeOrder = mView.findViewById<View>(R.id.remove_order) as LinearLayout
            grandTotal = mView.findViewById<View>(R.id.grand_total) as TextView
            grandTotalText = mView.findViewById<View>(R.id.grand_total_text) as TextView

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

    override fun getItemViewType(position: Int): Int {
        val viewType: Int
        if (dmSubCategories[position] == null) {
            viewType = FOOTER
        } else {
            viewType = NORMAL
        }
        return viewType
    }

    init {
        context.theme.resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true)
        for (subCategory in dmSubCategories) {
            if (null != subCategory) {
                val price = Integer.parseInt(subCategory.itemPriceUsd)
                priceTotal = price + priceTotal
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mInflater = LayoutInflater.from(parent.context)
        when (viewType) {
            FOOTER -> {
                val footerView = mInflater.inflate(R.layout.layout_total, parent, false) as ViewGroup
                return ViewHolder(footerView)
            }
            NORMAL -> {
                val normalGroup = mInflater.inflate(R.layout.category_selected_list_item, parent, false) as ViewGroup
                return ViewHolder(normalGroup)
            }
            else -> {
                val defaultGroup = mInflater.inflate(R.layout.category_selected_list_item, parent, false) as ViewGroup
                return ViewHolder(defaultGroup)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        this.viewHolder = holder
        if (getItemViewType(position) == FOOTER) {
            holder.grandTotal.text = DMUtils.getFormattedCurrencyString(DMUtils.getCurrencyCode(context), priceTotal.toDouble()).toString()
            val dmDataBaseHelper = DMDataBaseHelper(context)
            dmDataBaseHelper.openDataBase()
            val count = dmDataBaseHelper.addedCount
            if (count == 0) {
                holder.grandTotal.visibility = View.GONE
                holder.grandTotalText.visibility = View.GONE
            }
        } else {
            val dmSubCategory = dmSubCategories[position]
            DMUtils.setValueToView(holder.productName, dmSubCategory?.itemName)
            DMUtils.setValueToView(holder.productDescription, dmSubCategory?.itemDesc)
            val price = Integer.parseInt(dmSubCategory?.itemPriceUsd?:"0")

            DMUtils.setValueToView(holder.productPrice, DMUtils.getFormattedCurrencyString(DMUtils.getCurrencyCode(context), price.toDouble()))
            if (!TextUtils.isEmpty(dmSubCategory?.itemImage)) {
                DMUtils.setImageUrlToView(context, holder.productImage, dmSubCategory?.itemImage, R.drawable.product_place_holder, holder.subactegoryprogress)


            }
            holder.mView.setOnClickListener { dmSelectedMenuFragment.showItemDetails(dmSubCategory) }
            holder.removeOrder.setOnClickListener { removeItem(dmSubCategory, position, holder) }
            holder.productImage.setOnClickListener { DMUtils.showImageDialog(context, dmSubCategory?.itemImage) }

        }
    }

    private fun removeItem(dmSubCategory: DMSubCategory?, position: Int, holder: ViewHolder) {
        priceTotal = 0

        val dmDataBaseHelper = DMDataBaseHelper(context)
        dmDataBaseHelper.openDataBase()
        dmDataBaseHelper.deleteRecord(dmSubCategory?.id)
        dmSelectedMenuFragment.setMenuCount()
        dmSubCategories.removeAt(position)
        for (subCategory in dmSubCategories) {
            if (null != subCategory) {
                val price = Integer.parseInt(subCategory.itemPriceUsd)
                priceTotal = price + priceTotal
            }
        }
        notifyItemRemoved(position)

        notifyDataSetChanged()
    }

    fun remove() {
        dmSubCategories.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dmSubCategories.size
    }

    companion object {
        private val FOOTER = 0
        private val NORMAL = 1
    }

}

