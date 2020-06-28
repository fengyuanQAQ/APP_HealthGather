package com.example.heathinfogather.activities.entities;

public class LoginBody {
    private String number;
    private String name;

    public LoginBody(String number, String name) {
        this.number = number;
        this.name = name;
    }

    public LoginBody() {
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

}
