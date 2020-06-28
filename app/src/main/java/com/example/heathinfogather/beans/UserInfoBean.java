package com.example.heathinfogather.beans;

import com.google.gson.annotations.SerializedName;

public class UserInfoBean {

    /**
     * code : 0
     * msg : ok
     * data : {"id":1,"number":"2017213050","name":"赵洋","class":"08051703","grade":2017,"phone":"12345678910","profession":"物联网工程","academy":"自动化"}
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

    public static class DataBean {
        /**
         * id : 1
         * number : 2017213050
         * name : 赵洋
         * class : 08051703
         * grade : 2017
         * phone : 12345678910
         * profession : 物联网工程
         * academy : 自动化
         */

        private int id;
        private String number;
        private String name;
        @SerializedName("class")
        private String classX;
        private String grade;
        private String phone;
        private String profession;
        private String academy;
        private String pic_path;

        public String getPic_path() {
            return pic_path;
        }

        public void setPic_path(String pic_path) {
            this.pic_path = pic_path;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getClassX() {
            return classX;
        }

        public void setClassX(String classX) {
            this.classX = classX;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getProfession() {
            return profession;
        }

        public void setProfession(String profession) {
            this.profession = profession;
        }

        public String getAcademy() {
            return academy;
        }

        public void setAcademy(String academy) {
            this.academy = academy;
        }
    }
}
