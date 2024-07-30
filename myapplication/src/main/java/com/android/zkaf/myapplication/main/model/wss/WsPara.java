package com.android.zkaf.myapplication.main.model.wss;

public class WsPara {

    /**
     * workMode : 0
     * doorDelayTime : null
     * antifea : true
     * score : 2
     * soundSuccess : 2
     * soundStranger : 2
     * light : false
     * cardFormat : 0
     * successUnlock : true
     * qrcode : null
     * language : 0
     * recordImage : false
     * circulationTime : null
     * wiegand : 0
     * wiegandOutput : 0
     * livingBody : 0
     * threshold : 0.55
     * lock : 0.13
     * maintenanceTime : 3
     * delayDoor : null
     * network : 1
     * protocolVersion : 1
     * modifMac : null
     */

    private int workMode;
    private String doorDelayTime;
    private boolean antifea;
    private int score;
    private int soundSuccess;
    private int soundStranger;
    private boolean light;
    private int cardFormat;
    private boolean successUnlock;
    private String qrcode;
    private int language;
    private boolean recordImage;
    private String circulationTime;
    private int wiegand;
    private int wiegandOutput;
    private int livingBody;
    private String threshold;
    private String lock;
    private String maintenanceTime;
    private String delayDoor;
    private int network;
    private int protocolVersion;
    private String modifMac;

    public int getWorkMode() {
        return workMode;
    }

    public void setWorkMode(int workMode) {
        this.workMode = workMode;
    }

    public String getDoorDelayTime() {
        return doorDelayTime;
    }

    public void setDoorDelayTime(String doorDelayTime) {
        this.doorDelayTime = doorDelayTime;
    }

    public boolean isAntifea() {
        return antifea;
    }

    public void setAntifea(boolean antifea) {
        this.antifea = antifea;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getSoundSuccess() {
        return soundSuccess;
    }

    public void setSoundSuccess(int soundSuccess) {
        this.soundSuccess = soundSuccess;
    }

    public int getSoundStranger() {
        return soundStranger;
    }

    public void setSoundStranger(int soundStranger) {
        this.soundStranger = soundStranger;
    }

    public boolean isLight() {
        return light;
    }

    public void setLight(boolean light) {
        this.light = light;
    }

    public int getCardFormat() {
        return cardFormat;
    }

    public void setCardFormat(int cardFormat) {
        this.cardFormat = cardFormat;
    }

    public boolean isSuccessUnlock() {
        return successUnlock;
    }

    public void setSuccessUnlock(boolean successUnlock) {
        this.successUnlock = successUnlock;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public boolean isRecordImage() {
        return recordImage;
    }

    public void setRecordImage(boolean recordImage) {
        this.recordImage = recordImage;
    }

    public String getCirculationTime() {
        return circulationTime;
    }

    public void setCirculationTime(String circulationTime) {
        this.circulationTime = circulationTime;
    }

    public int getWiegand() {
        return wiegand;
    }

    public void setWiegand(int wiegand) {
        this.wiegand = wiegand;
    }

    public int getWiegandOutput() {
        return wiegandOutput;
    }

    public void setWiegandOutput(int wiegandOutput) {
        this.wiegandOutput = wiegandOutput;
    }

    public int getLivingBody() {
        return livingBody;
    }

    public void setLivingBody(int livingBody) {
        this.livingBody = livingBody;
    }

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public String getMaintenanceTime() {
        return maintenanceTime;
    }

    public void setMaintenanceTime(String maintenanceTime) {
        this.maintenanceTime = maintenanceTime;
    }

    public String getDelayDoor() {
        return delayDoor;
    }

    public void setDelayDoor(String delayDoor) {
        this.delayDoor = delayDoor;
    }

    public int getNetwork() {
        return network;
    }

    public void setNetwork(int network) {
        this.network = network;
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public String getModifMac() {
        return modifMac;
    }

    public void setModifMac(String modifMac) {
        this.modifMac = modifMac;
    }
}