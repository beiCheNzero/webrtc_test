package com.android.zkaf.myapplication.main.model.wss;

/**
 * 重启
 */
public class WssReboot {

    /**
     * command : reboot
     * sn : 2911A95040600147
     * time : 5
     */

    private String command;
    private String sn;
    private int time;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
