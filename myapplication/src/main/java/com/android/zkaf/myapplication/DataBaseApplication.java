package com.android.zkaf.myapplication;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.zkaf.myapplication.greendao.gen.DaoMaster;
import com.android.zkaf.myapplication.greendao.gen.DaoSession;
import com.android.zkaf.myapplication.main.db.GreenDaoHelper;

import org.greenrobot.greendao.query.QueryBuilder;

public class DataBaseApplication extends Application {
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private SQLiteDatabase db;

    private static DataBaseApplication instance;

    public static DataBaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        setDatabase();
        Log.d("test", "setDatabase");
    }

    /**
     * 设置greenDao
     */
    private void setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        GreenDaoHelper mHelper = new GreenDaoHelper(this);
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        QueryBuilder.LOG_VALUES = true;
        QueryBuilder.LOG_SQL = true;
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

}
