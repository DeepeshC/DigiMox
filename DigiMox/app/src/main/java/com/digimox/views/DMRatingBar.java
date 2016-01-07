package com.digimox.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.digimox.R;

/**
 * Created by Deepesh on 06-Jan-16.
 */
public class DMRatingBar extends android.widget.RatingBar {
    private Drawable starDrawable;

    public DMRatingBar(Context context) {
        this(context, null);
    }

    public DMRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            starDrawable = getResources().getDrawable(R.drawable.star_selected, context.getTheme());
        } else {
            starDrawable = getResources().getDrawable(R.drawable.star_selected);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Make sure to account for padding and margin, if you care.
        // I only cared about left padding.
        setMeasuredDimension(starDrawable.getIntrinsicWidth() * 5
                + getPaddingLeft(), starDrawable.getIntrinsicHeight());
    }
}