package com.android.zkaf.myapplication.main.model.wss;

/**
 * wss心跳
 */
public class WssUpdateStatus {

    /**
     * sn : 2911A95040600149
     * timestamp : 1595918761139
     */

    private String sn;
    private long timestamp;
    private int face;//人脸数
    private int user;//设备用户数量
    private int record;//设备存在考勤记录数


    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getFace() {
        return face;
    }

    public void setFace(int face) {
        this.face = face;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getRecord() {
        return record;
    }

    public void setRecord(int record) {
        this.record = record;
    }
}
