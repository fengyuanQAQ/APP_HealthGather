package com.example.heathinfogather.beans;

public class UserStatusBean {
    /**
     * code : 0
     * msg : ok
     * data : {"user_id":1,"is_card":true,"is_checked":false,"temprature":"36.5"}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean  {
        /**
         * user_id : 1
         * is_card : true
         * is_checked : false
         * temprature : 36.5
         */

        private int user_id;
        private int is_card;
        private int is_checked;
        private String temprature;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int isIs_card() {
            return is_card;
        }

        public void setIs_card(int is_card) {
            this.is_card = is_card;
        }

        public int isIs_checked() {
            return is_checked;
        }

        public void setIs_checked(int is_checked) {
            this.is_checked = is_checked;
        }

        public String getTemprature() {
            return temprature;
        }

        public void setTemprature(String temprature) {
            this.temprature = temprature;
        }
    }
}
