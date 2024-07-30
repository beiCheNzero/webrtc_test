package com.android.zkaf.webrtcjavacoderbeichen.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import redis.clients.jedis.JedisPubSub;

public class MyJedisPubSub {
    private Gson gson;

    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }

    private String currentUserName;

    public MyJedisPubSub() {
        gson = new Gson();
    }

    public void observeIncomingLatestEvent(NewEventCallBack newEventCallBack, String userName) {
        CompletableFuture.runAsync(() -> {
            new JedisPubSub() {
                @Override
                public void onPMessage(String pattern, String channel, String message) {
                    Log.d("MyJedisPubSub", "Received key event - pattern: " + pattern + ", channel: " + channel + ", message: " + message);
                    String key = Utils.getSubstringAfterColon(channel);
                    Log.d("MyJedisPubSub", "Key: " + key);
                    Log.d("MyJedisPubSub", "Key: " + getCurrentUserName());
                    // 根据需要处理键空间通知事件
                    // 例如，可以异步获取键的值并通知回调
                    RedisPoolManager.getInstance().getAsync(key).thenApply(value -> {
                        Log.d("MyJedisPubSub", "Key value: " + value);
                        Log.d("MyJedisPubSub", "Key value: " + getCurrentUserName());
                        Log.d("MyJedisPubSub", "Key value: " + channel.equals("__keyspace@0__:" + getCurrentUserName()));
                        DataModels dataModels = gson.fromJson(value, DataModels.class);
                        Log.d("MyJedisPubSub", "onPMessage: " + dataModels.getSender());
//            // 处理获取到的值
                        if (channel.equals("__keyspace@0__:" + getCurrentUserName())) {
//                Log.d("test", "onPMessage: " + dataModels.getSender());
                            RedisPoolManager.getInstance().fetchKeyAndNotify(newEventCallBack, dataModels.getSender());
                        }
                        return null;
                    });
                }
            };
        });
    }

//    @Override
//    public void onPMessage(String pattern, String channel, String message) {
//        Log.d("MyJedisPubSub", "Received key event - pattern: " + pattern + ", channel: " + channel + ", message: " + message);
//        String key = Utils.getSubstringAfterColon(channel);
//        Log.d("MyJedisPubSub", "Key: " + key);
//        Log.d("MyJedisPubSub", "Key: " + getCurrentUserName());
//        // 根据需要处理键空间通知事件
//        // 例如，可以异步获取键的值并通知回调
//        RedisPoolManager.getInstance().getAsync(key).thenApply(value -> {
//            Log.d("MyJedisPubSub", "Key value: " + value);
//            Log.d("MyJedisPubSub", "Key value: " + getCurrentUserName());
//            Log.d("MyJedisPubSub", "Key value: " + channel.equals("__keyspace@0__:" + getCurrentUserName()));
//            DataModels dataModels = gson.fromJson(value, DataModels.class);
//            Log.d("MyJedisPubSub", "onPMessage: " + dataModels.getSender());
////            // 处理获取到的值
//            if (channel.equals("__keyspace@0__:" + getCurrentUserName())) {
////                Log.d("test", "onPMessage: " + dataModels.getSender());
//                RedisPoolManager.getInstance().fetchKeyAndNotify(callback, dataModels.getSender());
//            }
//            return null;
//        });
//    }
}
