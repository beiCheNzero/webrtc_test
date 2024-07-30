package com.android.zkaf.webrtcjavacoderbeichen.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;

public class RedisPoolManager {
    private static RedisPoolManager instance;
    private JedisPool jedisPool;
    private Gson gson;
    private ExecutorService executorService;

    private static final ExecutorService fetchExecutorService = Executors.newSingleThreadExecutor();
    private static final String REDIS_HOST = "192.168.1.112";
    private static final int REDIS_PORT = 6379;
    private Activity activity;

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String targetName;
    private String userName;
    private RedisPoolManager() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(128);
        poolConfig.setMinIdle(16);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);
        poolConfig.setJmxEnabled(false); // 禁用 JMX

        gson = new Gson();

        this.jedisPool = new JedisPool(poolConfig, REDIS_HOST, REDIS_PORT);
        int corePoolSize = 10;
        int maximumPoolSize = 20;
        long keepAliveTime = 60L;
        this.executorService = Executors.newFixedThreadPool(10);
//        this.executorService = new ThreadPoolExecutor(
//                corePoolSize,
//                maximumPoolSize,
//                keepAliveTime,
//                TimeUnit.SECONDS,
//                new LinkedBlockingQueue<>(),
//                new ThreadPoolExecutor.CallerRunsPolicy()
//        );
    }

    public static synchronized RedisPoolManager getInstance() {
        if (instance == null) {
            instance = new RedisPoolManager();
        }
        return instance;
    }

    public void observeIncomingLatestEvent(NewEventCallBack newEventCallBack, String userName) {
        executorService.submit(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.psubscribe(
                    new JedisPubSub() {
                        @Override
                        public void onPMessage(String pattern, String channel, String message) {
                            Log.d("MyJedisPubSub", "Received key event - pattern: " + pattern + ", channel: " + channel + ", message: " + message);
                            String key = Utils.getSubstringAfterColon(channel);
                            Log.d("MyJedisPubSub", "Key: " + key);
//                            Log.d("MyJedisPubSub", "Key: " + getCurrentUserName());
                            // 根据需要处理键空间通知事件
                            // 例如，可以异步获取键的值并通知回调
//                            RedisPoolManager.getInstance().getAsync(key).thenApply(value -> {
////                                Log.d("MyJedisPubSub", "Key value: " + value);
////                                Log.d("MyJedisPubSub", "Key value: " + userName); // 当前用户
//                                Log.d("MyJedisPubSub", "Key value: " + channel.equals("__keyspace@0__:" + userName));
//                                // 处理获取到的值
//                                if (channel.equals("__keyspace@0__:" + userName)) {
////                                    RedisPoolManager.getInstance().fetchKeyAndNotify(newEventCallBack, userName);
//                                    newEventCallBack.onNewEventReceived(gson.fromJson(value, DataModels.class), userName);
//                                }
//                                return null;
//                            });

                            // 根据需要处理键空间通知事件
                            if (channel.equals("__keyspace@0__:" + userName)) {
                                RedisPoolManager.getInstance().getAsync(key).thenApply(value -> {
                                    Log.d("MyJedisPubSub", "Key value: " + value);
                                    try {
                                        DataModels dataModels = gson.fromJson(value, DataModels.class);
                                        newEventCallBack.onNewEventReceived(dataModels, userName);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    return null;
                                });
                            }
                        }
                    }, "__keyspace@0__:*"
                );
            } catch (Exception e) {
                Log.e("RedisManager", "Error during psubscribe", e);
                try {
                    Thread.sleep(5000); // Wait before retrying
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

   public void fetchKeyAndNotify(NewEventCallBack newEventCallBack, String userName) {
      CompletableFuture.supplyAsync(() -> {
         try {
            Log.d("test", "fetchKeyAndNotify: " + userName);
            RedisPoolManager.getInstance().getAsync(userName).thenApply(value -> {
//               JsonObject jsonObject = JsonParser.parseString(value).getAsJsonObject();
//               userDataModels = gson.fromJson(value, DataModels.class);

               Log.d("test", "targetName value: " + userName);

               activity.runOnUiThread(() -> {
                  Log.d("test", "Callback executed with dataModels: " + gson.fromJson(value, DataModels.class));
                  newEventCallBack.onNewEventReceived(gson.fromJson(value, DataModels.class), userName);
               });
               return null;
            });
         } catch (Exception e) {
            Log.e("test", "Error while getting value or parsing JSON", e);
         }
//         finally {
//             RedisPoolManager.getInstance().close();
//         }
         return null;
      }, executorService);
   }

    public CompletableFuture<String> getAsync(String key) {
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.get(key);
            }
        }, executorService);
    }

    public CompletableFuture<Boolean> setAsync(String key, String value) {
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                Log.d("test", "setAsync: " + key);
                jedis.set(key, value);
                return true;
            } catch (Exception e) {
                Log.e("RedisManager", "Error while setting value", e);
                return false;
            }
        }, executorService);
    }

    public CompletableFuture<Boolean> existsAsync(String key, Activity activity) {
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.exists(key);
            } catch (Exception e) {
                Log.e("RedisManager", "Error while checking key existence", e);
                return false;
            }
        }, executorService);
    }

    public void close() {
        if (jedisPool != null) {
            jedisPool.close();
        }
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
