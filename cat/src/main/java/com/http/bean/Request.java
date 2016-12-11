package com.http.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/11.
 * http 请求参数封装类
 */

public class Request implements Serializable{

    public enum Method {GET,POST,DELETE,PUT}

    public String url;
    public String content;
    public Map<String,String> map = new HashMap<>();
    public Method method = Method.GET;

    public Request(Method method) {
        this.method = method;
    }
}
