package com.http.callback.impl;

import org.json.JSONException;

/**
 * Created by Administrator on 2016/12/15.
 */

public abstract class FileCallback extends AbstractCallback<String> {


    @Override
    protected String bindData(String result) throws JSONException {
        return result;
    }
}
