package com.android.zkaf.myapplication.main.model.wss;

import java.io.Serializable;

/**
 * 数据请求
 *
 * @param <T>
 */
public class WssBaseRequest<T> implements Serializable {
    private String cmd;
    private T body;
    private long id;
    private String cmdId;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCmdId() {
        return cmdId;
    }

    public void setCmdId(String cmdId) {
        this.cmdId = cmdId;
    }
}
