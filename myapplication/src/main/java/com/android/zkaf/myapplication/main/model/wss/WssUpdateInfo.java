package com.android.zkaf.myapplication.main.model.wss;

/**
 * app更新信息
 */
public class WssUpdateInfo {


    /**
     * appCode : zkFace
     * apkId : 1
     * ver : 13.0
     * size : 76476461
     * md5 : 7c0dcca11f90061b98389fb74e771e80
     * path : 9999/media/apks/ZK_FACE/avms_20210322_13.0.apk
     */

    private String appCode;
    private int apkId;
    private double ver;
    private String size;
    private String md5;
    private String path;
    private String errCode;
    private String errMsg;

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public int getApkId() {
        return apkId;
    }

    public void setApkId(int apkId) {
        this.apkId = apkId;
    }

    public double getVer() {
        return ver;
    }

    public void setVer(double ver) {
        this.ver = ver;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    @Override
    public String toString() {
        return "WssUpdateInfo{" +
                "appCode='" + appCode + '\'' +
                ", apkId=" + apkId +
                ", ver=" + ver +
                ", size='" + size + '\'' +
                ", md5='" + md5 + '\'' +
                ", path='" + path + '\'' +
                ", errCode='" + errCode + '\'' +
                ", errMsg='" + errMsg + '\'' +
                '}';
    }
}