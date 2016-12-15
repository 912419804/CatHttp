package com.franky.cathttp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.franky.cathttp.bean.User;
import com.http.bean.Request;
import com.http.callback.impl.JsonCallback;
import com.http.task.HttpTask;
import com.orhanobut.logger.Logger;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


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
                getContent();
                break;
        }
    }

    private void setText(String s){
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
        request.setCallback(new JsonCallback<User>(){

            @Override
            public void onSuccess(User s) {
                setText(s.toString());
                Logger.d(s);
            }

            @Override
            public void onFailure(Exception e) {
                setText(e.getMessage());
            }
        });
        HttpTask httpTask = new HttpTask(request);
        httpTask.execute();
    }
}
