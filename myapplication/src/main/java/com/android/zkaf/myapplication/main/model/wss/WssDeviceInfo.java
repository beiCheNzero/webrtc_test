package com.android.zkaf.myapplication.main.model.wss;

public class WssDeviceInfo {
    private String sn;
    private String ipaddress;
    private String vender;
    private String model; // 模式
    private int user;//用户数
    private int face;//人脸数
    private int record;//考勤记录数
    private String version;
    private int face_width = 0;
    private int max_comm_count = 10;
    private int max_comm_size = 0;

    private int type;//0 默认考勤设备 1会议机 2消费机 3访客机

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }


    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }


    public String getVender() {
        return vender;
    }

    public void setVender(String vender) {
        this.vender = vender;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getFace() {
        return face;
    }

    public void setFace(int face) {
        this.face = face;
    }


    public int getRecord() {
        return record;
    }

    public void setRecord(int record) {
        this.record = record;
    }


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getFace_width() {
        return face_width;
    }

    public void setFace_width(int face_width) {
        this.face_width = face_width;
    }

    public int getMax_comm_count() {
        return max_comm_count;
    }

    public void setMax_comm_count(int max_comm_count) {
        this.max_comm_count = max_comm_count;
    }

    public int getMax_comm_size() {
        return max_comm_size;
    }

    public void setMax_comm_size(int max_comm_size) {
        this.max_comm_size = max_comm_size;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "WssDeviceInfo{" +
                "sn='" + sn + '\'' +
                ", ipaddress='" + ipaddress + '\'' +
                ", vender='" + vender + '\'' +
                ", model='" + model + '\'' +
                ", user=" + user +
                ", face=" + face +
                ", record=" + record +
                ", version='" + version + '\'' +
                ", face_width=" + face_width +
                ", max_comm_count=" + max_comm_count +
                ", max_comm_size=" + max_comm_size +
                '}';
    }
}
