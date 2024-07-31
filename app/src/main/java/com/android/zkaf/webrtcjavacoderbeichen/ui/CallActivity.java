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
import com.android.zkaf.webrtcjavacoderbeichen.http.WebSocketSingleton;
import com.android.zkaf.webrtcjavacoderbeichen.repository.MainRepository;
import com.android.zkaf.webrtcjavacoderbeichen.utils.DataModelType;
import com.android.zkaf.webrtcjavacoderbeichen.utils.DataModels;
import com.android.zkaf.webrtcjavacoderbeichen.utils.RedisPoolManager;
import com.android.zkaf.webrtcjavacoderbeichen.utils.Utils;
import com.android.zkaf.webrtcjavacoderbeichen.webrtc.MyPeerConnectionObserver;
import com.google.gson.Gson;

import java.net.URI;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.SessionDescription;

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

   private Gson gson;
   private Boolean isTrue = true;


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

      // 注册 EventBus
      EventBus.getDefault().register(this);

      gson = new Gson();

      init();
      WebSocketSingleton.getInstance().initWebSocket("192.168.1.216", "8765");
      views.callBtn.setOnClickListener(v -> {
         if (Objects.equals(username, views.targetUserNameEt.getText().toString())) {
            Toast.makeText(this, "你不能呼叫自己", Toast.LENGTH_SHORT).show();
            return;
         }
//         mainRepository.setTarget(views.targetUserNameEt.getText().toString());
//         RedisPoolManager.getInstance().setUserName(username);
//         RedisPoolManager.getInstance().setTargetName(views.targetUserNameEt.getText().toString());
         // 开始发送请求
//         mainRepository.sendCallRequest(views.targetUserNameEt.getText().toString(), () -> {
//            Toast.makeText(this, "couldnt find target", Toast.LENGTH_SHORT).show();
//         },this);
         DataModels dataModel = new DataModels(views.targetUserNameEt.getText().toString(), username, null, DataModelType.StartCall);
         String jsonMessage = gson.toJson(dataModel);
         Log.d("MyActivity", "Received WebSocket message: " + jsonMessage);
         WebSocketSingleton.getInstance().sendMessage(jsonMessage);
      });
      views.ping.setOnClickListener(v -> {
         Log.d("test", "username: " + username);
//         wsConnectTest();
//         mainRepository.setNull(username, () -> {});
         WebSocketSingleton.getInstance().initWebSocket("192.168.1.216", "8765");
      });
//      mainRepository.subscribeForLatestEvent((DataModels data, String username) -> {
//         if (data.getType() == DataModelType.StartCall) {
//            runOnUiThread(() -> {
//               views.incomingNameTV.setText(data.getSender() + " is calling you");
//               views.incomingCallLayout.setVisibility(View.VISIBLE);
//               views.acceptButton.setOnClickListener(v -> {
//                  // 开始呼叫
//                  Log.d("test", "init: 开始呼叫");
//                  mainRepository.startCall(data.getSender());
//                  views.incomingCallLayout.setVisibility(View.GONE);
//               });
//               views.rejectButton.setOnClickListener(v -> {
//                  Log.d("test", "setAsync:" + username);
//                  mainRepository.setNull(username, () -> {});
//                  views.incomingCallLayout.setVisibility(View.GONE);
//               });
//            });
//         }
//      });

      views.switchCameraButton.setOnClickListener(v -> {
         mainRepository.switchCamera(views.localView, views.remoteView);
      });

//      views.micButton.setOnClickListener(v->{
//         if (isTrue) {
//            try {
//               MainRepository.getInstance().stopCapture();
//               isTrue = false;
//            } catch (Exception e) {
//               e.printStackTrace();
//            }
//         } else {
//            try {
//               MainRepository.getInstance().startCapture();
//               isTrue = true;
//            } catch (Exception e) {
//               e.printStackTrace();
//            }
//         }
////         if (isMicrophoneMuted){
////            views.micButton.setImageResource(R.drawable.ic_baseline_mic_off_24);
////         }else {
////            views.micButton.setImageResource(R.drawable.ic_baseline_mic_24);
////         }
////         mainRepository.toggleAudio(isMicrophoneMuted);
////         isMicrophoneMuted=!isMicrophoneMuted;
//      });

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
//         mainRepository.endCall();
//         views.localView.release();
//         views.remoteView.release();
//         mainRepository.webRTCClient = null;
//         mainRepository.initWebRTCClient();
//         finish();
//         init();

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

      views.sendWs.setOnClickListener(v -> {
         WebSocketSingleton.getInstance().sendMessage("{" +
                 "a: A" +
                 "b: B" +
                 "c: C" +
                 "d: D" +
                 "}");
      });
   }

   private void init() {
      mainRepository = MainRepository.getInstance();
      mainRepository.setContext(this);
      mainRepository.setActivity(this);
      mainRepository.initWebRTCClient();
//      RedisPoolManager.getInstance().setActivity(this);
//      mainRepository.initWebRTCClient();
      mainRepository.initLocalView(views.localView);
      mainRepository.initRemoteView(views.remoteView);
      mainRepository.listener = this;

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
//      mainRepository.webRTCClient = null;
//      mainRepository.initWebRTCClient();
//      mainRepository.webRTCClient.reconnect(new MyPeerConnectionObserver(), views.localView);

//      runOnUiThread(() -> {
//         views.incomingCallLayout.setVisibility(View.GONE);
//         views.whoToCallLayout.setVisibility(View.VISIBLE);
//         views.callLayout.setVisibility(View.GONE);
//      });
   }

   @Override
   protected void onDestroy() {
      super.onDestroy();
      // 取消注册 EventBus
      EventBus.getDefault().unregister(this);
      views.localView.release();
      views.remoteView.release();
   }
