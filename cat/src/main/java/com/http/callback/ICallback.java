package com.http.callback;

/**
 * Created by Administrator on 2016/12/11.
 */

public interface ICallback {

    void onSuccess();
    void onFailure(Exception e);

}
