package com.example.heathinfogather.utils;

import android.net.Uri;

import com.example.heathinfogather.beans.UserInfoBean;
import com.example.heathinfogather.beans.UserStatusBean;
import com.example.heathinfogather.beans.UsersBean;

import java.io.File;


public class GlobalManager {
    public static UserInfoBean.DataBean user;
    public static UserStatusBean.DataBean status;
    public static UsersBean.DataBean usersInfo;
    public static boolean isFirstLoadUsers=true;


    public static ClickEvent event=ClickEvent.All;
    public enum ClickEvent{
        All,
        Card,
        Face,
        Temp,
        Exception,
    }

    public static int Id;
    public static Uri uri;
    public static File file;
    public static boolean hasChangeImage=false;

}
