package com.example.networkdemo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.networkdemo.model.Ip;
import com.example.networkdemo.model.IpData;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class OkVisitActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG="OkVisitActivity";
//请求的URL
    private static final String IP_BASE_URL = "http://ip.taobao.com/service/getIpInfo.php";
    private static final String IP_URL=IP_BASE_URL+"?ip=221.226.155.15";

    private static final String UPLOAD_FILE_URL = "https://api.github.com/markdown/raw";

    private static final String UPLOAD_IMG_URL = "https://api.imgur.com/3/image";

    private static final String DOWNLOAD_URL = "https://github.com/zhayh/AndroidExample/blob/master/README.md";
    private static final String IMAGE_URL = "https://img.alicdn.com/bao/uploaded/i2/100000294640179384/TB24I0xXNQa61Bjy0FhXXaalFXa_!!0-0-travel.jpg";

    // 指定MIME类型
//    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");


    private ScrollView scroll;
    private TextView text;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_visit);

        scroll=findViewById(R.id.sv_scroll2);
        text=findViewById(R.id.tv_OkHttp);
        image=findViewById(R.id.iv_image2);
//加载图片
        GlideApp.with(this)
                .load("http://guolin.tech/book.png")
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.d("OkHttpActivity", "加载失败 errorMsg：" + (e != null ? e.getMessage() : "null"));
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d("OkHttpActivity", "成功  Drawable Name：" + resource.getClass().getCanonicalName());
                        return false;
                    }
                })
                .placeholder(R.mipmap.ic_launcher_round)
                .into(image);

    }

    @Override
    public void onClick(View v) {
        String path=getFilesDir().getAbsolutePath();// data/data/<包名>/readme.md
        switch (v.getId()){
            case R.id.bt_get2:
               scroll.setVisibility(View.VISIBLE);
               image.setVisibility(View.GONE);
               get(IP_URL);
                break;
            case R.id.bt_post2:
                scroll.setVisibility(View.VISIBLE);
                image.setVisibility(View.GONE);
                Map<String,String> params=new HashMap<>();
                params.put("ip","121.229.217.223");
                post(IP_URL,params);
                break;
            case R.id.bt_up2:
                scroll.setVisibility(View.VISIBLE);
                image.setVisibility(View.GONE);

                String fileName=path+ File.separator+"readme.md";
                uploadFile(UPLOAD_FILE_URL,fileName);
                break;
            case R.id.bt_down2:
                scroll.setVisibility(View.VISIBLE);
                image.setVisibility(View.GONE);

                downFile(DOWNLOAD_URL, path);
                break;
            default:
                break;
        }
    }

//    get异步请求是在子线程中执行的，需要切换到主线程更新UI
    private void get(String url){
//        1、构造Request
        Request request=new Request.Builder().url(url)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) " +
                        "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36")
                .addHeader("Accept","application/json")
//                .get()
//                .method("GET",null)
                .build();
//        2、发送请求，并处理回调
        OkHttpClient client=HttpsUtil.handleSSLHandshakeByOkHttp();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull final IOException e) {
                Log.e(TAG, e.getMessage());
                runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    text.setText("获取失败，" + e.getMessage());
                }
            });
        }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
//                    1、获取相应主体的字符串
                    String json=response.body().string();
//                    2、使用FastJson库解析json字符串
                    final Ip ip= JSON.parseObject(json, Ip.class);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            3、根据返回的code判断获取是否成功
                            if(ip.getCode()!=0){
                                text.setText("未获得数据");
                            }else {
//                                4、解析数据
                                IpData data=ip.getData();
                                text.setText(data.getIp() + "," +data.getCountry());
                            }
                        }
                    });
                }else {
                    Log.d(TAG,response.body().string());
                }
            }
        });
    }

    private void post(String url,Map<String,String> params){
        // 1. 构建RequestBody
        RequestBody body=setRequestBody(params);
        // 2. 创建Request对象
        Request request=new Request.Builder().url(url).post(body)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) " +
                        "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36")
                .addHeader("Accept","application/json")
                .build();

        HttpsUtil.handleSSLHandshakeByOkHttp().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull final IOException e) {
                Log.e(TAG, e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text.setText("获取失败，" + e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                if (response.isSuccessful()) {
                    // 1. 获取响应主体的json字符串
                    String json = response.body().string();

                    // 2. 使用FastJson库解析json字符串
                    final Ip ip = JSON.parseObject(json, Ip.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 3. 根据返回的code判断获取是否成功
                            if (ip.getCode() != 0) {
                                text.setText("未获得数据");
                            } else {
                                // 4. 解析数据
                                IpData data = ip.getData();
                                text.setText(data.getIp() + ", " + data.getCity());
                            }
                        }

                    });
                } else {
                    Log.d(TAG, response.body().string());
                }
            }
        });
    }

    private RequestBody setRequestBody(Map<String, String> params) {
        FormBody.Builder builder=new FormBody.Builder();
        for(String key:params.keySet()){
            builder.add(key,params.get(key));
        }
        return builder.build();
    }
