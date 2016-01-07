package com.digimox.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.digimox.cache.DMCache;

/**
 * Created by Deepesh on 10-Dec-15.
 */
public class DMMyriadProRegularTextView extends TextView {
    public DMMyriadProRegularTextView(Context context) {
        super(context);
        setFont(context);
    }

    public DMMyriadProRegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }

    private void setFont(Context context) {
        if (!isInEditMode()) {
            synchronized (DMCache.getSharedLRCache().getFontCache()) {

                if (!DMCache.getSharedLRCache().getFontCache()
                        .containsKey("MyriadPro-Regular")) {

                    Typeface tf = Typeface.createFromAsset(context.getAssets(),
                            "font/MyriadPro-Regular.otf");
                    DMCache.getSharedLRCache().setFontCache("MyriadPro-Regular", tf);
                }

                setTypeface(
                        DMCache.getSharedLRCache().getFontCache().get("MyriadPro-Regular"),
                        Typeface.NORMAL);

            }
        }
    }
}
