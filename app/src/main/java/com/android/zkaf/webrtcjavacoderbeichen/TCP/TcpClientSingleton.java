package com.android.zkaf.webrtcjavacoderbeichen.TCP;

import android.util.Log;

import com.android.zkaf.webrtcjavacoderbeichen.http.WebSocketSingleton;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpClientSingleton {
    private static TcpClientSingleton instance;
    private static final Object lock = new Object();
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ExecutorService executor;
    private CountDownLatch initializationLatch = new CountDownLatch(1);
    private boolean initialized = false;

    private TcpClientSingleton(String ipAddress, int port) {
        executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                socket = new Socket(ipAddress, port);
                out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                initialized = true;
                receiveAndPrintMessage();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                initializationLatch.countDown();
            }
        });
    }

    public static TcpClientSingleton getInstance(String ipAddress, int port) {
        synchronized (lock) {
            if (instance == null) {
                instance = new TcpClientSingleton(ipAddress, port);
            }
            return instance;
        }
    }

    public void sendMessage(String message) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                initializationLatch.await();  // Wait until initialization is complete
                if (initialized && out != null) {
                    out.println(message);
                } else {
                    // Handle the case where the connection is not initialized
                    System.err.println("Connection is not initialized.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        });
    }

    //  接收消息的方法
    public void receiveAndPrintMessage() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    // Handle received message
                    handleMessage(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void handleMessage(String message) {
        // This method will be called on a background thread, so use runOnUiThread to update UI if needed
        // Example: runOnUiThread(() -> textView.setText(message));
        System.out.println("Received from server: " + message);
        EventBus.getDefault().post(new WebSocketSingleton.WebSocketMessageEvent(message));
    }

    public void close() {
        try {
            if (socket != null) socket.close();
            if (out != null) out.close();
            if (in != null) in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }

//    private static TcpClientSingleton instance;
//    private Socket socket;
//    private PrintWriter out;
//    private BufferedReader in;
//
//    // 私有构造函数
//    private TcpClientSingleton(String ipAddress, int port) {
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.submit(() -> {
//            try {
//                socket = new Socket(ipAddress, port);
//                out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
//                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                receiveAndPrintMessage();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//    }
//
//    // 获取实例的方法
//    public static synchronized TcpClientSingleton getInstance(String ipAddress, int port) {
//        if (instance == null) {
//            instance = new TcpClientSingleton(ipAddress, port);
//        }
//        return instance;
//    }
//
//    // 发送消息的方法
//    public void sendMessage(String message) {
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.submit(() -> {
//            // 执行网络操作
//            if (out != null) {
//                Log.d("test", "sendMessage: " + message);
//                out.println(message);
//            }
//        });
//    }
//
//    // 接收消息的方法
//    public void receiveAndPrintMessage() {
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.submit(() -> {
//            try {
//                String message;
//                while ((message = in.readLine()) != null) {
//                    // Handle received message
//                    handleMessage(message);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//    }
//
//    private void handleMessage(String message) {
//        // This method will be called on a background thread, so use runOnUiThread to update UI if needed
//        // Example: runOnUiThread(() -> textView.setText(message));
//        System.out.println("Received from server: " + message);
//        EventBus.getDefault().post(new WebSocketSingleton.WebSocketMessageEvent(message));
//    }
//
//
//    // 关闭连接的方法
//    public void closeConnection() {
//        try {
//            if (out != null) {
//                out.close();
//            }
//            if (in != null) {
//                in.close();
//            }
//            if (socket != null) {
//                socket.close();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}

