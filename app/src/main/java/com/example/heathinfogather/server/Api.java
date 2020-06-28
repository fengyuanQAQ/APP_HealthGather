package com.example.heathinfogather.server;


import com.example.heathinfogather.activities.entities.LoginBody;
import com.example.heathinfogather.beans.CommonDataBean;
import com.example.heathinfogather.beans.UserInfoBean;
import com.example.heathinfogather.beans.UserStatusBean;
import com.example.heathinfogather.beans.UsersBean;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface Api {
    @Multipart
    @POST("/user/login")
    Call<UserInfoBean> userLogin(@PartMap Map<String,RequestBody> map);

    @GET("/admin/get_students")
    Call<UsersBean> userInfoGet(@Query("type") int type);

    @GET("/user/status")
    Call<UserStatusBean> getUserStatus(@Query("user_id")int user_id);

    @POST("/user/upload")
    @Multipart
    Call<CommonDataBean> uploadFile(@PartMap Map<String, RequestBody> map,@Part MultipartBody.Part part);

    @Multipart
    @POST("/user/update")
    Call<CommonDataBean> updateUserInfo(@PartMap Map<String,RequestBody> map);

    @Multipart
    @POST("/user/update/card")
    Call<CommonDataBean> updateCardStatus(@Part("user_id") RequestBody user_id,@Part("status") RequestBody isCard);

}
