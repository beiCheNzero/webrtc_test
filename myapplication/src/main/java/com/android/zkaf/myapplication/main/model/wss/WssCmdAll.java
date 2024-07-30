package com.android.zkaf.myapplication.main.model.wss;

/**
 * 服务器主动下发命令
 */
public class WssCmdAll {
    private long id;
    private WssCmdContent content;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public WssCmdContent getContent() {
        return content;
    }

    public void setContent(WssCmdContent content) {
        this.content = content;
    }
}
