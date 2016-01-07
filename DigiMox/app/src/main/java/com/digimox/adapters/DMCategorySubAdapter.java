package com.digimox.adapters;

import android.content.Context;
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
import com.digimox.fragments.DMMainCategorySubFragment;
import com.digimox.models.response.DMMainCategory;
import com.digimox.utils.DMUtils;

import java.util.ArrayList;

/**
 * Created by Deepesh on 23-Aug-15.
 */
public class DMCategorySubAdapter extends RecyclerView.Adapter<DMCategorySubAdapter.ViewHolder> {
    private final TypedValue mTypedValue = new TypedValue();
    private Context context;
    private OnItemClickListener mItemClickListener;
    private DMMainCategorySubFragment dmMainCategorySubFragment;
    private ArrayList<DMMainCategory> dmMainCategories;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView categoryImage;
        private TextView categoryName;
        private ProgressBar mainCategoryProgress;
        private View mView;

        public ViewHolder(View view) {
            super(view);
            this.mView = view;
            view.setOnClickListener(this);
            categoryImage = (ImageView) view.findViewById(R.id.categoryImage);
            categoryName = (TextView) view.findViewById(R.id.category_name);
            mainCategoryProgress = (ProgressBar) view.findViewById(R.id.progress_main_category);

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

    public DMCategorySubAdapter(Context context, DMMainCategorySubFragment dmMainCategorySubFragment, ArrayList<DMMainCategory> dmMainCategories) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        this.context = context;
        this.dmMainCategorySubFragment = dmMainCategorySubFragment;
        this.dmMainCategories = dmMainCategories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        ViewGroup defaultGroup = (ViewGroup) mInflater.inflate(R.layout.category_list_item, parent, false);
        return new ViewHolder(defaultGroup);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final DMMainCategory dmMainCategory = dmMainCategories.get(position);
        if (!TextUtils.isEmpty(dmMainCategory.getGroupImage())) {
            DMUtils.setImageUrlToView(context, holder.categoryImage, dmMainCategory.getGroupImage(), holder.mainCategoryProgress);
        }
        holder.categoryName.setText(dmMainCategory.getGroupName());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSubCategory(dmMainCategory, position);
            }
        });

    }

    private void goToSubCategory(DMMainCategory dmMainCategory, int position) {
        dmMainCategorySubFragment.clickMainCategory(dmMainCategories, dmMainCategory, position);
    }

    @Override
    public int getItemCount() {
        return dmMainCategories.size();
    }

}

