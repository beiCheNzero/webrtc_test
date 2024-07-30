package com.android.zkaf.webrtcjavacoderbeichen.http;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class JWebSocketClient extends WebSocketClient {
    public JWebSocketClient(URI serverUri) {
        super(serverUri, new Draft_6455());
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
//        KLog.e("JWebSocketClient", "onOpen()");
    }

    @Override
    public void onMessage(String message) {
//        KLog.e("JWebSocketClient", message + " onMessage()");
    }

    //
    @Override
    public void onClose(int code, String reason, boolean remote) {
//        KLog.e("JWebSocketClient", "onClose()");
    }

    @Override
    public void onError(Exception ex) {
//        KLog.e("JWebSocketClient", "onError()");
    }

    @Override
    public void onWebsocketPing(WebSocket conn, Framedata f) {
        super.onWebsocketPing(conn, f);
//        KLog.d("test", "onWebsocketPing");
    }

    @Override
    public void onWebsocketPong(WebSocket conn, Framedata f) {
//        super.onWebsocketPong(conn, f);
//        KLog.d("test", "onWebsocketPong");
    }
}