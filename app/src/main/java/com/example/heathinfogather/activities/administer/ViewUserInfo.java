package com.example.heathinfogather.activities.administer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.heathinfogather.R;
import com.example.heathinfogather.beans.UserInfoBean;
import com.example.heathinfogather.utils.GlobalManager;

import java.util.List;

public class ViewUserInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_info);
        init();
    }

    private void init(){

        int index = getIntent().getIntExtra("pos", 0);
        UserInfoBean.DataBean user = GlobalManager.usersInfo.getUser().get(index);

        TextView tvName=findViewById(R.id.home_admin_view_name);
        TextView tvNumber=findViewById(R.id.home_admin_view_number);
        TextView tvGrade=findViewById(R.id.home_admin_view_grade);
        TextView tvClass=findViewById(R.id.home_admin_view_class);
        TextView tvPro=findViewById(R.id.home_admin_view_profession);
        TextView tvAca=findViewById(R.id.home_admin_view_academy);
        TextView tvPhone=findViewById(R.id.home_admin_view_phone);

        //设置
        tvName.setText(user.getName());
        tvNumber.setText(user.getNumber());
        tvGrade.setText(user.getGrade());
        tvClass.setText(user.getClassX());
        tvAca.setText(user.getAcademy());
        tvPro.setText(user.getProfession());
        tvPhone.setText(user.getPhone());

        ImageButton btnBack = findViewById(R.id.home_student_edit_back);
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

    }
}
