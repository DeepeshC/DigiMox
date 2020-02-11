package com.digimox.app

import android.app.Application
import android.content.Context

import com.digimox.R

import org.acra.ACRA
import org.acra.ReportField
import org.acra.ReportingInteractionMode
import org.acra.annotation.ReportsCrashes

/**
 * Created by Deepesh on 26-Nov-15.
 */

/**
 * The Class MSApplication.
 */
@ReportsCrashes(formKey = "", mailTo = "deepeshppm@gmail.com", mode = ReportingInteractionMode.TOAST, customReportContent = [ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME, ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT], resToastText = R.string.crash_toast)
class DMApplication : Application() {
    var context: Context? = null

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        ACRA.init(this)
    }
}
