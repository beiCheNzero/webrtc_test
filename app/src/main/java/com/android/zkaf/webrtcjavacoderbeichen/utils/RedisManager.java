package com.android.zkaf.webrtcjavacoderbeichen.utils;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.resps.StreamEntry;
import redis.clients.jedis.util.SafeEncoder;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.util.Log;

public class RedisManager {
//   private static RedisManager instance;
//   private static ExecutorService executorService = Executors.newSingleThreadExecutor();
//   private static final ExecutorService fetchExecutorService = Executors.newSingleThreadExecutor();
//   private static Jedis jedis;
//   private static Jedis jedis2;
//   final String host = "192.168.1.112";
//   final int port = 6379;
//   private Gson gson;
//   private DataModels userDataModels = null;
//   private DataModels targetDataModels = null;
//
//   private String targetName;
//   private String userName;
//
//   public String getUserName() {
//      return userName;
//   }
//
//   public void setUserName(String userName) {
//      this.userName = userName;
//   }
//
//   private Activity activity;
//
//   public Activity getActivity() {
//      return activity;
//   }
//
//   public void setActivity(Activity activity) {
//      this.activity = activity;
//   }
//
//   public RedisManager() {
//      executorService = Executors.newFixedThreadPool(10);
//      gson = new Gson();
//      connect();
//   }
//
//   public static synchronized RedisManager getInstance() {
//      if (instance == null) {
//         instance = new RedisManager();
//      }
//      return instance;
//   }
//
//   public String getTargetName() {
//      return targetName;
//   }
//
//   public void setTargetName(String targetName) {
//      this.targetName = targetName;
//   }
//
//   private void connect() {
//      // 连接到Redis服务器
//      jedis = new Jedis(host, port);
//      jedis2 = new Jedis(host, port);
//   }
//
//   public void setAsync(String key, String value) {
//      CompletableFuture.runAsync(() -> {
//         try {
//            jedis.set(key, value);
//            Log.d("test", "setAsync: " + "Key" + key +  "set to" + value);
//         } catch (Exception e) {
//            e.printStackTrace();
//         }
//      }, executorService);
//   }
//
//   public CompletableFuture<String> getAsync(String key) {
//      return CompletableFuture.supplyAsync(() -> {
//         try {
//            return jedis.get(key);
//         } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//         }
//      }, executorService);
//   }
//
//   public void set(String key, String value) {
//      jedis.set(key, value);
//   }
//
//   public static String get(String key) {
//      return jedis.get(key);
//   }
//
//   public CompletableFuture<Boolean> existsAsync(String key, Activity activity) {
//      return CompletableFuture.supplyAsync(() -> {
//         try {
//            return jedis.exists(key);
//         } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//         }
//      }, executorService).thenApply(exists -> {
//         activity.runOnUiThread(() -> {
//            String message = exists ? "Key '" + key + "' exists" : "Key '" + key + "' does not exist";
//            Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
//         });
//         Log.d("test", "exists===" + exists);
//         return exists;
//      });
//   }
//
//   public void observeIncomingLatestEvent(NewEventCallBack newEventCallBack, String userName) {
//      CompletableFuture.runAsync(() -> {
//         try {
////            // 订阅键空间通知
//            jedis2.psubscribe(new JedisPubSub() {
//               @Override
//               public void onPMessage(String pattern, String channel, String message) {
//                  Log.d("test", "Received key event - pattern: " + pattern + ", channel: " + channel + ", message: " + message);
//   //               Log.d("test", "onPMessage: " + jedis.exists(Utils.getSubstringAfterColon(channel)));
//                  RedisManager.getInstance().getAsync(Utils.getSubstringAfterColon(channel)).thenApply(value -> {
//                     // 解析 JSON 字符串
//                     JsonObject jsonObject = JsonParser.parseString(value).getAsJsonObject();
//                     targetDataModels = gson.fromJson(jsonObject, DataModels.class);
//   //                     Log.d("test", "onPMessage: value" + jsonObject.get("sender").getAsString());
//   //                     Log.d("test", "onPMessage: value" + jsonObject.get("target").getAsString());
//                     Log.d("test", "onPMessage: value" + gson.fromJson(jsonObject, DataModels.class).getSender());
//                     Log.d("test", "onPMessage: value" + gson.fromJson(jsonObject, DataModels.class).getTarget());
//                     Log.d("test", "onPMessage: userName" + userName);
//                     Log.d("test", "onPMessage: userName" + (targetDataModels != null && channel.equals("__keyspace@0__:" + userName) && Objects.equals(targetDataModels.getTarget(), userName)));
//                     // 根据需要处理键空间通知事件
//                     if (targetDataModels != null && channel.equals("__keyspace@0__:" + userName) && Objects.equals(targetDataModels.getTarget(), userName)) {
//                        setUserName(userName);
//                        fetchKeyAndNotify(newEventCallBack);
//                        // 当键被设置、更新或删除时，message 包含事件类型（如 set、expired）
//                        // 可以根据需要处理这些事件
//   //                     RedisManager redisManager = RedisManager.getInstance();
//   //                     String value = String.valueOf(redisManager.getAsync(redisManager.getTargetName()));
//   //                     Log.d("test", "Key value: " + JSON.toJSONString(value));
//   //
//   //                     DataModels dataModels = gson.fromJson(value, DataModels.class);
//   //                     Log.d("test", "Parsed DataModels: " + dataModels);
//   //
//   //                     activity.runOnUiThread(() -> {
//   //                        newEventCallBack.onNewEventReceived(dataModels);
//   //                        Log.d("test", "Callback executed with dataModels: " + dataModels);
//   //                     });
//   //                     Log.d("test", "Key event received: " + message);
//   //                     String value = redisManager.get(redisManager.getTargetName());
//   //                     // 可以根据需要处理这些事件，例如通过回调传递到主线程处理
//   //                     DataModels dataModels = gson.fromJson(value, DataModels.class);
//   //                     activity.runOnUiThread(() -> newEventCallBack.onNewEventReceived(dataModels));
//   //                     newEventCallBack.onNewEventReceived(dataModels);
//                     }
//                     return null;
//                  });
//   //               Log.d("test", "aaaaaaaaaaaaaaaa===" + (channel.equals("__keyspace@0__:" + userName)));
//   //               Log.d("test", "aaaaaaaaaaaaaaaa===" + RedisManager.getInstance().getUserName());
//               }
//            }, "__keyspace*__:*"); // 监听所有键空间通知事件
//         } catch (Exception e) {
//            e.printStackTrace();
//         } finally {
//            if (jedis != null) {
//               jedis.close();
//            }
//         }
//      }, executorService);
//   }
//
//   private void fetchKeyAndNotify(NewEventCallBack newEventCallBack) {
//      CompletableFuture.runAsync(() -> {
//         try {
//            Log.d("test", "fetchKeyAndNotify: " + jedis.exists(userName));
//            RedisManager.getInstance().getAsync(userName).thenApply(value -> {
////               JsonObject jsonObject = JsonParser.parseString(value).getAsJsonObject();
//               userDataModels = gson.fromJson(value, DataModels.class);
//
//               Log.d("test", "targetName value: " + gson.fromJson(value, DataModels.class));
////               Log.d("test", "targetName value: " + gson.fromJson(jsonObject, DataModels.class));
//               Log.d("test", "targetName value: " + userName);
//               Log.d("test", "Key value: " + userDataModels);
//               Log.d("test", "Key activity: " + activity);
////
//               activity.runOnUiThread(() -> {
//                  Log.d("test", "Callback executed with dataModels: " + userDataModels);
//                  newEventCallBack.onNewEventReceived(userDataModels, userName);
//               });
//               return null;
//            });
//         } catch (Exception e) {
//            Log.e("test", "Error while getting value or parsing JSON", e);
//         } finally {
//            RedisManager.getInstance().close();
//         }
//      }, fetchExecutorService);
//   }
//
//   public static void close() {
//      jedis.close();
//   }
//
//
//
//   public void shutdown() {
//      executorService.shutdown();
//      try {
//         if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
//            executorService.shutdownNow();
//         }
//      } catch (InterruptedException e) {
//         executorService.shutdownNow();
//      }
//   }
}
