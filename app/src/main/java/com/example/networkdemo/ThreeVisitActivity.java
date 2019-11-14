package com.example.networkdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.networkdemo.model.OilPrice;
import com.example.networkdemo.model.OilPriceBody;
import com.example.networkdemo.model.OilPriceRes;
import com.example.networkdemo.model.WeatherCurrent;
import com.example.networkdemo.model.WeatherFuture;
import com.example.networkdemo.model.WeatherRealtime;
import com.show.api.ShowApiRequest;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ThreeVisitActivity extends AppCompatActivity {
//    易源数据的ID和KEY
    private static final String APP_ID="47490";
    private static final String KEY="c1813441e5a0477cb68618165c4226dc";

//    聚合数据
    private static final String WEATHER_URL="http://apis.juhe.cn/simpleWeather/query";
    private static final String WEATHER_APP_KEY="aab53adfb4600cae3721fb01222bc767";

    private static final String OIL_URL="http://route.showapi.com/138-46";
    private static final String NEWS_URL="http://v.juhe.cn/toutiao/index";

    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_visit);

        tvResult=findViewById(R.id.tv_three);
    }


    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_oil:
                getOilPrice("江苏");
                break;
            case R.id.bt_new:
                getTopNews();
                break;
            case R.id.bt_weather:
                getWeather("南京");
                break;
        }
    }

    private void getWeather(String city) {
        try {
//            1、组装数据请求的url
            String url=WEATHER_URL
                    +"?key="+WEATHER_APP_KEY
                    +"&city="+ URLEncoder.encode(city,"utf-8");
//            2、使用OKHttp发送请求
            Request request=new Request.Builder().url(url).build();
            new OkHttpClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.e("ThreeDataActivity",e.getMessage());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//            3、数据处理
                    if(response.isSuccessful()){
                        String json=response.body().string();

                       JSONObject object= JSON.parseObject(json);
                        if(object!=null){
                            JSONObject result=object.getJSONObject("result");
                            JSONObject realtime=result.getJSONObject("realtime");
                            final String city=result.getString("city");

//                            利用FastJSON转对象
                            final WeatherRealtime weather=JSON.parseObject(realtime.toJSONString(),
                                    WeatherRealtime.class);

//                            获取五天的天气趋势
                            JSONArray futureWeather=result.getJSONArray("future");
                            final List<WeatherFuture> weatherFutures=JSON.parseArray(
                                    futureWeather.toJSONString(),
                                    WeatherFuture.class
                            );
//                            4、到界面显示获取的数据
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    StringBuilder builder=new StringBuilder();
                                    builder.append(city).append(weather)
                                            .append("\n\n").append("5天的天气趋势");
                                    for (WeatherFuture future:weatherFutures){
                                        builder.append("\n").append(future);
                                    }
                                    tvResult.setText(builder.toString());
                                }
                            });
                        }
                    }
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void getTopNews() {
    }

    private void getOilPrice(final String province) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String res=new ShowApiRequest(OIL_URL,APP_ID,KEY)
                        .addTextPara("prov",province)
                        .post();
                tvResult.post(new Runnable() {
                    @Override
                    public void run() {
                        OilPriceRes priceRes=JSON.parseObject(res,OilPriceRes.class);
                        if(priceRes != null && priceRes.getResCode() == 0) {
                            OilPriceBody body = priceRes.getResBody();
                            if(body != null && body.getRetCode() == 0) {
                                List<OilPrice> prices = body.getList();
                                tvResult.setText(prices.get(0).toString());
                            }
                        }
                    }
                });
            }
        }).start();
    }

}
