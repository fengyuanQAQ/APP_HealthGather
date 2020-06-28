package com.example.heathinfogather.activities.student;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heathinfogather.R;
import com.example.heathinfogather.beans.UserInfoBean;
import com.example.heathinfogather.server.RetrofitRequest;
import com.example.heathinfogather.utils.GlobalManager;
import com.example.heathinfogather.utils.SingleInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CompleteInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView edtGrade,edtClass,edtPhone,edtAcademy,edtProfession;
    private List<List<String>> mLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_info);
        init();
        loadInfo();
    }

    private void init(){
        mLists=new ArrayList<>();
        mLists.add(Arrays.asList("2016","2017","2018","2019"));
        mLists.add(Arrays.asList("08051701","08051702","08051703","08051704"));
        mLists.add(Arrays.asList("自动化","计算机","通信","理学院"));
        mLists.add(Arrays.asList("物联网","电气","智能电网","人工智能","数字媒体技术"));

        ImageButton btnBack=findViewById(R.id.home_student_edit_back);
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

        Button btnSave=findViewById(R.id.home_student_edit_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isComplete())
                    saveInfo();
            }
        });

        edtGrade=findViewById(R.id.home_student_edit_grade);
        edtClass=findViewById(R.id.home_student_edit_class);
        edtPhone=findViewById(R.id.home_student_edit_phone);
        edtAcademy=findViewById(R.id.home_student_edit_academy);
        edtProfession=findViewById(R.id.home_student_edit_profession);

        edtGrade.setOnClickListener(this);
        edtProfession.setOnClickListener(this);
        edtAcademy.setOnClickListener(this);
        edtClass.setOnClickListener(this);
    }

    private void loadInfo(){
        UserInfoBean.DataBean user= GlobalManager.user;
        edtAcademy.setText(user.getAcademy());
        edtClass.setText(user.getClassX());
        edtPhone.setText(user.getPhone());
        edtGrade.setText(user.getGrade());
        edtProfession.setText(user.getProfession());
    }

    private void saveInfo(){
        UserInfoBean.DataBean user=new UserInfoBean.DataBean();
        UserInfoBean.DataBean gUser = GlobalManager.user;
        user.setId(gUser.getId());
        user.setName(gUser.getName());
        user.setNumber(gUser.getNumber());
        user.setAcademy(edtAcademy.getText().toString());
        user.setClassX(edtClass.getText().toString());
        user.setPhone(edtPhone.getText().toString());
        user.setGrade(edtGrade.getText().toString());
        user.setProfession(edtProfession.getText().toString());
        user.setPic_path(gUser.getPic_path());

        //将改变了的信息放入map中
        Map<String,String> params=new HashMap<>();
        params.put("id", String.valueOf(gUser.getId()));
        if(!user.getClassX().equals(gUser.getClassX()))
            params.put("class",user.getClassX());
        if(!user.getGrade().equals(gUser.getGrade()))
            params.put("grade",user.getGrade());
        if(!user.getAcademy().equals(gUser.getAcademy()))
            params.put("academy",user.getAcademy());
        if(!user.getProfession().equals(gUser.getProfession()))
            params.put("profession",user.getProfession());
        if(!user.getPhone().equals(gUser.getPhone()))
            params.put("phone",user.getPhone());

        new RetrofitRequest().updateUserInfo(this,params,user,SingleInstance.getLoadingPwd(
                this,"正在保存..."
        ));
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.home_student_edit_grade:
                SingleInstance.getSinglePnd(this,mLists.get(0),edtGrade);
                break;
            case R.id.home_student_edit_class:
                SingleInstance.getSinglePnd(this,mLists.get(1),edtClass);
                break;
            case R.id.home_student_edit_academy:
                SingleInstance.getSinglePnd(this,mLists.get(2),edtAcademy);
                break;
            case R.id.home_student_edit_profession:
                SingleInstance.getSinglePnd(this,mLists.get(3),edtProfession);
                break;
        }
    }

    private  boolean isComplete(){
        if (edtAcademy.getText().toString().equals("") ||
                edtClass.getText().toString().equals("") ||
                edtGrade.getText().toString().equals("") ||
                edtPhone.getText().toString().equals("") ||
                edtProfession.getText().toString().equals("")) {
            Toast.makeText(CompleteInfoActivity.this, "请先完善您的信息", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
