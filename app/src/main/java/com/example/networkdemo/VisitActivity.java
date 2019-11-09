package com.example.networkdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class VisitActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String IP_URL = "http://ip.taobao.com/service/getIpInfo.php?ip=117.136.45.111";

    private ScrollView scroll;
    private TextView text;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit);

        text=findViewById(R.id.tv_http);
        image=findViewById(R.id.iv_image);

        GlideApp.with(this).
                load( "https://www.baidu.com/img/bd_logo1.png").
                placeholder(R.mipmap.ic_launcher_round).
                into(image);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_get1:
                scroll=findViewById(R.id.sv_scroll1);
                image=findViewById(R.id.iv_image);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String result=NetworkUtils.get(IP_URL);
                        Log.d("MainActivity",result);
                        if(result!=null){
                            text.post(new Runnable() {
                                @Override
                                public void run() {
                                    text.setText(result);
                                }
                            });
                        }else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    text.setText("获得数据为null");
                                }
                            });
                        }
                    }
                }).start();
                break;
            case R.id.bt_post1:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<NameValuePair> pairs=new ArrayList<>();
                        pairs.add(new BasicNameValuePair("ip","117.136.45.111"));
                        final String result=NetworkUtils.post(IP_URL,pairs);
                        if(result!=null){
                            Log.d("MainActivity",result);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    text.setText(result);
                                }
                            });
                        }else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    text.setText("请求失败，未获得数据");
                                }
                            });
                        }
                    }
                }).start();
                break;
            case R.id.bt_up1:

                break;
            case R.id.bt_down1:

                break;
        }
    }
}
