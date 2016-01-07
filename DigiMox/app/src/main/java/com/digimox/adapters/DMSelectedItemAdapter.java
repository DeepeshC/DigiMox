package com.digimox.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.digimox.R;
import com.digimox.activity.DMSelectedItemActivity;
import com.digimox.fragments.DMSelectedMenuFragment;
import com.digimox.models.response.DMSubCategory;
import com.digimox.utils.DMDataBaseHelper;
import com.digimox.utils.DMUtils;

import java.util.ArrayList;

/**
 * Created by Deepesh on 23-Aug-15.
 */
public class DMSelectedItemAdapter extends RecyclerView.Adapter<DMSelectedItemAdapter.ViewHolder> {
    private final TypedValue mTypedValue = new TypedValue();
    private Context context;
    private OnItemClickListener mItemClickListener;
    private static final int FOOTER = 0;
    private static final int NORMAL = 1;
    private int lastPosition = -1;
    private ArrayList<DMSubCategory> dmSubCategories;
    private DMSelectedItemActivity dmSelectedItemActivity;
    private DMSelectedMenuFragment dmSelectedMenuFragment;
    private int priceTotal = 0;
    private ViewHolder viewHolder;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView productImage;
        private View mView;
        private TextView clearList;
        private TextView productName;
        private TextView productDescription;
        private TextView productPrice;
        private ProgressBar subactegoryprogress;
        private LinearLayout removeOrder;
        private TextView grandTotal;
        private TextView grandTotalText;

        public ViewHolder(View view) {
            super(view);
            this.mView = view;
            view.setOnClickListener(this);
            productImage = (ImageView) view.findViewById(R.id.sub_category_image);
            clearList = (TextView) view.findViewById(R.id.clear_list);
            productName = (TextView) view.findViewById(R.id.sub_category_product_name);
            productDescription = (TextView) view.findViewById(R.id.sub_category_product_description);
            productPrice = (TextView) view.findViewById(R.id.sub_category_product_price);
            subactegoryprogress = (ProgressBar) view.findViewById(R.id.progress_subcategory);
            removeOrder = (LinearLayout) view.findViewById(R.id.remove_order);
            grandTotal = (TextView) view.findViewById(R.id.grand_total);
            grandTotalText = (TextView) view.findViewById(R.id.grand_total_text);

        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view, getPosition());
            }
        }
    }


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;
        if (dmSubCategories.get(position) == null) {
            viewType = FOOTER;
        } else {
            viewType = NORMAL;
        }
        return viewType;
    }

    public DMSelectedItemAdapter(Context context, ArrayList<DMSubCategory> dmSubCategories, DMSelectedMenuFragment dmSelectedMenuFragment) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        this.context = context;
        this.dmSubCategories = dmSubCategories;
        this.dmSelectedMenuFragment = dmSelectedMenuFragment;
        for (DMSubCategory subCategory : dmSubCategories) {
            if (null != subCategory) {
                int price = Integer.parseInt(subCategory.getItemPriceUsd());
                priceTotal = price + priceTotal;
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case FOOTER:
                ViewGroup footerView = (ViewGroup) mInflater.inflate(R.layout.layout_total, parent, false);
                return new ViewHolder(footerView);
            case NORMAL:
                ViewGroup normalGroup = (ViewGroup) mInflater.inflate(R.layout.category_selected_list_item, parent, false);
                return new ViewHolder(normalGroup);
            default:
                ViewGroup defaultGroup = (ViewGroup) mInflater.inflate(R.layout.category_selected_list_item, parent, false);
                return new ViewHolder(defaultGroup);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        this.viewHolder = holder;
        if (getItemViewType(position) == FOOTER) {
            holder.grandTotal.setText(String.valueOf(DMUtils.getFormattedCurrencyString(DMUtils.getCurrencyCode(context), priceTotal)));
            DMDataBaseHelper dmDataBaseHelper = new DMDataBaseHelper(context);
            dmDataBaseHelper.openDataBase();
            int count = dmDataBaseHelper.getAddedCount();
            if (count == 0) {
                holder.grandTotal.setVisibility(View.GONE);
                holder.grandTotalText.setVisibility(View.GONE);
            }
        } else {
            final DMSubCategory dmSubCategory = dmSubCategories.get(position);
            DMUtils.setValueToView(holder.productName, dmSubCategory.getItemName());
            DMUtils.setValueToView(holder.productDescription, dmSubCategory.getItemDesc());
            int price = Integer.parseInt(dmSubCategory.getItemPriceUsd());

            DMUtils.setValueToView(holder.productPrice, DMUtils.getFormattedCurrencyString(DMUtils.getCurrencyCode(context), price));
            if (!TextUtils.isEmpty(dmSubCategory.getItemImage())) {
                DMUtils.setImageUrlToView(context, holder.productImage, dmSubCategory.getItemImage(), R.drawable.product_place_holder, holder.subactegoryprogress);


            }
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dmSelectedMenuFragment.showItemDetails(dmSubCategory);
                }
            });
            holder.removeOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeItem(dmSubCategory, position, holder);
                }
            });
            holder.productImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DMUtils.showImageDialog(context, dmSubCategory.getItemImage());
                }
            });

        }
    }

    private void removeItem(DMSubCategory dmSubCategory, int position, ViewHolder holder) {
        priceTotal = 0;

        DMDataBaseHelper dmDataBaseHelper = new DMDataBaseHelper(context);
        dmDataBaseHelper.openDataBase();
        dmDataBaseHelper.deleteRecord(dmSubCategory.getItemId());
        dmSelectedMenuFragment.setMenuCount();
        dmSubCategories.remove(position);
        for (DMSubCategory subCategory : dmSubCategories) {
            if (null != subCategory) {
                int price = Integer.parseInt(subCategory.getItemPriceUsd());
                priceTotal = price + priceTotal;
            }
        }
        notifyItemRemoved(position);

        notifyDataSetChanged();
    }

    public void remove() {
        dmSubCategories.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dmSubCategories.size();
    }

}

