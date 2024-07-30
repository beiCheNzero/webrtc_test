package com.android.zkaf.myapplication.main.model.wss;

/**
 * 命令
 */
public class WssCmd {
    public static final String ADD_FACE_DEVICE = "addFaceDevice";//添加设备
    public static final String CHECK_SIGN = "checkSign";//校验合法
    public static final String UPDATE_STATUS = "updateStatus";//心跳
    public static final String ADD_FACE_DEVICE_RECORD = "addFaceDeviceRecord";//上传门禁记录
    public static final String ADD_USER = "addUser";//上传人员


    public static final String CMD_ALL = "cmdAll";
    public static final String RETURN_CMD_ALL = "returnCmd";
    public static final String DATA_USER = "data.user";//服务器下发人员
    //    public static final String RETURN_DATA_USER = "return.data.user";//服务器下发人员返回状态
    public static final String DATA_USER_REMOVE = "data.user.remove";//服务器删除人员
    //    public static final String RETURN_DATA_USER_REMOVE = "return.data.user.remove";//服务器删除人员返回状态
    public static final String REBOOT = "reboot";//服务器重启指令
    //    public static final String RETURN_REBOOT = "return.reboot";//服务器重启指令返回状态
    public static final String DEVICE_CONTROL = "device.control";//设备控制
    public static final String RE_BOOT = "reboot";//重启命令
    public static final String OPEN_DOOR = "open_door";//远程开门
    public static final String ATT_LOG = "attlog";//上传指定时间记录
    public static final String GET_USER_ALL = "getUserAll";//主动获取设备区域人员
    public static final String CLEANEMP = "cleanEmp";//清除成功
    public static final String GET_FIRMWARE_INFO = "getFirmwareInfo";//1.7获取升级包信息
    public static final String UPDATE = "update";//服务器下发更新应用
    public static final String CURFEW_TIME = "curfewTime";//时间段
    public static final String requestConsume = "requestConsume";//请求消费信息
    public static final String commoditCombo = "commoditCombo";//商品模式
    public static final String valueCombo = "valueCombo";//商品模式


    public static final String modelType = "modelType";//消费模式

    public static final String rollback = "rollback";//消费回滚

    public static final String cardCounting = "cardCounting";//计次模式

    public static final String deleteModeItems = "deleteModeItems"; // 删除指定id商品
    public static final String clearModeItems = "clearModeItems"; // 清空机器内所有模式的商品

}
