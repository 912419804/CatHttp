package com.http.manager;

import com.http.bean.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/12/19.
 * Request管理类，用于执行和取消任务
 */

public class RequestManager {

    private static RequestManager instance;
    private final ExecutorService mExecutorService;
    private HashMap<String, ArrayList<Request>> requests;

    public static RequestManager getInstance() {
        if (instance == null) {
            instance = new RequestManager();
        }
        return instance;
    }

    private RequestManager() {
        requests = new HashMap<>();
        mExecutorService = Executors.newFixedThreadPool(5);
    }

    public void performRequest(Request request) {
        if (request != null) {
          request.execute(mExecutorService);
            ArrayList<Request> list = this.requests.get(request.tag);
            if (list == null) {
                list = new ArrayList<>();
                this.requests.put(request.tag, list);
            }
            list.add(request);
        }
    }

    public void cancelRequestForTag(String tag,boolean force) {
        if (tag == null || tag.trim().equals("")){
            return;
        }
        ArrayList<Request> list = this.requests.remove(tag);
        if (list != null) {
            for (Request request : list) {
                if (!request.isCancel && tag.equals(request.tag)) {
                    request.cancel(force);
                }
            }
        }


    }

    public void cancelAllRequest() {
        Set<String> keySet = requests.keySet();
        for (String key : keySet) {
            ArrayList<Request> rs = this.requests.get(key);
            for (Request r : rs) {
                if (!r.isCancel) {
                    r.cancel(true);
                }

            }
        }
    }


}
