package com.example.heathinfogather.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UsersBean {


    /**
     * code : 0
     * data : {"status":[{"id":2,"user_id":2,"is_card":0,"is_checked":0,"temprature":"0"},{"id":3,"user_id":3,"is_card":0,"is_checked":0,"temprature":"0"},{"id":4,"user_id":4,"is_card":0,"is_checked":0,"temprature":"0"},{"id":5,"user_id":5,"is_card":0,"is_checked":0,"temprature":"0"},{"id":6,"user_id":6,"is_card":0,"is_checked":0,"temprature":"0"},{"id":7,"user_id":8,"is_card":0,"is_checked":0,"temprature":"0"}],"user":[{"id":2,"number":"2017213050","name":"赵洋","class":"08051703","grade":"2017","phone":"18184743789","profession":"物联网工程","academy":"自动化","pic_path":""},{"id":3,"number":"2017213049","name":"谭力","class":"08051703","grade":"2017","phone":"18184743789","profession":"物联网工程","academy":"自动化","pic_path":""},{"id":4,"number":"2017213034","name":"刘泽兴","class":"08051703","grade":"2017","phone":"18184743789","profession":"物联网工程","academy":"自动化","pic_path":""},{"id":5,"number":"2017213051","name":"姚科","class":"08051703","grade":"2017","phone":"18184743789","profession":"物联网工程","academy":"自动化","pic_path":""},{"id":6,"number":"2017213048","name":"任俊伟","class":"08051703","grade":"2017","phone":"18184743789","profession":"物联网工程","academy":"自动化","pic_path":""},{"id":8,"number":"2017213047","name":"马鑫渝","class":"08051701","grade":"2017","phone":"2233","profession":"数字媒体技术","academy":"通信","pic_path":"/pic/8_1592668726.jpg"}]}
     * msg : ok
     */

    private int code;
    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        private List<UserStatusBean.DataBean> status;
        private List<UserInfoBean.DataBean> user;

        public List<UserStatusBean.DataBean> getStatus() {
            return status;
        }

        public void setStatus(List<UserStatusBean.DataBean> status) {
            this.status = status;
        }

        public List<UserInfoBean.DataBean> getUser() {
            return user;
        }

        public void setUser(List<UserInfoBean.DataBean> user) {
            this.user = user;
        }
    }
}
