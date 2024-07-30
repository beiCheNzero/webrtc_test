package com.android.zkaf.myapplication.main.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class User {
   @Id(autoincrement = true)
   private Long id;
   private String username;
   private String password;
   private String latestEvent;


@Generated(hash = 1769723174)
public User(Long id, String username, String password, String latestEvent) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.latestEvent = latestEvent;
}
@Generated(hash = 586692638)
public User() {
}


   public Long getId() {
       return this.id;
   }
   public void setId(Long id) {
       this.id = id;
   }
   public String getUsername() {
       return this.username;
   }
   public void setUsername(String username) {
       this.username = username;
   }
   public String getPassword() {
       return this.password;
   }
   public void setPassword(String password) {
       this.password = password;
   }
public String getLatestEvent() {
    return this.latestEvent;
}
public void setLatestEvent(String latestEvent) {
    this.latestEvent = latestEvent;
}

}

