package com.http.task;

import android.os.AsyncTask;

import com.http.bean.Request;
import com.http.core.HttpUtils;

import java.net.HttpURLConnection;

/**
 * Created by Administrator on 2016/12/12.
 */

public class HttpTask extends AsyncTask<Void,Integer,Object> {

    public Request mRequest;

    public HttpTask(Request request) {
        mRequest = request;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Void... params) {

        try {
            HttpURLConnection connection = HttpUtils.execute(mRequest);
            return mRequest.iCallback.parse(connection);
        }catch (Exception e){
            return e;
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (o instanceof Exception){
            mRequest.iCallback.onFailure((Exception) o);
        }else {
            mRequest.iCallback.onSuccess(o);
        }
    }
}
