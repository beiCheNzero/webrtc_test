package com.android.zkaf.webrtcjavacoderbeichen.http;

import android.util.Log;
import android.text.TextUtils;

import com.android.zkaf.webrtcjavacoderbeichen.utils.DataModels;
import com.android.zkaf.webrtcjavacoderbeichen.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.net.URI;
import java.net.URISyntaxException;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class WebSocketSingleton {

    private static WebSocketSingleton instance;
    private WebSocketClient client;
    private boolean isWsStatus = false;
    private String wsUrl;
    private URI wsUri;
    private Gson gson = new Gson();

    // 私有构造函数，防止外部实例化
    public static class WebSocketMessageEvent {
        public final String message;
        public WebSocketMessageEvent(String message) {
            this.message = message;
        }
    }

    // 获取单例实例的方法
    public static synchronized WebSocketSingleton getInstance() {
        if (instance == null) {
            instance = new WebSocketSingleton();
        }
        return instance;
    }

    // 初始化 WebSocket 连接
    public void initWebSocket(String serverIp, String serverPort) {
        if (isWsStatus) {
            Log.d("test", "ws 已连接");
            return;
        }
        wsUrl = serverIp + ":" + serverPort;
        try {
            wsUri = new URI("ws://" + wsUrl + "/wsFaceDevice?sn=" + Utils.getSerialNumber());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        Log.d("测试", "wsUri===" + wsUri);
        client = new WebSocketClient(wsUri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.d("test", "ws 连接成功");
                isWsStatus = true;
                // 在这里更新连接状态，例如 UI 显示
            }

            @Override
            public void onMessage(String message) {
                Log.d("test", "onMessage===" + message);
                if (TextUtils.isEmpty(message)) {
                    return;
                }
                EventBus.getDefault().post(new WebSocketMessageEvent(message));
                // 处理接收到的消息
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.d("test", "onClose");
                isWsStatus = false;
//                EventBus.getDefault().post(new WebSocketMessageEvent("connect_fail"));
                // 在这里更新连接状态，例如 UI 显示
            }

            @Override
            public void onError(Exception ex) {
                Log.d("test", "onError");
                if (client != null) {
                    isWsStatus = false;
                    client.close();
//                    EventBus.getDefault().post(new WebSocketMessageEvent("connect_fail"));
                }
                // 在这里处理错误
            }
        };

        // 启动连接
        new Thread(() -> {
            try {
                if (client.connectBlocking()) {
                    Log.d("test", "ws 连接成功");
                    isWsStatus = true;
//                    EventBus.getDefault().post(new WebSocketMessageEvent("connect_success"));
                    // 在这里更新连接状态，例如 UI 显示
                } else {
                    Log.d("test", "ws 连接失败");
                    isWsStatus = false;
//                    EventBus.getDefault().post(new WebSocketMessageEvent("connect_fail"));
                    // 在这里更新连接状态，例如 UI 显示
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.e("WebSocketSingleton", "InterruptedException---" + e.getMessage());
            }
        }).start();
    }

    // 关闭 WebSocket 连接
    public void closeWebSocket() {
        if (client != null && client.isOpen()) {
            client.close();
        }
    }

    public boolean isWsStatus() {
        return isWsStatus;
    }

    // 发送消息的方法
    public void sendMessage(String message) {
        Log.d("test", "sendMessage: aaaaaaaaaaaaaaaaaaaaaaaaa" + message);
        if (client != null && client.isOpen()) {
            try {
                DataModels dataModel = gson.fromJson(message, DataModels.class);
                Log.d("test", "sendMessage: " + dataModel.getTarget());
                Log.d("test", "sendMessage: " + dataModel.getSender());
                Log.d("test", "sendMessage: " + dataModel.getType());
                Log.d("test", "sendMessage: " + dataModel.getData());
                client.send(message);
            } catch (JsonSyntaxException e) {
                Log.e("test", "sendMessage: JSON parsing error", e);
            }
        } else {
            Log.e("WebSocketSingleton", "WebSocket is not connected");
        }
    }
}

