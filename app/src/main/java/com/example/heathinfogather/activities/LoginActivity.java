package com.example.heathinfogather.activities;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;

import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.heathinfogather.R;
import com.example.heathinfogather.activities.administer.AdministerActivity;
import com.example.heathinfogather.activities.student.CompleteInfoActivity;
import com.example.heathinfogather.activities.student.StudentActivity;
import com.example.heathinfogather.beans.UserInfoBean;
import com.example.heathinfogather.beans.UserStatusBean;
import com.example.heathinfogather.server.RetrofitRequest;
import com.example.heathinfogather.utils.Constants;
import com.example.heathinfogather.utils.GlobalManager;
import com.example.heathinfogather.utils.SingleInstance;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edtName, edtId;
    private RadioGroup mGroup;

    private RetrofitRequest request;

    private Callback<UserInfoBean> userInfoCall;
    private Callback<UserStatusBean> userStatusCall;
    private PopupWindow mPopupWindow;

    private PopupWindow bgPnd;

    private Activity self = this;

    private Button confirmBtn;

    Handler jumpHandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what==Constants.LOGIN){
                mPopupWindow.dismiss();
                SharedPreferences sp = SingleInstance.getSP(self, "Login");
                SharedPreferences.Editor edit = sp.edit();
                boolean firstLogin = sp.getBoolean("firstLogin", true);
                edit.putInt("loginId", 0);
                edit.putString("name", edtName.getText().toString());
                edit.putString("id", edtId.getText().toString());
                edit.putBoolean("autoLogin", true);
                if (firstLogin) {
                    //如果是以学生身份第一次登陆
                    edit.putBoolean("firstLogin", false);
                    //进入完善信息页面
                    Intent intent = new Intent(self, CompleteInfoActivity.class);
                    self.startActivity((new Intent(self, StudentActivity.class)));
                    self.startActivity(intent);
                } else {
                    self.startActivity((new Intent(self, StudentActivity.class)));
                }
                edit.apply();
                finish();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                autoJump();
            }
        }, 50);

    }

    Handler mHandler = new Handler();

    private void callBackInit() {
        userInfoCall = new Callback<UserInfoBean>() {
            @Override
            public void onResponse(Call<UserInfoBean> call, Response<UserInfoBean> response) {
                confirmBtn.setClickable(false);
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    UserInfoBean user = response.body();
                    if (user.getMsg().equals("ok")) {
                        GlobalManager.user = user.getData();
                        request.getUserStatus(GlobalManager.user.getId(), userStatusCall);
                    } else {
                        mPopupWindow.dismiss();
                        Toast.makeText(self, "用户信息输入错误", Toast.LENGTH_SHORT).show();
                        confirmBtn.setClickable(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserInfoBean> call, Throwable t) {
                mPopupWindow.dismiss();
                confirmBtn.setClickable(true);
                Toast.makeText(self, Constants.CONNECT_FAIL, Toast.LENGTH_SHORT).show();
            }
        };

        userStatusCall = new Callback<UserStatusBean>() {
            @Override
            public void onResponse(Call<UserStatusBean> call, Response<UserStatusBean> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    UserStatusBean status = response.body();
                    if (status.getMsg().equals("ok")) {
                        GlobalManager.status = status.getData();
                        final Message msg=new Message();
                        if (!"".equals(GlobalManager.user.getPic_path())) {
                            Glide.with(self).load(SingleInstance.url + GlobalManager.user.getPic_path())
                                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            msg.what=1;
                                            jumpHandler.sendMessage(msg);
                                            return true;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            msg.what=1;
                                            jumpHandler.sendMessage(msg);
                                            return true;
                                        }
                                    }).preload();
                        }else{
                            msg.what=1;
                            jumpHandler.sendMessage(msg);
                        }
                    } else {
                        confirmBtn.setClickable(true);
                        mPopupWindow.dismiss();
                        Toast.makeText(LoginActivity.this, "获取信息失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserStatusBean> call, Throwable t) {
                confirmBtn.setClickable(true);
                mPopupWindow.dismiss();
                Toast.makeText(LoginActivity.this, Constants.CONNECT_FAIL, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void init() {
        callBackInit();
        request = new RetrofitRequest();
         confirmBtn = findViewById(R.id.login_confirm);
        edtName = findViewById(R.id.login_name);
        edtId = findViewById(R.id.login_id);

        mGroup = findViewById(R.id.login_identity);
        RadioButton rbStudent = findViewById(R.id.login_student);
        RadioButton rbAdm = findViewById(R.id.login_administer);

        mGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.login_student) {
                    edtName.setHint("姓名");
                    edtId.setHint("学号");
                } else {
                    edtId.setHint("密码(888)");
                    edtName.setHint("管理员账号(666)");
                }
            }
        });

        //设置身份选择
        if (SingleInstance.getSP(this, "Login").getInt("loginId", 0) == 0)
            rbStudent.setChecked(true);
        else
            rbAdm.setChecked(true);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump();
            }
        });
    }

    private void jump() {
        String name = edtName.getText().toString();
        String number = edtId.getText().toString();

        if (mGroup.getCheckedRadioButtonId() == R.id.login_student) {
            //如果是学生
            mPopupWindow = SingleInstance.getLoadingPwd(self, "正在登陆...\n(初次加载比较慢)" );
            request.userLogin(number, name, userInfoCall);
        } else {
            //如果以管理员身份登陆
            SharedPreferences sp = SingleInstance.getSP(self, "Login");
            SharedPreferences.Editor edit = sp.edit();
            if(edtName.getText().toString().equals("666")&&
            edtId.getText().toString().equals("888")){
                edit.putString("name","666");
                edit.putString("id","888");
                edit.putInt("loginId", 1);
                edit.putBoolean("autoLogin", true);
                edit.apply();
                startActivity(new Intent(this, AdministerActivity.class));
                finish();
            }else{
                Toast.makeText(self, "管理员身份验证失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void autoJump() {
        //自动配置密码
        SharedPreferences sp = SingleInstance.getSP(this, "Login");
        if (sp.getBoolean("autoLogin", false)) {
            edtName.setText(sp.getString("name", ""));
            edtId.setText(sp.getString("id", ""));
            jump();
        }
    }

}
