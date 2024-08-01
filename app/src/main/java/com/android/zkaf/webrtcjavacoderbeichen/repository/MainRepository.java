package com.android.zkaf.webrtcjavacoderbeichen.repository;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.FrameLayout;

import com.android.zkaf.myapplication.greendao.gen.DaoSession;
import com.android.zkaf.myapplication.greendao.gen.UserDao;
import com.android.zkaf.myapplication.main.model.User;
import com.android.zkaf.webrtcjavacoderbeichen.http.WebSocketSingleton;
import com.android.zkaf.webrtcjavacoderbeichen.remote.FirebaseClient;
import com.android.zkaf.webrtcjavacoderbeichen.utils.DataModelType;
import com.android.zkaf.webrtcjavacoderbeichen.utils.DataModels;
import com.android.zkaf.webrtcjavacoderbeichen.utils.ErrorCallback;
import com.android.zkaf.webrtcjavacoderbeichen.utils.NewEventCallBack;
import com.android.zkaf.webrtcjavacoderbeichen.utils.RedisPoolManager;
import com.android.zkaf.webrtcjavacoderbeichen.utils.SuccessCallback;
import com.android.zkaf.webrtcjavacoderbeichen.webrtc.MyPeerConnectionObserver;
import com.android.zkaf.webrtcjavacoderbeichen.webrtc.MySdpObserver;
import com.android.zkaf.webrtcjavacoderbeichen.webrtc.WebRTCClient;
import com.google.gson.Gson;

import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceViewRenderer;

import static com.android.zkaf.myapplication.main.db.GreenDaoHelper.getDaoSession;

import java.util.concurrent.CompletableFuture;

public class MainRepository implements WebRTCClient.Listener {


    public Listener listener;
    private final Gson gson = new Gson();
//    private final FirebaseClient firebaseClient;
    public WebRTCClient webRTCClient;
    private String currentUsername;

    private SurfaceViewRenderer remoteView;

    private String target;
    private PeerConnection peerConnection;
    private Context context;
    private Activity activity;
//    private RedisManager redisManager;

    private void updateCurrentUsername(String username) {
        this.currentUsername = username;
    }

    private MainRepository(){
    }

