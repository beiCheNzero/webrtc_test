package com.android.zkaf.webrtcjavacoderbeichen.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.zkaf.myapplication.greendao.gen.DaoSession;
import com.android.zkaf.myapplication.greendao.gen.TestBeichenDao;
import com.android.zkaf.myapplication.greendao.gen.UserDao;
import com.android.zkaf.myapplication.main.model.TestBeichen;
import com.android.zkaf.myapplication.main.model.User;
import com.android.zkaf.webrtcjavacoderbeichen.R;
import com.android.zkaf.webrtcjavacoderbeichen.databinding.ActivityLoginBinding;
import com.android.zkaf.webrtcjavacoderbeichen.repository.MainRepository;
import com.android.zkaf.webrtcjavacoderbeichen.utils.RedisManager;
import com.android.zkaf.webrtcjavacoderbeichen.utils.RedisPoolManager;
import com.google.firebase.database.FirebaseDatabase;
import com.permissionx.guolindev.PermissionX;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.UnifiedJedis;

import org.greenrobot.greendao.query.Query;
import org.json.JSONStringer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.android.zkaf.myapplication.main.db.GreenDaoHelper.getDaoSession;

public class LoginActivity extends AppCompatActivity {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    private ActivityLoginBinding views;
    private MainRepository mainRepository;
    private RedisManager redisManager;
    private Jedis jedis;
    private CompletableFuture<Void> future;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        views = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(views.getRoot());

//        redisManager = RedisManager.getInstance();

        init();

//        DaoSession daoSession = getDaoSession(this);
//        UserDao userDao = daoSession.getUserDao();
//        TestBeichenDao testBeichenDao = daoSession.getTestBeichenDao();
//
//        // 插入数据
//        User user = new User();
//        user.setUsername("John Doe");
//        userDao.insert(user);
//        TestBeichen beichen = new TestBeichen();
//        beichen.setId(3L);
//        beichen.setTestUserName("beiChenZero");
//        beichen.setTestPassword("123456789");
//        testBeichenDao.insert(beichen);
//
//        // 查询数据
//        Query<User> query = userDao.queryBuilder().where(UserDao.Properties.Username.eq("John Doe")).build();
//        List<User> users = query.list();
    }

    private void waitForInitialization() {
        while (redisManager == null) {
            try {
                Thread.sleep(100); // 等待 100 毫秒
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void init() {
        mainRepository = MainRepository.getInstance();
        views.enterBtn.setOnClickListener(v -> {
            PermissionX.init(this)
                            .permissions(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                                    .request((allGranted, grantedList, deniedList)-> {
                                        if (allGranted) {
//                                            mainRepository.login(
//                                                    views.username.getText().toString(), getApplicationContext(), () -> {
//                                                        //if success then we want to move to call activity
//                                                        startActivity(new Intent(LoginActivity.this, CallActivity.class));
//                                                    }
//                                            );
                                            mainRepository.login(views.username.getText().toString(), getApplicationContext(), () -> {
                                                if (views.username.getText().toString().trim().isEmpty()) {
                                                    Toast.makeText(this, "请先填写名称", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
//                                                if (!views.username.getText().toString().equals("")) {
//                                                    Log.d("test", "views.username.getText().toString()===" + views.username.getText().toString());
                                                    // 示例操作
//                                                    RedisManager.getInstance().setAsync(views.username.getText().toString(), "");
//                                                    RedisPoolManager.getInstance().setAsync(views.username.getText().toString(), "");
//                                                    RedisPoolManager.getInstance().setAsync(views.username.getText().toString(), "").thenAccept(success -> {
//                                                        if (success) {
//                                                            Log.d("test", "init: set success");
                                                Intent intent = new Intent(LoginActivity.this, CallActivity.class);
                                                intent.putExtra("username", views.username.getText().toString());
                                                startActivity(intent);
//                                                        }
//                                                    });
//                                                }
                                            });
                                        }
                                    });
        });
    }
}