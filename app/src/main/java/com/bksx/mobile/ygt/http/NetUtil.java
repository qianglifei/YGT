package com.bksx.mobile.ygt.http;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.bksx.mobile.ygt.utils.SharedPreferencesUtils;
import com.bksx.mobile.ygt.utils.StaticObject;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;



public class NetUtil {
    private static final String TAG = "NETUTIL";

    public static void setIp(String ip) {
        NetUtil.ip = ip;
        NetUtil.url="http://" + ip +"/qxydcjjk/";
        single = new NetUtil();
    }



    public static String ip = "130.10.7.54:9200";
    public static String url = "http://" + ip +"/qxydcjjk/";

    //测试部ip
//    public static String ip = "130.10.8.38:6050";
//    public static String url="http://" + ip + "/qxydcjjk/";


    private JSONObject jo;

    private NetUtil() {}
    private static NetUtil single=null;
    //静态工厂方法
    public static NetUtil getNetUtil() {
        if (single == null) {
            single = new NetUtil();
        }
        return single;
    }

    public String getVersionCode(Context context){
        PackageManager packageManager=context.getPackageManager();
        PackageInfo packageInfo;
        String versionCode="";
        try {
            packageInfo=packageManager.getPackageInfo(context.getPackageName(),0);
            versionCode=packageInfo.versionCode+"";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    //发送普通post请求
    public void sendPost(final Handler handler, final RequestParams rp, final Context context) {
        rp.setConnectTimeout(70000);
        rp.setConnectTimeout(70000);
        rp.setReadTimeout(70000);
        rp.setCacheMaxAge(1000*10);//默认缓存存活时间, 单位:毫秒
        x.http().post(rp, new Callback.CacheCallback<String>() {

            @Override
            public void onSuccess(String result) {
                handleSuccessMessage(handler,result,context);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                try {
                    handleErrorMessage(handler, ex);
                } catch (Exception e) {
                    Log.d("lebron", "onError");
                    handleBadMessage(handler, e);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });

    }

    //发送普通get请求
    public void sendGet(final Handler handler, final RequestParams rp, final Context context) {
        rp.setConnectTimeout(70000);
        rp.setReadTimeout(70000);
        rp.setCacheMaxAge(1000*10);//  默认缓存存活时间, 单位:毫秒
        x.http().get(rp, new Callback.CacheCallback<String>(){

            @Override
            public void onSuccess(String result) {
                handleSuccessMessage(handler,result,context);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });

    }

    //广播请求异常
    public void handleBadMessage(Handler handler, Exception e) {
        e.printStackTrace();
        try {
            Message msg = Message.obtain();
            jo = new JSONObject();
            msg.obj = jo;
            jo.put("state", "error");
            jo.put("msg", "网络连接失败");
            jo.put("code", "000");
            handler.sendMessage(msg);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }


    //广播成功消息
    public void handleSuccessMessage(Handler handler, String result,Context context) {
        try {
            Message msg = Message.obtain();
            jo = new JSONObject(result);
            if (jo.optString("returnCode").equalsIgnoreCase("500")){
                Log.i(TAG, "handleSuccessMessage: " + jo.toString());
                Toast.makeText(context, jo.optString("returnMsg"),Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(context, LoginActivity.class);
                //context.startActivity(intent);
                return;
            }else {
                jo.put("state", "success");
                msg.obj = jo;
                handler.sendMessage(msg);
            }
        } catch (Exception e) {
            handleBadMessage(handler, e);
        }
    }

    //广播错误消息
    public void handleErrorMessage(Handler handler, Throwable ex) {
        try {
            HttpException httpEx = (HttpException) ex;
            Log.i("HttpException_result", httpEx.getResult());
            Message msg = Message.obtain();
            jo = new JSONObject(httpEx.getResult());
            jo.put("state", "error");
            msg.obj = jo;
            handler.sendMessage(msg);
        } catch (Exception e) {
            handleBadMessage(handler, e);
        }
    }

}
