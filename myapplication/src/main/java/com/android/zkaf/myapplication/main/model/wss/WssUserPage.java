package com.android.zkaf.myapplication.main.model.wss;

import java.util.List;

/**
 * 用户分页
 */
public class WssUserPage {
    private List<WssCmdUser> userList;
    private int count;
    private String next;

    public List<WssCmdUser> getUserList() {
        return userList;
    }

    public void setUserList(List<WssCmdUser> userList) {
        this.userList = userList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }
}
