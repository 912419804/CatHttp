package com.http.callback;

import com.http.exception.CatException;

/**
 * Created by Administrator on 2016/12/19.
 */
public interface GlobalExceptionListener {

    boolean handleException(CatException e);

}
