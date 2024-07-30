package com.android.zkaf.myapplication.main.model.wss;

public class WssPermissionGroup {

    /**
     * id : 1
     * level_name : 权限组名字
     * emp_id : 人员id
     * door_group_id : 门id
     * level_timeseg_id : ‘时间段id'
     * levelset_type : ’权限组类型‘
     * is_visitor : ‘访客权限组’
     * weekName:"时间段名称"
     * opendoor_type 验证类型
     */

    private long id;
    private String level_name;
    private long[] emp_id;
    private String door_group_id;
    private long level_timeseg_id;
    private String levelset_type;
    private String is_visitor;
    private String weekName;
    private int opendoor_type;


    public String getWeekName() {
        return weekName;
    }

    public void setWeekName(String weekName) {
        this.weekName = weekName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLevel_name() {
        return level_name;
    }

    public void setLevel_name(String level_name) {
        this.level_name = level_name;
    }


    public long[] getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(long[] emp_id) {
        this.emp_id = emp_id;
    }

    public String getDoor_group_id() {
        return door_group_id;
    }

    public void setDoor_group_id(String door_group_id) {
        this.door_group_id = door_group_id;
    }

    public long getLevel_timeseg_id() {
        return level_timeseg_id;
    }

    public void setLevel_timeseg_id(long level_timeseg_id) {
        this.level_timeseg_id = level_timeseg_id;
    }

    public String getLevelset_type() {
        return levelset_type;
    }

    public void setLevelset_type(String levelset_type) {
        this.levelset_type = levelset_type;
    }

    public String getIs_visitor() {
        return is_visitor;
    }

    public int getOpendoor_type() {
        return opendoor_type;
    }

    public void setOpendoor_type(int opendoor_type) {
        this.opendoor_type = opendoor_type;
    }

    public void setIs_visitor(String is_visitor) {
        this.is_visitor = is_visitor;
    }
}