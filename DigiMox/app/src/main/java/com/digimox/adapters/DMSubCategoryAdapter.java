package com.digimox.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.digimox.R;

/**
 * Created by Deepesh on 23-Aug-15.
 */
public class DMSubCategoryAdapter extends RecyclerView.Adapter<DMSubCategoryAdapter.ViewHolder> {
    private final TypedValue mTypedValue = new TypedValue();

    private int mBackground;
    //    private ArrayList<AQProductModel> aqProductModels;
    private Context context;
    private OnItemClickListener mItemClickListener;
    private int[] categoryImage = {R.drawable.cat_1, R.drawable.cat_2, R.drawable.cat_3, R.drawable.cat_4, R.drawable.cat_5, R.drawable.cat_6, R.drawable.cat_7, R.drawable.cat_6, R.drawable.cat_7};

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView categoryImage;
        private View mView;

        public ViewHolder(View view) {
            super(view);
            this.mView = view;
            view.setOnClickListener(this);
            categoryImage = (ImageView) view.findViewById(R.id.sub_category_image);

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

    public DMSubCategoryAdapter(Context context) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        ViewGroup defaultGroup = (ViewGroup) mInflater.inflate(R.layout.category_sub_list_item, parent, false);
        return new ViewHolder(defaultGroup);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.categoryImage.setImageResource(categoryImage[position]);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }


    @Override
    public int getItemCount() {
        return 9;
    }

}

