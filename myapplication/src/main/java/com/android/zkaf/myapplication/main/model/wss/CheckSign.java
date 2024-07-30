package com.android.zkaf.myapplication.main.model.wss;

/**
 * 校验设备
 */
public class CheckSign {

    /**
     * device_signature : 2911A95040600149
     */

    private String device_signature;
    private String sn;
    private int page;
    private int limit;
    private String mac;

    public String getDevice_signature() {
        return device_signature;
    }

    public void setDevice_signature(String device_signature) {
        this.device_signature = device_signature;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
