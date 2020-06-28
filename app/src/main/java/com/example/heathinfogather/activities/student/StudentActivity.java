package com.example.heathinfogather.activities.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.ViewTarget;
import com.example.heathinfogather.R;
import com.example.heathinfogather.beans.UserInfoBean;
import com.example.heathinfogather.beans.UserStatusBean;
import com.example.heathinfogather.utils.GlobalManager;
import com.example.heathinfogather.utils.SingleInstance;
import com.example.heathinfogather.utils.ToolsUtil;


import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG ="mxy" ;
    private TextView tvName,tvId,tvGrade,tvYear,tvMonth,tvClass;
    private CircleImageView ciHead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        init();
    }

    private void init(){
        tvName=findViewById(R.id.home_student_name);
        tvId=findViewById(R.id.home_student_id);
        tvGrade=findViewById(R.id.home_student_grade);
        tvClass=findViewById(R.id.home_student_class);
        tvYear=findViewById(R.id.home_student_year);
        tvMonth=findViewById(R.id.home_student_month);

        ciHead=findViewById(R.id.home_student_head);

        CardView cvDayCard = findViewById(R.id.home_student_dayCard);
        CardView cvMod = findViewById(R.id.home_student_mod);
        CardView cvMessage = findViewById(R.id.home_student_message);
        CardView cvJump=findViewById(R.id.home_student_jump);

        cvJump.setOnClickListener(this);
        cvDayCard.setOnClickListener(this);
        cvMod.setOnClickListener(this);
        cvMessage.setOnClickListener(this);

        //处理温度相关逻辑
        tempRel();
    }

    private void loadInfo(){
//        Log.d(TAG, "loadInfo: ");

        //加载用户信息
        UserInfoBean.DataBean user=GlobalManager.user;
        //加载图片
        if(!"".equals(user.getPic_path())) {
            if(!GlobalManager.hasChangeImage){
                Glide.with(this).load(SingleInstance.url+user.getPic_path())
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(ciHead);
            }else{
                Glide.with(this).load(GlobalManager.file).into(ciHead);
            }
        }

        String grade="年级:"+user.getGrade();
        String lclass="班级:"+user.getClassX();
        String name="姓名:"+user.getName();
        String id="学号:"+user.getNumber();
        tvGrade.setText(grade);
        tvName.setText(name);
        tvId.setText(id);
        tvClass.setText(lclass);

        //获取当前时间
        String[] dates = ToolsUtil.getCurrentDates();
        String year=dates[0];
        tvYear.setText(year);
        tvMonth.setText(dates[1]);
    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.home_student_dayCard:
                intent=new Intent(this, DayCardActivity.class);
                break;

            case R.id.home_student_jump:
            case R.id.home_student_mod:
                intent=new Intent(this, ModActivity.class);
                break;

            case R.id.home_student_message:
                intent=new Intent(this, MessageActivity.class);
                break;

        }
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadInfo();
    }

    private void tempRel(){
        ImageView img=findViewById(R.id.home_student_temp);
        TextView tvNum=findViewById(R.id.home_student_number);
        UserStatusBean.DataBean status = GlobalManager.status;
        String s = status.getTemprature() + "℃";
        tvNum.setText(s);
        if(status.getTemprature().compareTo("36.0")<0){
            img.setImageResource(R.mipmap.ic_cold);
            tvNum.setTextColor(getResources().getColor(R.color.colorBlue));
        }else if(status.getTemprature().compareTo("37.2")>0){
            img.setImageResource(R.mipmap.ic_hot);
            tvNum.setTextColor(getResources().getColor(R.color.colorRed));
        }else{
            img.setImageResource(R.mipmap.ic_normal);
            tvNum.setTextColor(getResources().getColor(R.color.colorGreenDark));
        }
    }
}
