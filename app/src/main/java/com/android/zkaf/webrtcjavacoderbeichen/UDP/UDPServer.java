package com.android.zkaf.webrtcjavacoderbeichen.UDP;

import android.util.Log;

import com.android.zkaf.webrtcjavacoderbeichen.http.WebSocketSingleton;

import org.greenrobot.eventbus.EventBus;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UDPServer {
    private DatagramSocket socket;
    private InetAddress clientAddress;
    private int clientPort;
    private ExecutorService executorService;

    // 构造函数，初始化服务器
    public UDPServer(int port) {
        try {
            socket = new DatagramSocket(port);
            executorService = Executors.newSingleThreadExecutor();
            System.out.println("UDP Server started on port " + port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 启动服务器，接收消息的同时保存客户端信息
    public void start() {
        executorService.execute(() -> {
            try {
                byte[] buffer = new byte[4096];

                while (true) {
                    // 接收客户端消息
                    DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                    socket.receive(receivePacket);
                    String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    System.out.println("Received message: " + receivedMessage);
                    EventBus.getDefault().post(new WebSocketSingleton.WebSocketMessageEvent(receivedMessage));

                    // 保存客户端的 IP 和端口，供后续发送消息使用
                    clientAddress = receivePacket.getAddress();
                    clientPort = receivePacket.getPort();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            }
        });
    }

    // 主动向客户端发送消息的方法
    public void sendMessage(String message) {
        try {
            if (clientAddress != null && clientPort > 0) {
                byte[] responseBuffer = message.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(responseBuffer, responseBuffer.length, clientAddress, clientPort);
                socket.send(sendPacket);
                System.out.println("Sent message to client: " + message);
            } else {
                System.out.println("No client connected yet.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 关闭服务器
    public void stop() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        executorService.shutdown();
    }

    public static void main(String[] args) {
        UDPServer server = new UDPServer(12345); // 指定端口号
        server.start();

        // 在任意时刻调用sendMessage()来主动发送消息
        try {
            Thread.sleep(5000); // 模拟等待5秒后发送消息
            server.sendMessage("Hello from server!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 其他业务逻辑...
    }
}
