package com.android.zkaf.myapplication.main.model.wss;

/**
 * 服务器返回命令执行结果
 *
 * @param <T>
 */
public class WssBaseResult<T> {


    /**
     * cmd : checkSign
     * body : {}
     * errcode : 0
     * errmsg : ok
     */

    private String cmd;


    private String body;
    private int errcode;
    private String errmsg;
    private long id;
    private String cmdId;


    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }


    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
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
