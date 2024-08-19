package com.android.zkaf.webrtcjavacoderbeichen.ui;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.zkaf.webrtcjavacoderbeichen.R;
import com.android.zkaf.webrtcjavacoderbeichen.TCP.TcpClientSingleton;
import com.android.zkaf.webrtcjavacoderbeichen.TCP.TcpServer;
import com.android.zkaf.webrtcjavacoderbeichen.UDP.UDPClient;
import com.android.zkaf.webrtcjavacoderbeichen.UDP.UDPServer;
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

public class CallActivity extends AppCompatActivity {

   private ActivityCallBinding views;
   private String username;
   private Gson gson;
   private UDPClient udpClient;
   private UDPServer udpServer;
   private TcpClientSingleton tcpClient;


   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      views = ActivityCallBinding.inflate(getLayoutInflater());
      setContentView(views.getRoot());

      // 获取登录的username
      Intent intent = getIntent();
      username = intent.getStringExtra("username");
      Toast.makeText(this, username + "登录成功", Toast.LENGTH_SHORT).show();

      // 注册 EventBus
      EventBus.getDefault().register(this);

      gson = new Gson();

      try {
//         udpServer = new UDPServer(12345); // 端口号
//         udpServer.start();

         // 创建并启动 TCP 服务器
         TcpServer.getInstance().startServer();
      } catch (Exception e) {
         e.printStackTrace();
      }
//      init();
      WebSocketSingleton.getInstance().initWebSocket("192.168.1.216", "8765");
      views.callBtn.setOnClickListener(v -> {
         String targetIp = views.targetUserNameEt.getText().toString();
         if (Objects.equals(username, views.targetUserNameEt.getText().toString())) {
            Toast.makeText(this, "你不能呼叫自己", Toast.LENGTH_SHORT).show();
            return;
         }
//         try {
//            udpClient = UDPClient.getInstance(targetIp); // 服务端 IP 和端口号
//            udpClient.receiveMessage(); // 开始监听服务端消息
//         } catch (Exception e) {
//            e.printStackTrace();
//         }
         DataModels dataModel = new DataModels(targetIp, Utils.getLocalIpAddress(), null, DataModelType.StartCall);
         String jsonMessage = gson.toJson(dataModel);
         Log.d("MyActivity", "Received WebSocket message: " + jsonMessage);

//         WebSocketSingleton.getInstance().sendMessage(jsonMessage);


         // Initialize the TcpClientSingleton instance
         TcpClientSingleton tcpClient = TcpClientSingleton.getInstance(targetIp, 5000);

         // Send the message
         tcpClient.sendMessage(jsonMessage);
         try {
//            udpClient.sendMessage(jsonMessage);
         } catch (Exception e) {
            throw new RuntimeException(e);
         }
      });
      views.ping.setOnClickListener(v -> {
         Log.d("test", "username: " + username);
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

//      views.switchCameraButton.setOnClickListener(v -> {
////         mainRepository.switchCamera(views.localView, views.remoteView);
//      });

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

//      views.videoButton.setOnClickListener(v->{
//         if (isCameraMuted){
//            views.videoButton.setImageResource(R.drawable.ic_baseline_videocam_off_24);
//         }else {
//            views.videoButton.setImageResource(R.drawable.ic_baseline_videocam_24);
//         }
//         mainRepository.toggleVideo(isCameraMuted);
//         isCameraMuted=!isCameraMuted;
//      });

//      views.endCallButton.setOnClickListener(v->{
//         mainRepository.endCall();
//         finish();
//      });

      // 远程流和本地流切换显示
//      views.localView.setOnClickListener(v -> {
//         Log.d("test", "toggleVideoScreenSize");
//         mainRepository.toggleVideoScreenSize(views.remoteView, views.localView, views.frameLayout);
//      });
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
      Log.d("MyActivity", "Received WebSocket message: " + gson.fromJson(event.message, DataModels.class));
      DataModels dataModels = gson.fromJson(event.message, DataModels.class);
      Log.d("MyActivity", "Received WebSocket message: " + dataModels.getType());

      switch (dataModels.getType()) {
//          case Offer:
//              mainRepository.webRTCClient.onRemoteSessionReceived(new SessionDescription(
//                      SessionDescription.Type.OFFER, dataModels.getData()
//              ));
//             mainRepository.webRTCClient.answer(dataModels.getSender());
//              break;
//          case Answer:
//             mainRepository.webRTCClient.onRemoteSessionReceived(new SessionDescription(
//                      SessionDescription.Type.ANSWER, dataModels.getData()
//              ));
//              break;
//          case IceCandidate:
//             Log.d("test", "onWebSocketMessage: ===" + !isAppRunning(CallActivity.this, "VideoScreen"));
//             if (!isAppRunning(CallActivity.this, "VideoScreen")) {
//                Intent intent1 = new Intent(CallActivity.this, VideoScreen.class);
//                startActivity(intent1);
//             }

//              try {
//                  IceCandidate candidate = gson.fromJson(dataModels.getData(), IceCandidate.class);
//                 mainRepository.webRTCClient.addIceCandidate(candidate);
//              } catch (Exception e) {
//                  e.printStackTrace();
//              }
//              break;
         case StartCall:
            if (!Objects.equals(dataModels.getTarget(), Utils.getLocalIpAddress())) {
               return;
            }
            runOnUiThread(() -> {
               views.incomingNameTV.setText(dataModels.getSender() + " is calling you");
               views.incomingCallLayout.setVisibility(View.VISIBLE);
               views.acceptButton.setOnClickListener(v -> {
                  // 开始呼叫
                  Log.d("test", "init: 开始呼叫");
//                  mainRepository.startCall(dataModels.getSender())

//                  WebSocketSingleton.getInstance().sendMessage(gson.toJson(new DataModels(dataModels.getTarget(), Utils.getLocalIpAddress(), "", DataModelType.Jump)));

                  try {
//                     udpClient = UDPClient.getInstance(dataModels.getSender());
//                     udpClient.sendMessage(gson.toJson(new DataModels(dataModels.getSender(), Utils.getLocalIpAddress(), "", DataModelType.Jump)));

//                     TcpClientSingleton tcpClient = TcpClientSingleton.getInstance(dataModels.getTarget(), 5000);
//                     tcpClient.sendMessage(gson.toJson(new DataModels(dataModels.getSender(), Utils.getLocalIpAddress(), "", DataModelType.Jump)));

                     TcpServer tcpServer = TcpServer.getInstance();
                     tcpServer.sendMessageToClient(gson.toJson(new DataModels(dataModels.getSender(), Utils.getLocalIpAddress(), "", DataModelType.Jump)));
                  } catch (Exception e) {
                     throw new RuntimeException(e);
                  }
                  Intent intent = new Intent(CallActivity.this, VideoScreen.class);
                  intent.putExtra("target", dataModels.getSender());
                  startActivity(intent);
                  views.incomingCallLayout.setVisibility(View.GONE);
               });
//               views.rejectButton.setOnClickListener(v -> {
//                  Log.d("test", "setAsync:" + username);
//                  mainRepository.setNull(username, () -> {});
//                  views.incomingCallLayout.setVisibility(View.GONE);
//               });
            });
            break;
         case Jump:
            Intent intent = new Intent(CallActivity.this, VideoScreen.class);
            startActivity(intent);
            break;
         default:
            break;
      }
   }
}
