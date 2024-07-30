package com.android.zkaf.myapplication.main.model.wss;

public class WsWeb {

    /**
     * wsaddr : ws://abc.net:10200/facedevice
     */

    private String wsaddr;
    private String wsport;


    public String getWsport() {
        return wsport;
    }

    public void setWsport(String wsport) {
        this.wsport = wsport;
    }

    public String getWsaddr() {
        return wsaddr;
    }

    public void setWsaddr(String wsaddr) {
        this.wsaddr = wsaddr;
    }
}