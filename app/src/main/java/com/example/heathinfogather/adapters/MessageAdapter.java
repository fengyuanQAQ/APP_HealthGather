package com.example.heathinfogather.adapters;

import android.app.Activity;
import android.content.Intent;
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


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.LinearViewHolder>{

    private UsersBean.DataBean datas;
    private Activity mActivity;

    public MessageAdapter(UsersBean.DataBean datas, Activity activity) {
        this.datas = datas;
        mActivity = activity;
    }

    @NonNull
    @Override
    public LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.adapter_message,parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull LinearViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return datas.getUser().size();
    }

    class LinearViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName,tvCard,tvFace,tvTemp;
        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }

}
