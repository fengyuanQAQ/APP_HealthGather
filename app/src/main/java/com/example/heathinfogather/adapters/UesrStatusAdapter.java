package com.example.heathinfogather.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heathinfogather.R;
import com.example.heathinfogather.activities.administer.ViewUserInfo;
import com.example.heathinfogather.beans.UserInfoBean;
import com.example.heathinfogather.beans.UserStatusBean;
import com.example.heathinfogather.beans.UsersBean;


public class UesrStatusAdapter extends RecyclerView.Adapter<UesrStatusAdapter.LinearViewHolder>{

    private UsersBean.DataBean datas;
    private Activity mActivity;

    public UesrStatusAdapter(UsersBean.DataBean datas, Activity activity) {
        this.datas = datas;
        mActivity = activity;
    }

    @NonNull
    @Override
    public LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.adpter_user_status,parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull LinearViewHolder holder, int position) {
        UserInfoBean.DataBean info=datas.getUser().get(position);
        UserStatusBean.DataBean status=datas.getStatus().get(position);
        holder.tvTemp.setText(status.getTemprature());
        if(status.getTemprature().compareTo("36.0")<0){
            holder.tvTemp.setTextColor(mActivity.getResources().getColor(R.color.colorBlue));
        }else if(status.getTemprature().compareTo("37.2")>0){
            holder.tvTemp.setTextColor(mActivity.getResources().getColor(R.color.colorRed));
        }

        if(status.isIs_checked()==1){
            holder.tvFace.setText("识别");
        }else{
            holder.tvFace.setTextColor(mActivity.getResources().getColor(R.color.colorRed));
            holder.tvFace.setText("未识别");
        }
        holder.tvName.setText(info.getName());
        if(status.isIs_card()==1){
            holder.tvCard.setText("打卡");
        }else{
            holder.tvCard.setTextColor(mActivity.getResources().getColor(R.color.colorRed));
            holder.tvCard.setText("未打卡");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        CardView cv = holder.itemView.findViewById(R.id.home_admin_item);
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity, ViewUserInfo.class);
                intent.putExtra("pos",position);
                mActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.getUser().size();
    }

    class LinearViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName,tvCard,tvFace,tvTemp;
        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCard=itemView.findViewById(R.id.adapter_user_card);
            tvName=itemView.findViewById(R.id.adapter_user_name);
            tvFace=itemView.findViewById(R.id.adapter_user_face);
            tvTemp=itemView.findViewById(R.id.adapter_user_temp);
        }
    }

}
