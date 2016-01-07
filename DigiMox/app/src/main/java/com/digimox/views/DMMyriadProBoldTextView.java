package com.digimox.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.digimox.cache.DMCache;

/**
 * Created by Deepesh on 10-Dec-15.
 */
public class DMMyriadProBoldTextView extends TextView {
    public DMMyriadProBoldTextView(Context context) {
        super(context);

        if (!isInEditMode()) {
            synchronized (DMCache.getSharedLRCache().getFontCache()) {

                if (!DMCache.getSharedLRCache().getFontCache()
                        .containsKey("MyriadPro-Bold")) {

                    Typeface tf = Typeface.createFromAsset(context.getAssets(),
                            "font/MyriadPro-Bold.otf");
                    DMCache.getSharedLRCache().setFontCache("MyriadPro-Bold", tf);
                }

                setTypeface(
                        DMCache.getSharedLRCache().getFontCache().get("MyriadPro-Bold"),
                        Typeface.BOLD);

            }

        }
    }

    public DMMyriadProBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            synchronized (DMCache.getSharedLRCache().getFontCache()) {

                if (!DMCache.getSharedLRCache().getFontCache()
                        .containsKey("MyriadPro-Bold")) {

                    Typeface tf = Typeface.createFromAsset(context.getAssets(),
                            "font/MyriadPro-Bold.otf");
                    DMCache.getSharedLRCache().setFontCache("MyriadPro-Bold", tf);
                }

                setTypeface(
                        DMCache.getSharedLRCache().getFontCache().get("MyriadPro-Bold"),
                        Typeface.BOLD);

            }
        }
    }


}
