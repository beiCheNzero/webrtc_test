package com.android.zkaf.myapplication.main.model.wss;

/**
 * 用户上传
 */
public class WssUser {

    /**
     * deviceSn : 2911A95040600149
     * eTime : 1690524915383
     * sTime : 1595916915383
     * userCode : 20621
     * userName : 方杜成
     * userPassword : 123456
     * file : ”xxxxxxxxxxxx”
     */

    private String deviceSn;
    private long eTime;
    private long sTime;
    private String userCode;
    private String userName;
    private String userPassword;
    private String file;
    private String sn;
    private String card;
//    private File file;

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public long getETime() {
        return eTime;
    }

    public void setETime(long eTime) {
        this.eTime = eTime;
    }

    public long getSTime() {
        return sTime;
    }

    public void setSTime(long sTime) {
        this.sTime = sTime;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public long geteTime() {
        return eTime;
    }

    public void seteTime(long eTime) {
        this.eTime = eTime;
    }

    public long getsTime() {
        return sTime;
    }

    public void setsTime(long sTime) {
        this.sTime = sTime;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }
}
