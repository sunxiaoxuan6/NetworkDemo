package com.example.networkdemo;

import android.text.TextUtils;

import org.apache.http.NameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtils {

    public static String get(String urlPath) {
        HttpURLConnection connection = null;
        InputStream is = null;
        try {
//            1、将url字符串转为URL对象
            URL url = new URL(urlPath);
//            2、获得HttpURLConnection对象
            connection = (HttpURLConnection) url.openConnection();
//            3、设置连接的相关参数
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36");
            connection.setDoInput(true);

//            4、配置https的证书
            if ("https".equalsIgnoreCase(url.getProtocol())) {
                ((HttpsURLConnection) connection).setSSLSocketFactory(
                        HttpsUtil.getSSLSocketFactory());
            }
//            5、进行数据的读取，首先判断响应码是否为200
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                获得输入流
                is = connection.getInputStream();
//                包装字节流为字符流
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//                读取数据
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
//                6、关闭资源
                is.close();
                connection.disconnect();
//                7、返回结果
                return response.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    //    组装请求参数
    private static String getParamString(List<NameValuePair> pairs)
            throws UnsupportedEncodingException {
        StringBuilder builder=new StringBuilder();
        for (NameValuePair pair:pairs){
            if(!TextUtils.isEmpty(builder)){
                builder.append("&");
            }
            builder.append(URLEncoder.encode(pair.getName(),"UTF-8"));
            builder.append(URLEncoder.encode(pair.getValue(),"UTF-8"));
        }
        return builder.toString();
    }

    public static String post(String urlPath,List<NameValuePair> params){
        HttpURLConnection connection = null;
        InputStream is = null;
        if(params==null||params.size()==0){
            return get(urlPath);
        }
        try {
            String body=getParamString(params);
            byte[] data=body.getBytes();

            URL url=new URL(urlPath);

            connection= (HttpsURLConnection) url.openConnection();
            //            3、设置连接的相关参数
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36");
            connection.setDoInput(true);
            connection.setDoOutput(true);

//            4、配置https的证书
            if ("https".equalsIgnoreCase(url.getProtocol())) {
                ((HttpsURLConnection) connection).setSSLSocketFactory(
                        HttpsUtil.getSSLSocketFactory());
            }
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length",String.valueOf(data.length));
            OutputStream os=connection.getOutputStream();
            os.write(data);
            os.flush();


            if(connection.getResponseCode()==HttpURLConnection.HTTP_OK){

               is=connection.getInputStream();
               BufferedReader reader=new BufferedReader(new InputStreamReader(is));
                //                读取数据
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
//                6、关闭资源
                is.close();
                connection.disconnect();
//                7、返回结果
                return response.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
        }


}