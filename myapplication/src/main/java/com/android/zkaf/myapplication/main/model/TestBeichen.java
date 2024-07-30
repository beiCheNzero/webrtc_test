package com.android.zkaf.myapplication.main.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class TestBeichen {
    private Long Id;

    private String testUserName;

    private String testPassword;



    @Generated(hash = 987837067)
    public TestBeichen() {
    }

    @Generated(hash = 1585934295)
    public TestBeichen(Long Id, String testUserName, String testPassword) {
        this.Id = Id;
        this.testUserName = testUserName;
        this.testPassword = testPassword;
    }

    public String getTestUserName() {
        return this.testUserName;
    }

    public void setTestUserName(String testUserName) {
        this.testUserName = testUserName;
    }

    public String getTestPassword() {
        return this.testPassword;
    }

    public void setTestPassword(String testPassword) {
        this.testPassword = testPassword;
    }

    public Long getId() {
        return this.Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }
}
