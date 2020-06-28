package com.example.heathinfogather.activities.administer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.heathinfogather.R;
import com.example.heathinfogather.activities.LoginActivity;
import com.example.heathinfogather.beans.UsersBean;
import com.example.heathinfogather.server.RetrofitRequest;
import com.example.heathinfogather.utils.Constants;
import com.example.heathinfogather.utils.GlobalManager;
import com.example.heathinfogather.utils.SingleInstance;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdministerActivity extends AppCompatActivity implements Callback<UsersBean> {

    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administer);
        init();
    }

    private void init() {
        Button btnQuitLogin = findViewById(R.id.home_setting_quitLogin);
        btnQuitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitLogin();
            }
        });

        CardView cv = findViewById(R.id.home_administer_all);
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalManager.isFirstLoadUsers) {
                    mPopupWindow = SingleInstance.getLoadingPwd(AdministerActivity.this, "正在加载学生信息...");
                    new RetrofitRequest().userInfoGet(AdministerActivity.this);
                }else{
                    startActivity(new Intent(AdministerActivity.this,UserStatusActivity.class));
                }
            }
        });
    }

    private void quitLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确认退出").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(AdministerActivity.this, LoginActivity.class).
                        setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                SharedPreferences sp = SingleInstance.getSP(getParent(), "Login");
                SharedPreferences.Editor edit = sp.edit();
                edit.putBoolean("autoLogin", false);
                edit.apply();
                finish();
                startActivity(intent);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setCancelable(true).show();
    }

    @Override
    public void onResponse(Call<UsersBean> call, Response<UsersBean> response) {
        if (response.code() == HttpURLConnection.HTTP_OK) {
            mPopupWindow.dismiss();
            UsersBean usersBean = response.body();
            if (usersBean.getMsg().equals("ok")) {
                GlobalManager.usersInfo = usersBean.getData();
                GlobalManager.isFirstLoadUsers = false;
                startActivity(new Intent(this, UserStatusActivity.class));
            } else {
                Toast.makeText(this, "获取信息失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFailure(Call<UsersBean> call, Throwable t) {
        mPopupWindow.dismiss();
        Toast.makeText(this, Constants.CONNECT_FAIL, Toast.LENGTH_SHORT).show();
    }
}
