package com.android.zkaf.webrtcjavacoderbeichen.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.zkaf.myapplication.main.model.wss.WssBaseResult;
import com.android.zkaf.myapplication.main.model.wss.WssCmd;
import com.android.zkaf.webrtcjavacoderbeichen.R;
import com.android.zkaf.webrtcjavacoderbeichen.databinding.ActivityCallBinding;
import com.android.zkaf.webrtcjavacoderbeichen.databinding.ActivityLoginBinding;
import com.android.zkaf.webrtcjavacoderbeichen.http.JWebSocketClient;
import com.android.zkaf.webrtcjavacoderbeichen.repository.MainRepository;
import com.android.zkaf.webrtcjavacoderbeichen.utils.DataModelType;
import com.android.zkaf.webrtcjavacoderbeichen.utils.DataModels;
import com.android.zkaf.webrtcjavacoderbeichen.utils.RedisPoolManager;
import com.android.zkaf.webrtcjavacoderbeichen.utils.Utils;

import java.net.URI;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CallActivity extends AppCompatActivity implements MainRepository.Listener {

   private ActivityCallBinding views;
   private MainRepository mainRepository;

   private Boolean isCameraMuted = true; // 摄像头默认是开启的状态
   private Boolean isMicrophoneMuted = false;
   private String username;
//   private RedisManager redisManager;

   // ws
   private String wsUrl;
   private URI wsUri;
   private JWebSocketClient client;

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      views = ActivityCallBinding.inflate(getLayoutInflater());
      setContentView(views.getRoot());

//      redisManager = RedisManager.getInstance();

      // 获取登录的username
      Intent intent = getIntent();
      username = intent.getStringExtra("username");
      Toast.makeText(this, username + "登录成功", Toast.LENGTH_SHORT).show();

      init();
   }

   private void init() {
      mainRepository = MainRepository.getInstance();
      mainRepository.setContext(this);
      mainRepository.setActivity(this);
      RedisPoolManager.getInstance().setActivity(this);
      views.callBtn.setOnClickListener(v -> {
         if (Objects.equals(username, views.targetUserNameEt.getText().toString())) {
            Toast.makeText(this, "你不能呼叫自己", Toast.LENGTH_SHORT).show();
            return;
         }
         RedisPoolManager.getInstance().setUserName(username);
         RedisPoolManager.getInstance().setTargetName(views.targetUserNameEt.getText().toString());
         // 开始发送请求
         mainRepository.sendCallRequest(views.targetUserNameEt.getText().toString(), () -> {
            Toast.makeText(this, "couldnt find target", Toast.LENGTH_SHORT).show();
         },this);
      });
      views.ping.setOnClickListener(v -> {
//         wsConnectTest();
         Log.d("test", "username: " + username);
         mainRepository.setNull(username, () -> {});
      });
      mainRepository.initLocalView(views.localView);
      mainRepository.initRemoteView(views.remoteView);
      mainRepository.listener = this;

      mainRepository.subscribeForLatestEvent((DataModels data, String username) -> {
         if (data.getType() == DataModelType.StartCall) {
            runOnUiThread(() -> {
               views.incomingNameTV.setText(data.getSender() + " is calling you");
               views.incomingCallLayout.setVisibility(View.VISIBLE);
               views.acceptButton.setOnClickListener(v -> {
                  // 开始呼叫
                  Log.d("test", "init: 开始呼叫");
                  mainRepository.startCall(data.getSender());
                  views.incomingCallLayout.setVisibility(View.GONE);
               });
               views.rejectButton.setOnClickListener(v -> {
                  Log.d("test", "setAsync:" + username);
                  mainRepository.setNull(username, () -> {});
                  views.incomingCallLayout.setVisibility(View.GONE);
               });
            });
         }
      });

      views.switchCameraButton.setOnClickListener(v -> {
         mainRepository.switchCamera(views.localView, views.remoteView);
      });

      views.micButton.setOnClickListener(v->{
         if (isMicrophoneMuted){
            views.micButton.setImageResource(R.drawable.ic_baseline_mic_off_24);
         }else {
            views.micButton.setImageResource(R.drawable.ic_baseline_mic_24);
         }
         mainRepository.toggleAudio(isMicrophoneMuted);
         isMicrophoneMuted=!isMicrophoneMuted;
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
         views.localView.release();
         views.remoteView.release();
         finish();
      });

      // 远程流和本地流切换显示
      views.localView.setOnClickListener(v -> {
//         mainRepository.initRemoteView(views.localView);
//         mainRepository.initRemoteView(views.remoteView);
         Log.d("test", "toggleVideoScreenSize");
         mainRepository.toggleVideoScreenSize(views.remoteView, views.localView, views.frameLayout);
      });

   }

   @Override
   public void webrtcConnected() {
      runOnUiThread(() -> {
         views.incomingCallLayout.setVisibility(View.GONE);
         views.whoToCallLayout.setVisibility(View.GONE);
         views.callLayout.setVisibility(View.VISIBLE);
      });
   }

   @Override
   public void webrtcClose() {
      runOnUiThread(this::finish);
   }

   @Override
   protected void onDestroy() {
      super.onDestroy();
      views.localView.release();
      views.remoteView.release();
   }
   private void wsConnectTest() {
//      if (MMKV.defaultMMKV().getBoolean(MMKVUtil.jWebSocketClientIsConnected, false)) {
//         KLog.d("test", "ws已连接");
//         return;
//      }
//      wsUrl = trim(et_serverip) + ":" + trim(et_serverport);
      wsUrl = "192.168.1.216:8083";
      // 这里的wsUrl前缀无协议类型，需手动添加协议。
      wsUri = URI.create("ws://" + wsUrl + "/wsFaceDevice?sn=" + Utils.getSerialNumber());
      Log.d("test", "wsUri===" + wsUri);
      try {
         client = new JWebSocketClient(wsUri)
         {
            @Override
            public void onMessage(String message) {
               // Do something with the message
               Log.d("test", "onMessage===" + message);
               if (TextUtils.isEmpty(message) || message == null) {
                  return;
               }

               WssBaseResult wssBaseResult = JSON.parseObject(message, WssBaseResult.class);
               if (TextUtils.isEmpty(wssBaseResult.getCmd())) {
                  return;
               }
               Log.d("test", "wssBaseResult.getCmd().equals(WssCmd.CHECK_SIGN)===" + (wssBaseResult.getCmd().equals(WssCmd.CHECK_SIGN)));
               if (wssBaseResult.getCmd().equals(WssCmd.CHECK_SIGN)) {
                  // 收到消息
                  Log.d("test", "ws收到成功消息");
                  try {
                     Log.d("test", "关闭ws");
                     if (client != null && client.isOpen()) {
                        client.close();  // 使用非阻塞方法
                     } else {
                        Log.d("test", "客户端已经关闭或为空");
                     }
                  } finally {
                     Log.d("test", "关闭加载框");
//                     dismissLoadingDialog();
                  }
               }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
               // Do something on close
               Log.d("test", "onClose");
//               dismissLoadingDialog();
//               wsConnectError();
            }

            @Override
            public void onError(Exception ex) {
               // Handle the error
               Log.d("test", "onError");
               client.close();
            }
         };

         if (client.connectBlocking()) {
            // ws连接成功
            Log.d("test", "ws连接成功");
//            checkSign();
         } else {
            Log.d("test", "ws连接失败");
//            wsConnectError();
         }

      } catch (InternalError | InterruptedException e) {
         e.printStackTrace();
         Log.e("WssService", "InterruptedException---" + e.getMessage());
      }
   }

   private void checkSign() {
//      String salt = DataManager.getInstance().getPrefEntry(DataManager.WSS_TOKEN);
//      Log.d("test", "checkSign----salt");
//      sendData(MQTTReq.checkSign(salt));
   }
}
