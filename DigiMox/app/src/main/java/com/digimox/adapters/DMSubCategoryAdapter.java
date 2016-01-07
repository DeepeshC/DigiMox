package com.digimox.adapters;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.digimox.R;
import com.digimox.fragments.DMSubCategoryFragment;
import com.digimox.models.response.DMSubCategory;
import com.digimox.utils.DMDataBaseHelper;
import com.digimox.utils.DMUtils;

import java.util.ArrayList;

/**
 * Created by Deepesh on 23-Aug-15.
 */
public class DMSubCategoryAdapter extends RecyclerView.Adapter<DMSubCategoryAdapter.ViewHolder> {
    private final TypedValue mTypedValue = new TypedValue();
    private DMSubCategoryFragment dmSubCategoryFragment;
    private int mBackground;
    //    private ArrayList<AQProductModel> aqProductModels;
    private Context context;
    private OnItemClickListener mItemClickListener;
    //    private int[] categoryImage = {R.drawable.sub_item, R.drawable.cat_2, R.drawable.cat_3, R.drawable.cat_4, R.drawable.cat_5, R.drawable.cat_6, R.drawable.cat_7, R.drawable.cat_6, R.drawable.cat_7};
    private static final int FOOTER = 0;
    private static final int NORMAL = 1;
    private int lastPosition = -1;
    private ArrayList<DMSubCategory> dmSubCategories;
    private ArrayList<DMSubCategory> dmSubCategoriesDB;
    private DMDataBaseHelper dmDataBaseHelper;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView productImage;
        private View mView;
        private ImageView backImage;
        private TextView addList;
        private ImageView addListImage;
        private TextView productName;
        private TextView animationText;
        private TextView productDescription;
        private TextView productPrice;
        private ProgressBar subactegoryprogress;

        public ViewHolder(View view) {
            super(view);
            this.mView = view;
            view.setOnClickListener(this);
            productImage = (ImageView) view.findViewById(R.id.sub_category_image);
            backImage = (ImageView) view.findViewById(R.id.back_list);
            addList = (TextView) view.findViewById(R.id.add_list);
            addListImage = (ImageView) view.findViewById(R.id.add_list_img);
            productName = (TextView) view.findViewById(R.id.sub_category_product_name);
            productDescription = (TextView) view.findViewById(R.id.sub_category_product_description);
            productPrice = (TextView) view.findViewById(R.id.sub_category_product_price);
            animationText = (TextView) view.findViewById(R.id.animation_text);
            subactegoryprogress = (ProgressBar) view.findViewById(R.id.progress_subcategory);

        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view, getPosition());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;
        if (position == dmSubCategories.size()) {
            viewType = FOOTER;
        } else {
            viewType = NORMAL;
        }
        return viewType;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public DMSubCategoryAdapter(Context context, DMSubCategoryFragment dmSubCategoryFragment, ArrayList<DMSubCategory> dmSubCategories) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        this.context = context;
        this.dmSubCategoryFragment = dmSubCategoryFragment;
        this.dmSubCategories = dmSubCategories;
        dmDataBaseHelper = new DMDataBaseHelper(context);
        dmDataBaseHelper.openDataBase();
        dmSubCategoriesDB = dmDataBaseHelper.getAddedList();
        dmDataBaseHelper.close();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case FOOTER:
                ViewGroup footerView = (ViewGroup) mInflater.inflate(R.layout.layout_close, parent, false);
                return new ViewHolder(footerView);
            case NORMAL:
                ViewGroup normalGroup = (ViewGroup) mInflater.inflate(R.layout.category_sub_list_item, parent, false);
                return new ViewHolder(normalGroup);
            default:
                ViewGroup defaultGroup = (ViewGroup) mInflater.inflate(R.layout.category_sub_list_item, parent, false);
                return new ViewHolder(defaultGroup);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (getItemViewType(position) == FOOTER) {
            holder.backImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dmSubCategoryFragment.back();
                }
            });
        } else {
            final DMSubCategory dmSubCategory = dmSubCategories.get(position);
            for (DMSubCategory subCategory : dmSubCategoriesDB) {
                if (subCategory.getItemId().equalsIgnoreCase(dmSubCategory.getItemId())) {
                    holder.addListImage.setImageResource(R.drawable.select_menu_icon_selected);
                    holder.addList.setText(context.getResources().getString(R.string.selected));
                    holder.addList.setTextColor(getColor(context, R.color.green_bg));
                }
            }
            holder.animationText.setVisibility(View.GONE);
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
                    dmSubCategoryFragment.showItemDetails(dmSubCategory, dmSubCategories, position);
                }
            });
            holder.addList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dmSubCategoryFragment.addToDb(holder.addListImage, holder.addList, dmSubCategory);
                }
            });holder.addListImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dmSubCategoryFragment.addToDb(holder.addListImage, holder.addList, dmSubCategory);
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

    public static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    @Override
    public int getItemCount() {
        return dmSubCategories.size();
    }

}

