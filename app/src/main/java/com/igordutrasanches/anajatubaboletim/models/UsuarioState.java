package com.igordutrasanches.anajatubaboletim.models;

import android.os.Build;

public class UsuarioState {
    private String userName;
    private int count;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUserName() {
        userName = "Android "+ Build.VERSION.RELEASE;
        return userName;
    }


}
