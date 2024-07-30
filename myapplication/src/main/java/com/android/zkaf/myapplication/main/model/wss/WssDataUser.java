package com.android.zkaf.myapplication.main.model.wss;

/**
 * 用户上传
 */
public class WssDataUser {

    /**
     * deviceSn : 2911A95040600149
     * eTime : 1690524915383
     * sTime : 1595916915383
     * userPassword : 123456
     * file : ”xxxxxxxxxxxx”
     */


    private String idcard;// '',
    private String name;// u '\u6d4b1',
    private String img;// '/9j/4AAQSkZJRgABAQAAAQABAAD/...',
    private int privilege;// 0,
    private String pin;// u '111222333',
    private long etime;// '1693548499402',
    private String userPassword;// u '',
    private long stime;// '1598940499402',

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getPrivilege() {
        return privilege;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public long getEtime() {
        return etime;
    }

    public void setEtime(long etime) {
        this.etime = etime;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public long getStime() {
        return stime;
    }

    public void setStime(long stime) {
        this.stime = stime;
    }
}
