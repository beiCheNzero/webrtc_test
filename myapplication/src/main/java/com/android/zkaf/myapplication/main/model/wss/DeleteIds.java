package com.android.zkaf.myapplication.main.model.wss;

import java.util.List;

public class DeleteIds {
    private List<Integer> ids;
    private int mode; // 1 定值 2 商品  3 计次

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public int getMode() {
        return mode;
    }
    public void setMode(int mode) {
        this.mode = mode;
    }

//    @Override
//    public String toString() {
//        return "DeleteIds{" +
//                "ids=" + ids +
//                "}";
//    }
}
