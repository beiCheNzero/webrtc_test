package com.android.zkaf.myapplication.main.model.wss;

/**
 * 服务器下发数据内容
 */
public class WssCmdContent {
    private String type;//内容类型
    private String data;//内容数据
    private int count = 0;//数量

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
