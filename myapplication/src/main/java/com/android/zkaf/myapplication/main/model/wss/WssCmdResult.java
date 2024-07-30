package com.android.zkaf.myapplication.main.model.wss;

/**
 * 命令执行结果
 */
public class WssCmdResult {

    /**
     * data :
     * id : 576
     * value : 1
     * sn : ”2911A95040600149”
     */

    private String data;
    private long id;
    private int value;
    private String sn;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }
}
