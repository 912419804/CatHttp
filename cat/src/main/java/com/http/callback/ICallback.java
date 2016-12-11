package com.http.callback;

/**
 * Created by Administrator on 2016/12/11.
 */

public interface ICallback<T> {

    void onSuccess(T t);
    void onFailure(Exception e);

}
