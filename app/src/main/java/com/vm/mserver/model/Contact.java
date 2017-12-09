package com.vm.mserver.model;

/**
 * Created by VanManh on 04-Dec-17.
 */

public class Contact {
    private int id;
    private String name;
    private String phoneNum;

    public Contact(int id, String name, String phoneNum) {
        this.id = id;
        this.name = name;
        this.phoneNum = phoneNum;
    }

    public Contact(String name, String phoneNum) {
        this.name = name;
        this.phoneNum = phoneNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @Override
    public String toString() {
        return "Tên: "+this.name + "\n\nSố điện thoại: " + this.phoneNum+"\n";
    }

    public boolean checkValidate() {
        if (this.name == null || this.phoneNum == null)
            return false;

        if (this.name.equals("") || this.phoneNum.equals(""))
            return false;
        try {
            Long.parseLong(this.phoneNum);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
