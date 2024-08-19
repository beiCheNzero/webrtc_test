package com.android.zkaf.webrtcjavacoderbeichen.TCP;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.android.zkaf.webrtcjavacoderbeichen.http.WebSocketSingleton;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpServer {

    private static TcpServer instance;
    private static final int PORT = 5000;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;
    private BufferedWriter out;
    private Handler handler;
    private boolean isRunning = false;

    // Private constructor to prevent instantiation
    private TcpServer() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                // Update UI or handle received message
                String receivedMessage = (String) msg.obj;
                EventBus.getDefault().post(new WebSocketSingleton.WebSocketMessageEvent(receivedMessage));
                Log.d("test", "handleMessage: " + receivedMessage);
            }
        };
    }

    // Public method to provide access to the single instance
    public static synchronized TcpServer getInstance() {
        if (instance == null) {
            instance = new TcpServer();
        }
        return instance;
    }

    // Method to start the server
    public void startServer() {
        if (isRunning) return;
        isRunning = true;
        new Thread(new TcpServerRunnable()).start();
    }

    public void sendMessageToClient(String message) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            if (out != null) {
                try {
                    Log.d("test", "sendMessageToClient: 发送消息===" + message);
                    out.write(message);
                    out.newLine();
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Method to stop the server
    public void stopServer() {
        isRunning = false;
        try {
            if (clientSocket != null) clientSocket.close();
            if (serverSocket != null) serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class TcpServerRunnable implements Runnable {

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(PORT);
                while (isRunning) {
                    // Wait for client connection
                    clientSocket = serverSocket.accept();
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                    String message;
                    while (isRunning && (message = in.readLine()) != null) {
                        // Handle received message
                        Message uiMessage = handler.obtainMessage(0, message);
                        handler.sendMessage(uiMessage);

                        // Send response to client
//                        String response = "Server received: " + message;
//                        out.write(response);
//                        out.newLine();
//                        out.flush();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (clientSocket != null) clientSocket.close();
                    if (serverSocket != null) serverSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

