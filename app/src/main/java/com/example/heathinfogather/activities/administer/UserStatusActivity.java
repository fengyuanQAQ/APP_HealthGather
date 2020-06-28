package com.example.heathinfogather.activities.administer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.example.heathinfogather.R;
import com.example.heathinfogather.adapters.UesrStatusAdapter;
import com.example.heathinfogather.beans.UserInfoBean;
import com.example.heathinfogather.beans.UserStatusBean;
import com.example.heathinfogather.beans.UsersBean;
import com.example.heathinfogather.server.RetrofitRequest;
import com.example.heathinfogather.utils.Constants;
import com.example.heathinfogather.utils.GlobalManager;
import com.example.heathinfogather.utils.ToolsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserStatusActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private PopupWindow popupWindow;
    private PullRefreshLayout mRefresh;
    private Callback<UsersBean> callback;
    private UserStatusActivity self = this;

    //自动刷新一次事件
    private TextView all, card, face, tempExp, hasExp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_status);
        init();
    }

    private void init() {
        callBackInit();

        ImageButton btnBack = findViewById(R.id.home_student_edit_back);
        recyclerView = findViewById(R.id.home_admin_rv);
        ImageButton btnEdit = findViewById(R.id.home_admin_edit);
        mRefresh = findViewById(R.id.home_admin_pull);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 150);
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow = new PopupWindow();
                View view = LayoutInflater.from(UserStatusActivity.this).inflate(R.layout.popwnd_dropdown
                        , null);
                popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setContentView(view);
                popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
                ToolsUtil.setTransparent(UserStatusActivity.this, popupWindow);

                //按钮相关
                all = view.findViewById(R.id.home_admin_all);
                card = view.findViewById(R.id.home_admin_card);
                tempExp = view.findViewById(R.id.home_admin_tempExp);
                face = view.findViewById(R.id.home_admin_face);
                hasExp = view.findViewById(R.id.home_admin_hasExp);

                hasExp.setOnClickListener(UserStatusActivity.this);
                all.setOnClickListener(UserStatusActivity.this);
                card.setOnClickListener(UserStatusActivity.this);
                tempExp.setOnClickListener(UserStatusActivity.this);
                face.setOnClickListener(UserStatusActivity.this);

                //显示相关
                popupWindow.setOutsideTouchable(true);//点击外面不可被销毁
                popupWindow.showAtLocation(view
                        , Gravity.BOTTOM, 0, 0);
            }
        });

        mRefresh.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new RetrofitRequest().userInfoGet(callback);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new UesrStatusAdapter(GlobalManager.usersInfo, this));
    }

    private void callBackInit() {
        callback = new Callback<UsersBean>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<UsersBean> call, Response<UsersBean> response) {
                mRefresh.setRefreshing(false);
                UsersBean usersBean = response.body();
                if (usersBean.getMsg().equals("ok")) {
                    GlobalManager.usersInfo = usersBean.getData();
                    //刷新数据
                    List<UserStatusBean.DataBean> datas = GlobalManager.usersInfo.getStatus();
                    List<UserStatusBean.DataBean> collections = null;
                    switch (GlobalManager.event) {
                        case All:
                            GlobalManager.event = GlobalManager.ClickEvent.All;
                            collections = datas;
                            break;
                        case Card:
                            GlobalManager.event = GlobalManager.ClickEvent.Card;
                            collections = datas.stream()
                                    .filter((UserStatusBean.DataBean data) -> data.isIs_card() != 1)
                                    .collect(Collectors.toList());
                            break;
                        case Face:
                            GlobalManager.event = GlobalManager.ClickEvent.Face;
                            collections = datas.stream()
                                    .filter((UserStatusBean.DataBean data) -> data.isIs_checked() != 1)
                                    .collect(Collectors.toList());
                            break;
                        case Temp:
                            GlobalManager.event = GlobalManager.ClickEvent.Temp;
                            collections = datas.stream()
                                    .filter((UserStatusBean.DataBean data) -> (!tempCompare(data.getTemprature())))
                                    .collect(Collectors.toList());
                            break;
                        case Exception:
                            GlobalManager.event = GlobalManager.ClickEvent.Exception;
                            collections = datas.stream()
                                    .filter((UserStatusBean.DataBean data) -> data.isIs_card() != 1 || data.isIs_checked() != 1
                                            || !tempCompare(data.getTemprature()))
                                    .collect(Collectors.toList());
                            break;

                    }
                    List<UserStatusBean.DataBean> finalCollections = collections;
                    List<UserInfoBean.DataBean> userInfo = GlobalManager.usersInfo.getUser()
                            .stream()
                            .filter((info) -> hasId(info, finalCollections))
                            .collect(Collectors.toList());
                    UsersBean.DataBean tempBeans = new UsersBean.DataBean();
                    tempBeans.setStatus(collections);
                    tempBeans.setUser(userInfo);
                    recyclerView.setAdapter(new UesrStatusAdapter(tempBeans, self));
                } else {
                    Toast.makeText(self, "获取信息失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UsersBean> call, Throwable t) {
                Toast.makeText(self, Constants.CONNECT_FAIL, Toast.LENGTH_SHORT).show();
            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        List<UserStatusBean.DataBean> datas = GlobalManager.usersInfo.getStatus();
        List<UserStatusBean.DataBean> collections;
        switch (v.getId()) {
            case R.id.home_admin_face:
                GlobalManager.event = GlobalManager.ClickEvent.Face;
                collections = datas.stream()
                        .filter((UserStatusBean.DataBean data) -> data.isIs_checked() != 1)
                        .collect(Collectors.toList());
                break;
            case R.id.home_admin_tempExp:
                GlobalManager.event = GlobalManager.ClickEvent.Temp;
                collections = datas.stream()
                        .filter((UserStatusBean.DataBean data) -> (!tempCompare(data.getTemprature())))
                        .collect(Collectors.toList());
                break;
            case R.id.home_admin_card:
                GlobalManager.event = GlobalManager.ClickEvent.Card;
                collections = datas.stream()
                        .filter((UserStatusBean.DataBean data) -> data.isIs_card() != 1)
                        .collect(Collectors.toList());
                break;
            case R.id.home_admin_all:
                GlobalManager.event = GlobalManager.ClickEvent.All;
                collections = datas;
                break;
            case R.id.home_admin_hasExp:
                GlobalManager.event = GlobalManager.ClickEvent.Exception;
                collections = datas.stream()
                        .filter((UserStatusBean.DataBean data) -> data.isIs_card() != 1 || data.isIs_checked() != 1
                                || !tempCompare(data.getTemprature()))
                        .collect(Collectors.toList());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
        List<UserInfoBean.DataBean> userInfo = GlobalManager.usersInfo.getUser()
                .stream()
                .filter((info) -> hasId(info, collections))
                .collect(Collectors.toList());
        UsersBean.DataBean tempBeans = new UsersBean.DataBean();
        tempBeans.setStatus(collections);
        tempBeans.setUser(userInfo);
        popupWindow.dismiss();
        recyclerView.setAdapter(new UesrStatusAdapter(tempBeans, this));
    }

    private boolean tempCompare(String temp) {
        return temp.compareTo("36.0") > 0 && temp.compareTo("37.2") < 0;
    }

    private boolean hasId(UserInfoBean.DataBean info, List<UserStatusBean.DataBean> l2) {
        for (UserStatusBean.DataBean status :
                l2) {
            if (info.getId() == status.getUser_id())
                return true;
        }
        return false;
    }
}
