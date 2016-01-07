package com.digimox.models.requests;

import java.io.Serializable;

/**
 * Created by b2c on 11/14/15.
 */
public class DMAPIRequestObject implements Serializable {

    protected transient String method;


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}