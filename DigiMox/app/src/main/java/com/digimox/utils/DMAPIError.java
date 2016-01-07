package com.digimox.utils;


import com.digimox.R;
import com.digimox.app.DMApplication;

/**
 * Created by b2c on 11/14/15.
 */
public class DMAPIError {
    public static final int DM_GENERAL_ERROR_CODE = 4000;
    public static final int DM_NETWORK_ERROR_CODE = 4001;
    public static final int MDM_SERVER_ERROR_CODE = 4002;
    public static final int DM_API_ERROR = 4003;

    public static final String DM_GENERAL_ERROR_TITLE = DMApplication.applicationContext.getResources().getString(R.string.api_error_message_api);
    ;
    public static final String DM_GENERAL_API_ERROR_MESSAGE = DMApplication.applicationContext.getResources().getString(R.string.api_error_message_api);
    public static final String DM_GENERAL_NETWORK_ERROR_MESSAGE = DMApplication.applicationContext.getResources().getString(R.string.api_error_no_network);

    private int code;
    private String message;
    private String title;

    public DMAPIError(int code, String title, String message) {
        this.code = code;
        this.title = title;
        this.message = message;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
