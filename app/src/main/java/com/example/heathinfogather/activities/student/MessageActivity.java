package com.example.heathinfogather.activities.student;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heathinfogather.R;
import com.example.heathinfogather.beans.UserStatusBean;
import com.example.heathinfogather.utils.GlobalManager;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageActivity extends AppCompatActivity {

    private LinearLayout tvCardId,tvFaceId,tvTempId;
    private TextView tvCardTime,tvFaceTime,tvTempTime;
    private TextView tvCardContent,tvFaceContent,tvTempContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        init();
        loadInfo();
    }

    private void init() {
        ImageButton ivBtn=findViewById(R.id.home_student_edit_back);
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

        tvCardId = findViewById(R.id.home_student_message_cardId);
        tvFaceId = findViewById(R.id.home_student_message_faceId);
        tvTempId = findViewById(R.id.home_student_message_tempId);

        tvCardTime = findViewById(R.id.home_student_message_cardTime);
        tvTempTime = findViewById(R.id.home_student_message_tempTime);
        tvFaceTime = findViewById(R.id.home_student_message_faceTime);

        tvCardContent = findViewById(R.id.home_student_message_cardContent);
        tvTempContent = findViewById(R.id.home_student_message_tempContent);
        tvFaceContent = findViewById(R.id.home_student_message_faceContent);
    }

    private void loadInfo(){
        String time= new SimpleDateFormat("MM月dd日 HH:mm").format(new Date());
        String smallTime=time.substring(time.indexOf(" ")+1);
        if(smallTime.compareTo("07:00")>0){
            UserStatusBean.DataBean status=GlobalManager.status;
            if(status.isIs_card()!=1){
                tvCardId.setVisibility(View.VISIBLE);
                tvCardTime.setText(time);
                tvCardContent.setText("您还没打卡，请尽快打卡！");
            }

            if(status.isIs_checked()!=1){
                tvFaceId.setVisibility(View.VISIBLE);
                tvFaceTime.setText(time);
                tvFaceContent.setText("您还进行人脸识别，请尽快识别！");
            }

            if(status.getTemprature().compareTo("36.0")<0||status.getTemprature().compareTo("37.2")>0){
                tvTempId.setVisibility(View.VISIBLE);
                tvTempTime.setText(time);
                tvTempContent.setText("您的体温有异常，请如实上报原因！");
            }
        }

    }


}
