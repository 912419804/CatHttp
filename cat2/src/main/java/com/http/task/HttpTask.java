package com.http.task;

import android.os.AsyncTask;

import com.http.bean.Request;
import com.http.callback.IUpdateProgressListener;
import com.http.core.HttpUtils;
import com.http.exception.CatException;

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
            return mRequest.iCallback.parse(connection,new IUpdateProgressListener(){
                @Override
                public void updateProgress(int currentLength, int totalLength) {
                    publishProgress(currentLength,totalLength);
                }
            });
        }catch (CatException e){
            return e;
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (o instanceof CatException){
            if (mRequest.listener !=null){
                if (!mRequest.listener.handleException((CatException) o)){
                    mRequest.iCallback.onFailure((CatException) o);
                }
            }else {
                mRequest.iCallback.onFailure((CatException) o);
            }
        }else {
            mRequest.iCallback.onSuccess(o);
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        mRequest.iCallback.updateProgress(values[0],values[1]);
    }
}
