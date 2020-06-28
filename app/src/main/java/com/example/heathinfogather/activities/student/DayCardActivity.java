package com.example.heathinfogather.activities.student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.net.DnsResolver;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heathinfogather.R;
import com.example.heathinfogather.beans.CommonDataBean;
import com.example.heathinfogather.server.RetrofitRequest;
import com.example.heathinfogather.utils.GlobalManager;
import com.example.heathinfogather.utils.SingleInstance;


import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import  retrofit2.Callback;
import retrofit2.Response;

public class DayCardActivity extends AppCompatActivity implements View.OnClickListener ,Callback<CommonDataBean>{

    private List<String> listData=new ArrayList<>();
    private PopupWindow mPopupWindow;
    private Button btnClockIn;
    private TextView tvIll,tvIsIll,tvTemp,tvSchool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_card);
        init();
    }

    private void init() {
        listData=new ArrayList<>();
        listData.add("否");
        listData.add("待选择");
        listData.add("是");

        ImageButton ivBtn=findViewById(R.id.home_setting_back);
        ivBtn.setOnClickListener(new View.OnClickListener() {
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

        final TextView tvId=findViewById(R.id.home_student_dayCard_id);
        TextView tvName=findViewById(R.id.home_student_dayCard_name);
         tvIll=findViewById(R.id.home_student_dayCard_ill);
         tvIsIll=findViewById(R.id.home_student_dayCard_isIll);
         tvTemp=findViewById(R.id.home_student_dayCard_temperature);
         tvSchool=findViewById(R.id.home_student_dayCard_school);
        btnClockIn=findViewById(R.id.home_student_dayCard_clockIn);

        tvIll.setText("待选择");
        tvIsIll.setText("待选择");
        tvTemp.setText("待选择");
        tvSchool.setText("待选择");
        tvId.setText(GlobalManager.user.getNumber());
        tvName.setText(GlobalManager.user.getName());

        tvIll.setOnClickListener(this);
        tvIsIll.setOnClickListener(this);
        tvTemp.setOnClickListener(this);
        tvSchool.setOnClickListener(this);


        btnClockIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(cardAll()){
                        mPopupWindow=SingleInstance.getLoadingPwd(DayCardActivity.this,"打卡中...");
                        new RetrofitRequest().updateCardStatus(DayCardActivity.this,
                                GlobalManager.user.getId(),"1");
                    }else{
                        Toast.makeText(DayCardActivity.this, "请先完善信息", Toast.LENGTH_SHORT).show();
                    }
            }
        });

        if(GlobalManager.status.isIs_card()==1) {
            btnClockIn.setText("已打卡");
            btnClockIn.setClickable(false);
            btnClockIn.setBackgroundResource(R.color.colorGray);
        }
    }

    @Override
    public void onClick(View v) {
       SingleInstance.getSinglePnd(DayCardActivity.this,listData,(TextView) v);
    }

    @Override
    public void onResponse(Call<CommonDataBean> call, Response<CommonDataBean> response) {
        if(response.code()== HttpURLConnection.HTTP_OK) {
            mPopupWindow.dismiss();
            if (response.code() == HttpURLConnection.HTTP_OK) {
                CommonDataBean body = response.body();
                if (body.getMsg().equals("ok")) {
                    Toast.makeText(this, "打卡成功", Toast.LENGTH_SHORT).show();
                    GlobalManager.status.setIs_card(1);
                    finish();
                } else
                    Toast.makeText(this, "打卡失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFailure(Call<CommonDataBean> call, Throwable t) {
        mPopupWindow.dismiss();
        Toast.makeText(this, "连接网络失败", Toast.LENGTH_SHORT).show();
    }

    private boolean cardAll(){
        return !tvIll.getText().toString().equals("待选择") && !tvIsIll.getText().toString().equals("待选择") &&
                !tvTemp.getText().toString().equals("待选择") && !tvSchool.getText().toString().equals("待选择");
    }
}
