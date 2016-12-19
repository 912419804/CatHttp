package com.franky.cathttp;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.franky.cathttp.bean.User;
import com.http.bean.Request;
import com.http.callback.impl.FileCallback;
import com.http.callback.impl.JsonCallback;
import com.http.exception.CatException;
import com.http.callback.GlobalExceptionListener;
import com.http.task.HttpTask;
import com.orhanobut.logger.Logger;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GlobalExceptionListener {


    private static final int GET_CONTENT = 1;

    private EditText et_url;
    private TextView tv_content;
    private Button bt_go;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_CONTENT:
                    tv_content.setText(msg.obj.toString());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        et_url = (EditText) findViewById(R.id.et_url);
        tv_content = (TextView) findViewById(R.id.tv_content);
        bt_go = (Button) findViewById(R.id.bt_go);
        bt_go.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_go:
//                getContent();
                getContentForDownload();
                break;
        }
    }

    private void setText(String s) {
        Message message = mHandler.obtainMessage();
        message.what = GET_CONTENT;
        message.obj = s;
        mHandler.sendMessage(message);
    }

    private void getContent() {
//        String url = et_url.getText().toString();
        String url = "http://api.stay4it.com/v1/public/core/?service=user.login";
        String content = "account=stay4it&password=123456";
        Request request = new Request(Request.Method.POST);
        request.url = url;
        request.content = content;
        request.setCallback(new JsonCallback<User>() {

            @Override
            public void onSuccess(User s) {
                setText(s.toString());
                Logger.d(s);
            }

            @Override
            public void onFailure(CatException e) {
                setText(e.getMessage());
            }
        });
        HttpTask httpTask = new HttpTask(request);
        httpTask.execute();
    }

    private void getContentForDownload() {
//        String url = et_url.getText().toString();
        String url = "http://sw.bos.baidu.com/sw-search-sp/software/7891170d89f61/npp_7.2.2_Installer.exe";
        Request request = new Request(Request.Method.GET);
        request.url = url;
        String cachePath = Environment.getExternalStorageDirectory() + File.separator + "download" + File.separator + "test.exe";
        request.setCallback(new FileCallback() {

            @Override
            public void onSuccess(String s) {
                Logger.d(s);
            }

            @Override
            public void onFailure(CatException e) {
                if (e.statusCode == -1) {//代表java系统异常
                    Logger.d(e.getMessage());
                } else {//服务器返回异常
                    Logger.d(e.getMessage());
                }

            }

            @Override
            public void updateProgress(int currentLength, int totalLength) {
                super.updateProgress(currentLength, totalLength);
                Logger.d(currentLength + "/" + totalLength, totalLength + "");

            }
        }.setCachePath(cachePath).setProgressEnabled(true));
        request.setGlobalExceptionListener(this);
        HttpTask httpTask = new HttpTask(request);
        httpTask.execute();
    }

    @Override
    public boolean handleException(CatException e) {
        if (e.statusCode == 403){//如果token过期那么将会跳转到登陆界面，或者其他处理逻辑
            return true;
        }else {
            return false;
        }
    }
}
