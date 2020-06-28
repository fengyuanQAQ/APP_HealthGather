package com.example.heathinfogather.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.text.Layout;
import android.util.JsonReader;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.example.heathinfogather.R;
import com.example.heathinfogather.server.Api;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wang.avi.AVLoadingIndicatorView;
import com.zyyoona7.wheel.WheelView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SingleInstance {
    private static Map<String,SharedPreferences> spMap=new HashMap<>();
    private static Api sApi=null;
    public static String url="http://112.124.16.123";

    public static SharedPreferences getSP(Activity activity,String spName){
        if (null==spMap.get(spName)) {
            spMap.put(spName,activity.getSharedPreferences(spName, Context.MODE_PRIVATE));
        }
        return spMap.get(spName);
    }

    public static void getSinglePnd(final Activity activity, List<String> data, final TextView tvText){
            final PopupWindow singleWheelPnd=new PopupWindow();
            View view= LayoutInflater.from(activity).inflate(R.layout.popwnd_scrollview,null);
            //设置透明度
            ToolsUtil.setTransparent(activity,singleWheelPnd);

            //设置视图  设置宽度高度
            singleWheelPnd.setContentView(view);
            singleWheelPnd.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            singleWheelPnd.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

            //设置wheelview样式
            final WheelView<String> wheelView=view.findViewById(R.id.popwnd_wheelview);
            wheelView.setData(data);
            wheelView.setShowDivider(true);
            wheelView.setDividerColor(activity.getResources().getColor(R.color.colorBlack));
            wheelView.setDividerType(WheelView.DIVIDER_TYPE_FILL);
            wheelView.setDividerHeight(1);
            wheelView.setTextSize(25,true);
            Button btnCancle = view.findViewById(R.id.popwnd_wheelview_cancle);
            Button btnConfirm = view.findViewById(R.id.popwnd_wheelview_confirm);
            btnCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    singleWheelPnd.dismiss();
                }
            });

            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //保存数据
                    singleWheelPnd.dismiss();
                    tvText.setText(wheelView.getSelectedItemData());
                }
            });

            //显示相关
            singleWheelPnd.setFocusable(true);
            singleWheelPnd.setOutsideTouchable(true);//点击外面可被销毁
            singleWheelPnd.setAnimationStyle(R.style.PopupWindowAnimation);
            singleWheelPnd.showAtLocation(view
                    , Gravity.BOTTOM, 0, 0);
    }

    public static Api getApi(){
        if (sApi == null) {

//            //客户端设置连接时间
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10000, TimeUnit.MILLISECONDS)//10s
                    .build();

//            Gson gson = new GsonBuilder()
//                    .setLenient()
//                    .create();

            //retrofit框架
            Retrofit retrofit=new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(url)
                    .build();

             sApi=retrofit.create(Api.class);
        }
        return sApi;
    }

    public static PopupWindow getLoadingPwd(Activity activity,String text){
        PopupWindow popupWindow=new PopupWindow();
        View view =LayoutInflater.from(activity).inflate(R.layout.popwnd_loading,null);
        popupWindow.setContentView(view);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        ToolsUtil.setTransparent(activity,popupWindow);

        //显示相关
        popupWindow.setOutsideTouchable(false);//点击外面不可被销毁
        popupWindow.showAtLocation(view
                , Gravity.CENTER, 0, 0);

        //播放动画
        AVLoadingIndicatorView avi = view.findViewById(R.id.loading_avi);
        TextView tv=view.findViewById(R.id.loading_text);
        tv.setText(text);
        avi.smoothToShow();
        return popupWindow;
    }

}
