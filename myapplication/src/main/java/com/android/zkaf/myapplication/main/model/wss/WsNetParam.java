package com.android.zkaf.myapplication.main.model.wss;

public class WsNetParam {

    /**
     * DHCP : 1 0:自动获取 IP 1:固定 IP。设为自动获取时,其他字段可以省略
     * ip : 192.168.1.10 设备 IP 地址
     * submask : 255.225.255.0 子网掩码
     * gateway : 192.168.1.1 网关
     * dns : 192.168.1.1 DNS服务器
     */

    private String DHCP;
    private String ip;
    private String submask;
    private String gateway;
    private String dns;

    public String getDHCP() {
        return DHCP;
    }

    public void setDHCP(String DHCP) {
        this.DHCP = DHCP;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSubmask() {
        return submask;
    }

    public void setSubmask(String submask) {
        this.submask = submask;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getDns() {
        return dns;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }
}