package com.franky.cathttp.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/15.
 */

public class User implements Serializable {

    public String id;
    public String account;
    public String email;
    public String username;
    public String token;

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", account='" + account + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