//   private void wsConnectTest() {
////      if (MMKV.defaultMMKV().getBoolean(MMKVUtil.jWebSocketClientIsConnected, false)) {
////         KLog.d("test", "ws已连接");
////         return;
////      }
////      wsUrl = trim(et_serverip) + ":" + trim(et_serverport);
//      wsUrl = "192.168.1.85:8765";
//      // 这里的wsUrl前缀无协议类型，需手动添加协议。
//      wsUri = URI.create("ws://" + wsUrl + "/wsFaceDevice?sn=" + Utils.getSerialNumber());
//      Log.d("test", "wsUri===" + wsUri);
//      try {
//         client = new JWebSocketClient(wsUri)
//         {
//            @Override
//            public void onMessage(String message) {
//               // Do something with the message
//               Log.d("test", "onMessage===" + message);
//               if (TextUtils.isEmpty(message) || message == null) {
//                  return;
//               }
//
////               WssBaseResult wssBaseResult = JSON.parseObject(message, WssBaseResult.class);
////               if (TextUtils.isEmpty(wssBaseResult.getCmd())) {
////                  return;
////               }
////               Log.d("test", "wssBaseResult.getCmd().equals(WssCmd.CHECK_SIGN)===" + (wssBaseResult.getCmd().equals(WssCmd.CHECK_SIGN)));
////               if (wssBaseResult.getCmd().equals(WssCmd.CHECK_SIGN)) {
////                  // 收到消息
////                  Log.d("test", "ws收到成功消息");
////                  try {
////                     Log.d("test", "关闭ws");
////                     if (client != null && client.isOpen()) {
////                        client.close();  // 使用非阻塞方法
////                     } else {
////                        Log.d("test", "客户端已经关闭或为空");
////                     }
////                  } finally {
////                     Log.d("test", "关闭加载框");
//////                     dismissLoadingDialog();
////                  }
////               }
//            }
//
//            @Override
//            public void onClose(int code, String reason, boolean remote) {
//               // Do something on close
//               Log.d("test", "onClose");
//               isWsStatus = false;
//               views.wsStatus.setText("未连接");
////               dismissLoadingDialog();
////               wsConnectError();
//            }
//
//            @Override
//            public void onError(Exception ex) {
//               // Handle the error
//               Log.d("test", "onError");
//               views.wsStatus.setText("未连接");
//               client.close();
//            }
//         };
//
//         if (client.connectBlocking()) {
//            // ws连接成功
//            Log.d("test", "ws连接成功");
//            isWsStatus = true;
//            views.wsStatus.setText("已连接");
////            checkSign();
//         } else {
//            Log.d("test", "ws连接失败");
//            isWsStatus = false;
//            views.wsStatus.setText("未连接");
////            wsConnectError();
//         }
//
//      } catch (InternalError | InterruptedException e) {
//         e.printStackTrace();
//         Log.e("WssService", "InterruptedException---" + e.getMessage());
//      }
//   }


   @Override
   protected void onResume() {
      super.onResume();
//      mainRepository.initWebRTCClient();
//      if (mainRepository.webRTCClient != null) {
//         mainRepository.webRTCClient = null;
//         mainRepository.initWebRTCClient();
////         mainRepository.initLocalView(views.localView);
////         mainRepository.initRemoteView(views.remoteView);
//      }
   }

   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onWebSocketMessage(WebSocketSingleton.WebSocketMessageEvent event) {
      Log.d("MyActivity", "Received WebSocket message: " + event.message);
      Log.d("MyActivity", "Received WebSocket message: " + gson.fromJson(event.message, DataModels.class));
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
         case StartCall:
            if (!Objects.equals(dataModels.getTarget(), username)) {
               return;
            }
            runOnUiThread(() -> {
               views.incomingNameTV.setText(dataModels.getSender() + " is calling you");
               views.incomingCallLayout.setVisibility(View.VISIBLE);
               views.acceptButton.setOnClickListener(v -> {
                  // 开始呼叫
                  Log.d("test", "init: 开始呼叫");
                  mainRepository.startCall(dataModels.getSender());
                  views.incomingCallLayout.setVisibility(View.GONE);
               });
               views.rejectButton.setOnClickListener(v -> {
                  Log.d("test", "setAsync:" + username);
                  mainRepository.setNull(username, () -> {});
                  views.incomingCallLayout.setVisibility(View.GONE);
               });
            });
            break;
         default:
            break;
      }
      // 处理接收到的消息
//      switch (event.message) {
//         case "connect_success":
//            views.wsStatus.setText("已连接");
//            break;
//         case "connect_fail":
//            views.wsStatus.setText("未连接");
//            break;
//         default:
//             break;
//      }
   }
}
