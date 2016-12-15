package com.http.callback.impl;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Administrator on 2016/12/15.
 */

public abstract class JsonCallback<T> extends AbstractCallback<T> {

    @Override
    protected T bindData(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        JSONObject data = jsonObject.optJSONObject("data");
        Gson gson = new Gson();
        Type type = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return gson.fromJson(data.toString(),type);
    }
}