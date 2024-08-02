package com.android.zkaf.webrtcjavacoderbeichen.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.zkaf.webrtcjavacoderbeichen.R;
import com.android.zkaf.webrtcjavacoderbeichen.databinding.ActivityVideoScreenBinding;
import com.android.zkaf.webrtcjavacoderbeichen.http.WebSocketSingleton;
import com.android.zkaf.webrtcjavacoderbeichen.repository.MainRepository;
import com.android.zkaf.webrtcjavacoderbeichen.utils.DataModelType;
import com.android.zkaf.webrtcjavacoderbeichen.utils.DataModels;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;

import java.util.Objects;

public class VideoScreen extends AppCompatActivity implements MainRepository.Listener  {
    private ActivityVideoScreenBinding views;
    private MainRepository mainRepository;

    private Boolean isCameraMuted = true; // 摄像头默认是开启的状态
    private Boolean isMicrophoneMuted = false;
    private String username;
    private String target;
    private Gson gson;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        views = ActivityVideoScreenBinding.inflate(getLayoutInflater());
        setContentView(views.getRoot());

        // 获取登录的username
        Intent intent = getIntent();
        target = intent.getStringExtra("target");
        Log.d("test", "onCreate: target===" + target);
//        Toast.makeText(this, username + "登录成功", Toast.LENGTH_SHORT).show();

        // 注册 EventBus
        EventBus.getDefault().register(this);

        gson = new Gson();

        init();
    }

    private void init() {
        mainRepository = MainRepository.getInstance();
        mainRepository.setContext(this);
        mainRepository.setActivity(this);
        mainRepository.initWebRTCClient();
        mainRepository.initLocalView(views.localView);
        mainRepository.initRemoteView(views.remoteView);
        mainRepository.listener = this;

        Log.d("test", "init: videoScreen===" + "开始呼叫" + target);
        if (target != null) {
            mainRepository.startCall(target);
        }

//        views.callBtn.setOnClickListener(v -> {
//            if (Objects.equals(username, views.targetUserNameEt.getText().toString())) {
//                Toast.makeText(this, "你不能呼叫自己", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            DataModels dataModel = new DataModels(views.targetUserNameEt.getText().toString(), username, null, DataModelType.StartCall);
//            String jsonMessage = gson.toJson(dataModel);
//            Log.d("MyActivity", "Received WebSocket message: " + jsonMessage);
//            WebSocketSingleton.getInstance().sendMessage(jsonMessage);
//        });
//        views.ping.setOnClickListener(v -> {
//            Log.d("test", "username: " + username);
//            WebSocketSingleton.getInstance().initWebSocket("192.168.1.216", "8765");
//        });
        views.switchCameraButton.setOnClickListener(v -> {
            mainRepository.switchCamera(views.localView, views.remoteView);
        });
        views.videoButton.setOnClickListener(v->{
            if (isCameraMuted){
                views.videoButton.setImageResource(R.drawable.ic_baseline_videocam_off_24);
            }else {
                views.videoButton.setImageResource(R.drawable.ic_baseline_videocam_24);
            }
            mainRepository.toggleVideo(isCameraMuted);
            isCameraMuted=!isCameraMuted;
        });
        views.endCallButton.setOnClickListener(v->{
            mainRepository.endCall();
            finish();
        });
        // 远程流和本地流切换显示
        views.localView.setOnClickListener(v -> {
            Log.d("test", "toggleVideoScreenSize");
            mainRepository.toggleVideoScreenSize(views.remoteView, views.localView, views.frameLayout);
        });
    }

    @Override
    public void webrtcConnected() {
//        Intent intent = new Intent(this, VideoScreen.class);
//        startActivity(intent);
//        runOnUiThread(() -> {
//            views.incomingCallLayout.setVisibility(View.GONE);
//            views.whoToCallLayout.setVisibility(View.GONE);
//            views.callLayout.setVisibility(View.VISIBLE);
//        });
    }

    @Override
    public void webrtcClose() {
        mainRepository.closeConnection();
        runOnUiThread(this::finish);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 取消注册 EventBus
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWebSocketMessage(WebSocketSingleton.WebSocketMessageEvent event) {
        Log.d("MyActivity", "Received WebSocket message: " + event.message);
//        Log.d("MyActivity", "Received WebSocket message: " + gson.fromJson(event.message, DataModels.class));
        DataModels dataModels = gson.fromJson(event.message, DataModels.class);
        Log.d("MyActivity", "Received WebSocket message: " + dataModels.getType());

        switch (dataModels.getType()) {
            case Offer:
                mainRepository.webRTCClient.onRemoteSessionReceived(new SessionDescription(
                        SessionDescription.Type.OFFER, dataModels.getData()
                ));
                mainRepository.webRTCClient.answer(dataModels.getSender());
                break;
            case Answer:
                mainRepository.webRTCClient.onRemoteSessionReceived(new SessionDescription(
                        SessionDescription.Type.ANSWER, dataModels.getData()
                ));
                break;
            case IceCandidate:
                try {
                    IceCandidate candidate = gson.fromJson(dataModels.getData(), IceCandidate.class);
                    mainRepository.webRTCClient.addIceCandidate(candidate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
//            case StartCall:
//                if (!Objects.equals(dataModels.getTarget(), username)) {
//                    return;
//                }
//                runOnUiThread(() -> {
////                    views.incomingNameTV.setText(dataModels.getSender() + " is calling you");
////                    views.incomingCallLayout.setVisibility(View.VISIBLE);
////                    views.acceptButton.setOnClickListener(v -> {
////                        // 开始呼叫
////                        Log.d("test", "init: 开始呼叫");
////                        mainRepository.startCall(dataModels.getSender());
//////                        views.incomingCallLayout.setVisibility(View.GONE);
////                    });
//                    views.rejectButton.setOnClickListener(v -> {
//                        Log.d("test", "setAsync:" + username);
//                        mainRepository.setNull(username, () -> {});
////                        views.incomingCallLayout.setVisibility(View.GONE);
//                    });
//                });
//                break;
            default:
                break;
        }
    }
}