//上传文件

    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown;charset=utf-8");
    private void uploadFile(String url, final String fileName){
        final Request request=new Request.Builder().url(url)
                .post(RequestBody.create(new File(fileName),MEDIA_TYPE_MARKDOWN))
                .build();

        OkHttpClient client=HttpsUtil.handleSSLHandshakeByOkHttp();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, e.getMessage());
                text.post(new Runnable() {
                    @Override
                    public void run() {
                        text.setText(fileName + "上传失败");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    final String str=response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            text.setText("上传成功，"+str);
                        }
                    });
                }else {
                    Log.d(TAG,response.body().string());
                }

            }
        });
    }

//上传图片
    public static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private void uploadImage(String url,final String fileName){
        // 1. 创建请求主体RequestBody
        RequestBody fileBody = RequestBody.create(new File(fileName), MEDIA_TYPE_PNG);
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", "头像")
                .addFormDataPart("file", fileName, fileBody)
                .build();

        // 2. 创建请求
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Client-ID 4ff8b2fc6d5f339")
                .header("User-Agent", "NetworkDemo")
                .post(body)
                .build();

        // 3. 创建OkHttpClient对象，发送请求，并处理回调
        OkHttpClient client = HttpsUtil.handleSSLHandshakeByOkHttp();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text.setText(fileName + "上传失败");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                final String str = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text.setText("上传成功，" + str);
                    }
                });
            }
        });
    }

//下载文件
    public static void writeFile(InputStream is,String path,String fileName) throws IOException{
        // 1. 根据path创建目录对象，并检查path是否存在，不存在则创建
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        // 2. 根据path和fileName创建文件对象，如果文件存在则删除
        File file = new File(path, fileName);
        if (file.exists()) {
            file.delete();
        }
        // 3. 创建文件输出流对象，根据输入流创建缓冲输入流对象，
        FileOutputStream fos = new FileOutputStream(file);
        BufferedInputStream bis = new BufferedInputStream(is);
        // 4. 以每次1024个字节写入输出流对象
        byte[] buffer = new byte[1024];
        int len;
        while ((len = bis.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
        }
        fos.flush();
        // 5. 关闭输入流、输出流对象
        fos.close();
        bis.close();
    }

//OKHttp下载文件
    private void downFile(final String url,final String path){
        // 1. 创建Requet对象
        Request request = new Request.Builder().url(url).build();
        // 2. 创建OkHttpClient对象，发送请求，并处理回调
        OkHttpClient client = HttpsUtil.handleSSLHandshakeByOkHttp();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    // 1. 获取下载文件的后缀名
                    String ext = url.substring(url.lastIndexOf(".") + 1);
                    // 2. 根据当前时间创建文件名，避免重名冲突
                    final String fileName = System.currentTimeMillis() + "." + ext;
                    // 3. 获取响应主体的字节流
                    InputStream is = response.body().byteStream();
                    // 4. 将文件写入path目录
                    writeFile(is, path, fileName);
                    // 5. 在界面给出提示信息
                    text.post(new Runnable() {
                        @Override
                        public void run() {
                            text.setText(fileName + "下载成功，存放在" + path);
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull final IOException e) {
                Log.d(TAG, e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text.setText("下载失败，" + e.getMessage());
                    }
                });
            }
        });
    }
}
