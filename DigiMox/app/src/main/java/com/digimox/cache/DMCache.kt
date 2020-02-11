package com.digimox.cache

import android.graphics.Typeface

import java.util.Hashtable

/**
 * Created by Abhishek on 5/24/2015.
 */
class DMCache {

    fun getFontCache(): Hashtable<String, Typeface> {
        return fontCache
    }

    fun setFontCache(font: String, fontname: Typeface) {
        fontCache[font] = fontname
    }

    companion object {

        val sharedLRCache: DMCache
            get() {

                if (_cache == null) {
                    _cache = DMCache()

                }
                return _cache
            }

        private var _cache: DMCache? = null


        private val fontCache = Hashtable<String, Typeface>()
    }
}
