package com.digimox.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.digimox.cache.DMCache;

/**
 * Created by Deepesh on 10-Dec-15.
 */
public class DMMyriadProSemiBoldTextView extends TextView {
    public DMMyriadProSemiBoldTextView(Context context) {
        super(context);

        if (!isInEditMode()) {
            synchronized (DMCache.getSharedLRCache().getFontCache()) {

                if (!DMCache.getSharedLRCache().getFontCache()
                        .containsKey("MyriadPro-Semibold")) {

                    Typeface tf = Typeface.createFromAsset(context.getAssets(),
                            "font/MyriadPro-Semibold.otf");
                    DMCache.getSharedLRCache().setFontCache("MyriadPro-Semibold", tf);
                }

                setTypeface(
                        DMCache.getSharedLRCache().getFontCache().get("MyriadPro-Semibold"),
                        Typeface.BOLD);

            }

        }
    }

    public DMMyriadProSemiBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            synchronized (DMCache.getSharedLRCache().getFontCache()) {

                if (!DMCache.getSharedLRCache().getFontCache()
                        .containsKey("MyriadPro-Semibold")) {

                    Typeface tf = Typeface.createFromAsset(context.getAssets(),
                            "font/MyriadPro-Semibold.otf");
                    DMCache.getSharedLRCache().setFontCache("MyriadPro-Semibold", tf);
                }

                setTypeface(
                        DMCache.getSharedLRCache().getFontCache().get("MyriadPro-Semibold"),
                        Typeface.BOLD);

            }
        }
    }


}