    private static volatile MainRepository instance;
    public static MainRepository getInstance() {
        if (instance == null) {
            instance = new MainRepository();
        }
        return instance;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void login(String username, Context context, SuccessCallback callback) {
        login(username, () -> {
            updateCurrentUsername(username);
//            this.webRTCClient = new WebRTCClient(context, new MyPeerConnectionObserver(){
//                @Override
//                public void onAddStream(MediaStream mediaStream) {
//                    super.onAddStream(mediaStream);
//                    try {
//                        Log.d("test", "mediaStream.videoTracks.get(0)===" + mediaStream.videoTracks.get(0));
//                        onRemoteStream(mediaStream, remoteView);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onConnectionChange(PeerConnection.PeerConnectionState newState) {
//                    super.onConnectionChange(newState);
//                    Log.d("test", "newState===" + newState);
//                    if (newState == PeerConnection.PeerConnectionState.CONNECTING && listener != null) {
//                        listener.webrtcConnected();
//                    }
//                    if (newState == PeerConnection.PeerConnectionState.CLOSED ||
//                            newState == PeerConnection.PeerConnectionState.DISCONNECTED
//                    ) {
//                        if (listener != null){
//                            listener.webrtcClose();
//                        }
//                    }
//                }
//
//                @Override
//                public void onIceCandidate(IceCandidate iceCandidate) {
//                    super.onIceCandidate(iceCandidate);
//                    webRTCClient.sendIceCandidate(iceCandidate, target);
//                }
//
//                @Override
//                public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
//                    super.onIceConnectionChange(iceConnectionState);
//                    Log.d("test", "iceConnectionState=" + iceConnectionState);
//                    if (iceConnectionState == PeerConnection.IceConnectionState.DISCONNECTED) {
//                        if (listener != null) {
//                            listener.webrtcClose();
//                        }
//                    }
//                }
//            }, username);
//            webRTCClient.listener = this;
            callback.onSuccess();
        });
//        firebaseClient.login(username, () -> {
//            updateCurrentUsername(username);
//            this.webRTCClient = new WebRTCClient(context, new MyPeerConnectionObserver(){
//                @Override
//                public void onAddStream(MediaStream mediaStream) {
//                    super.onAddStream(mediaStream);
//                    try {
//                        Log.d("test", "mediaStream.videoTracks.get(0)===" + mediaStream.videoTracks.get(0));
//                        mediaStream.videoTracks.get(0).addSink(remoteView);
////                        onRemoteStream(mediaStream, remoteView);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onConnectionChange(PeerConnection.PeerConnectionState newState) {
//                    super.onConnectionChange(newState);
//                    Log.d("test", "newState===" + newState);
//                    if (newState == PeerConnection.PeerConnectionState.CONNECTING && listener != null) {
//                        listener.webrtcConnected();
//                    }
//                    if (newState == PeerConnection.PeerConnectionState.CLOSED ||
//                        newState == PeerConnection.PeerConnectionState.DISCONNECTED
//                    ) {
//                        if (listener != null){
//                            listener.webrtcClose();
//                        }
//                    }
//                }
//
//                @Override
//                public void onIceCandidate(IceCandidate iceCandidate) {
//                    super.onIceCandidate(iceCandidate);
//                    webRTCClient.sendIceCandidate(iceCandidate, target);
//                }
//
//                @Override
//                public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
//                    super.onIceConnectionChange(iceConnectionState);
//                    Log.d("test", "iceConnectionState=" + iceConnectionState);
//                    if (iceConnectionState == PeerConnection.IceConnectionState.DISCONNECTED) {
////                        Log.d("test", "iceConnectionState===" + iceConnectionState);
//                        // 处理ICE连接断开
//                        if (listener != null) {
//                            listener.webrtcClose();
//                        }
//                        webRTCClient.recreateIceConnection(target);
//                    }
//                }
//            },username);
//            webRTCClient.listener = this;
//            callback.onSuccess();
//        });
    }

    public void initWebRTCClient() {
//        if (this.webRTCClient != null) {
//            Log.d("test", "initWebRTCClient: webRTCClient is not null, return");
//            return;
//        }
        this.webRTCClient = new WebRTCClient(context, new MyPeerConnectionObserver(){
            @Override
            public void onAddStream(MediaStream mediaStream) {
                super.onAddStream(mediaStream);
                try {
                    Log.d("test", "mediaStream.videoTracks.get(0)===" + mediaStream.videoTracks.get(0));
                    onRemoteStream(mediaStream, remoteView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onConnectionChange(PeerConnection.PeerConnectionState newState) {
                super.onConnectionChange(newState);
                Log.d("test", "newState===" + newState);
                if (newState == PeerConnection.PeerConnectionState.CONNECTING && listener != null) {
                    listener.webrtcConnected();
                }
                if (newState == PeerConnection.PeerConnectionState.CLOSED ||
                        newState == PeerConnection.PeerConnectionState.DISCONNECTED
                ) {
                    if (listener != null){
                        listener.webrtcClose();
                    }
                }
            }

            @Override
            public void onIceCandidate(IceCandidate iceCandidate) {
                super.onIceCandidate(iceCandidate);
                Log.d("test", "onIceCandidate: target===" + target);
                webRTCClient.sendIceCandidate(iceCandidate, target);
            }

            @Override
            public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
                super.onIceConnectionChange(iceConnectionState);
                Log.d("test", "iceConnectionState=" + iceConnectionState);
                if (iceConnectionState == PeerConnection.IceConnectionState.DISCONNECTED) {
                    if (listener != null) {
                        listener.webrtcClose();
                    }
                }
            }
        }, currentUsername);
        webRTCClient.listener = this;
    }

    public void initLocalView(SurfaceViewRenderer view) {
        webRTCClient.initLocalSurfaceView(view);
    }

    public void initRemoteView(SurfaceViewRenderer view) {
        webRTCClient.initRemoteSurfaceView(view);
        this.remoteView = view;
    }

    public void startCall(String target) {
        Log.d("test", "startCall: ");
        setTarget(target);
        webRTCClient.call(target);
    }

    public void switchCamera(SurfaceViewRenderer localRenderer, SurfaceViewRenderer remoteRenderer) {
        webRTCClient.switchCamera(localRenderer, remoteRenderer);
    }

    public void toggleVideo(Boolean shouldMuted) {
        webRTCClient.toggleVideo(shouldMuted);
    }

    public void toggleAudio(Boolean shouldMuted) {
        webRTCClient.toggleAudio(shouldMuted);
    }

    public void sendCallRequest(String target, ErrorCallback errorCallback, Context context) {
//        firebaseClient.sendMessageToOtherUser(
//                new DataModels(target, currentUsername, null, DataModelType.StartCall),errorCallback
//        );
//        sendMessageToOtherUser(new DataModels(target, currentUsername, null, DataModelType.StartCall), errorCallback);
        DataModels dataModel = new DataModels(target, currentUsername, null, DataModelType.StartCall);
        String jsonMessage = gson.toJson(dataModel);
        Log.d("MyActivity", "Received WebSocket message: " + jsonMessage);
        WebSocketSingleton.getInstance().sendMessage(jsonMessage);
    }

    public void endCall() {
        webRTCClient.closeConnection();
        listener.webrtcClose();
    }

    public void closeConnection() {
        webRTCClient.closeConnection();
    }

    public void toggleVideoScreenSize(SurfaceViewRenderer remoteView, SurfaceViewRenderer localView, FrameLayout frameLayout) {
//        webRTCClient.toggleVideoScreenSize(viewRenderer);
        webRTCClient.swapVideoViews(remoteView, localView, frameLayout);
    }

    // 接受远程视频流
    public void onRemoteStream(MediaStream stream, SurfaceViewRenderer remoteView) {
        webRTCClient.onRemoteStream(stream, remoteView);
    }

    @Override
    public void onTransferDataToOtherPeer(DataModels model) {
        WebSocketSingleton.getInstance().sendMessage(gson.toJson(model));
    }

    public void login(String username, SuccessCallback callback) {
        callback.onSuccess();
    }

    public void setNull(String key, ErrorCallback errorCallback) {
        RedisPoolManager.getInstance().existsAsync(key, activity).thenAccept(exists -> {
            Log.d("test", "setNull: " + exists);
            if (exists) {
                RedisPoolManager.getInstance().setAsync(key, "");
            } else {
                errorCallback.onError();
            }
            Log.d("MainActivity", "exists2===" + exists);
        });
    }

    public void stopCapture() throws InterruptedException {
        webRTCClient.stopCapture();
    }

    public void startCapture() throws InterruptedException {
        webRTCClient.startCapture();
    }

    public void restartLocalVideoStreaming(SurfaceViewRenderer localView) {
        webRTCClient.restartLocalVideoStreaming(localView);
    }

    public interface Listener {
        void webrtcConnected();
        void webrtcClose();
    }
}
