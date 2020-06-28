package com.example.heathinfogather.server;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.UnicodeSetSpanner;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.heathinfogather.beans.CommonDataBean;
import com.example.heathinfogather.beans.UserInfoBean;
import com.example.heathinfogather.beans.UserStatusBean;
import com.example.heathinfogather.beans.UsersBean;
import com.example.heathinfogather.utils.GlobalManager;
import com.example.heathinfogather.utils.SingleInstance;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitRequest {
    private static final String TAG = "mxy";

    public void userLogin(String number, String name,Callback<UserInfoBean> callback){
        Map<String,RequestBody> param=new HashMap<>();
        param.put("number",RequestBody.create(MediaType.parse("multipart/form-data"),number));
        param.put("name",RequestBody.create(MediaType.parse("multipart/form-data"), name));
        Call<UserInfoBean> userLoginBeanCall = SingleInstance.getApi().userLogin(param);
        userLoginBeanCall.enqueue(callback);
    }

    public void userInfoGet(Callback<UsersBean> callback){
        Call<UsersBean> userInfoBeanCall = SingleInstance.getApi().userInfoGet(1);
        userInfoBeanCall.enqueue(callback);
    }

    public void getUserStatus(int user_id,Callback<UserStatusBean> callback){
        Call<UserStatusBean> userStatus = SingleInstance.getApi().getUserStatus(user_id);
        userStatus.enqueue(callback);
    }

    public void uploadFile(Activity activity, File file,int user_id, PopupWindow loadingPwd){
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"),file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("pic",
                user_id+".jpg",requestBody);
        Map<String,RequestBody> map=new HashMap<>();
        map.put("user_id",RequestBody.create(MediaType.parse("multipart/form-data")
                , String.valueOf(user_id)));
        Call<CommonDataBean> commonDataBeanCall = SingleInstance.getApi().uploadFile(map,part);
        commonDataBeanCall.enqueue(new Callback<CommonDataBean>() {
            @Override
            public void onResponse(Call<CommonDataBean> call, Response<CommonDataBean> response) {
                loadingPwd.dismiss();
                int code = response.code();
                if (code== HttpURLConnection.HTTP_OK) {
                    CommonDataBean body = response.body();
                    if (body.getMsg().equals("ok")) {
                        GlobalManager.hasChangeImage=true;
                        Toast.makeText(activity, "文件上传成功", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(activity, "文件上传失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonDataBean> call, Throwable t) {
                loadingPwd.dismiss();
                Toast.makeText(activity, "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void updateUserInfo(Activity activity, Map<String, String> infoMap, UserInfoBean.DataBean user, PopupWindow popupWindow) {
        Map<String, RequestBody> params = GenerateBody(infoMap);
        Call<CommonDataBean> call = SingleInstance.getApi().updateUserInfo(params);
        call.enqueue(new Callback<CommonDataBean>() {
            @Override
            public void onResponse(Call<CommonDataBean> call, Response<CommonDataBean> response) {
                popupWindow.dismiss();
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    CommonDataBean body = response.body();
                    if (body.getMsg().equals("ok")) {
                        Toast.makeText(activity, "信息更新成功", Toast.LENGTH_SHORT).show();
                        GlobalManager.user = user;
                        activity.finish();
                    } else {
                        Toast.makeText(activity, "更新失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonDataBean> call, Throwable t) {
                popupWindow.dismiss();
                Toast.makeText(activity, "连接网络失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private Map<String,RequestBody> GenerateBody(Map<String,String> params){
        Map<String,RequestBody> map=new HashMap<>();
        Set<Map.Entry<String, String>> entries = params.entrySet();
        for (Map.Entry<String,String> entry:
             entries) {
           RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"),
                   entry.getValue());
           map.put(entry.getKey(),body);
        }
        return map;
    }

    public void updateCardStatus(Callback<CommonDataBean> callback,int user_id,String isCard){
        RequestBody body1 = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(user_id));
        RequestBody body2 = RequestBody.create(MediaType.parse("multipart/form-data"), isCard);
        Call<CommonDataBean> call = SingleInstance.getApi().updateCardStatus(body1,body2);
        call.enqueue(callback);
    }

}
