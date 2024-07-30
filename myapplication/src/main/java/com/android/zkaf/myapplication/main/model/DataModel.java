package com.android.zkaf.myapplication.main.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class DataModel {
   @Id
   private Long id;  // 主键

   private String target;
   private String latestEventField;
   @Generated(hash = 360610331)
   public DataModel(Long id, String target, String latestEventField) {
       this.id = id;
       this.target = target;
       this.latestEventField = latestEventField;
   }
   @Generated(hash = 1794172823)
   public DataModel() {
   }
   public Long getId() {
       return this.id;
   }
   public void setId(Long id) {
       this.id = id;
   }
   public String getTarget() {
       return this.target;
   }
   public void setTarget(String target) {
       this.target = target;
   }
   public String getLatestEventField() {
       return this.latestEventField;
   }
   public void setLatestEventField(String latestEventField) {
       this.latestEventField = latestEventField;
   }
}
