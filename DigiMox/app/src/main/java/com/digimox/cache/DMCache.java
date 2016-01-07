package com.digimox.cache;

import android.graphics.Typeface;

import java.util.Hashtable;

/**
 * Created by Abhishek on 5/24/2015.
 */
public class DMCache {

    public static DMCache getSharedLRCache() {

        if (_cache == null) {
            _cache = new DMCache();

        }
        return _cache;
    }

    private static DMCache _cache;


    private static final Hashtable<String, Typeface> fontCache = new Hashtable<String, Typeface>();

    public Hashtable<String, Typeface> getFontCache() {
        return fontCache;
    }

    public void setFontCache(String font, Typeface fontname) {
        fontCache.put(font, fontname);
    }
}
