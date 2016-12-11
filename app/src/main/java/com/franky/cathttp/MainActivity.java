package com.franky.cathttp;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.http.bean.Request;
import com.http.core.HttpUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private static final int GET_CONTENT = 1;

    private EditText et_url;
    private TextView tv_content;
    private Button bt_go;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
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
        switch (v.getId()){
            case R.id.bt_go:
                getContent();
                break;
        }
    }

    private void getContent() {
        String url = et_url.getText().toString();
        final Request request = new Request(Request.Method.GET);
        request.url = url;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String s = HttpUtils.get(request);
                Message message = mHandler.obtainMessage();
                message.what = GET_CONTENT;
                message.obj = s;
                mHandler.sendMessage(message);
            }
        }).start();
    }
}
