package com.android.zkaf.webrtcjavacoderbeichen.UDP;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UDPClient {
    private static volatile UDPClient instance;
    private DatagramSocket socket;
    private InetAddress address;
    private int port;

    // 私有化构造函数，防止外部直接实例化
    private UDPClient(String target) {
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName(target);  // 这里替换为目标服务器的 IP 地址
            port = 12345;  // 目标服务器的端口号
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取单例实例的方法
    public static UDPClient getInstance(String target) {
        if (instance == null) {
            synchronized (UDPClient.class) {
                if (instance == null) {
                    instance = new UDPClient(target);
                }
            }
        }
        return instance;
    }

    // 发送消息的方法
    public void sendMessage(String message) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                byte[] buffer = message.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // 接收消息的方法
    public void receiveMessage() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            byte[] buffer = new byte[4096];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                socket.receive(packet);
                Log.d("UDPClient receiveMessage", "receiveMessage: " + new String(packet.getData(), 0, packet.getLength()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // 关闭套接字的方法
    public void close() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}

