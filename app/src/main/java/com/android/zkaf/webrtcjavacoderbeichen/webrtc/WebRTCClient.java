package com.android.zkaf.webrtcjavacoderbeichen.webrtc;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.zkaf.webrtcjavacoderbeichen.utils.DataModelType;
import com.android.zkaf.webrtcjavacoderbeichen.utils.DataModels;
import com.android.zkaf.webrtcjavacoderbeichen.utils.ErrorCallback;
import com.google.gson.Gson;

import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraVideoCapturer;
import org.webrtc.CapturerObserver;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.RTCStats;
import org.webrtc.RTCStatsCollectorCallback;
import org.webrtc.RTCStatsReport;
import org.webrtc.RtpParameters;
import org.webrtc.RtpSender;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.lang.invoke.CallSite;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.view.menu.ShowableListMenu;

public class WebRTCClient {

    private final Gson gson = new Gson();
    private final Context context;
    private final String username;
    private EglBase.Context eglbaseContext = EglBase.create().getEglBaseContext();
    private PeerConnectionFactory peerConnectionFactory;
    private PeerConnection peerConnection;
    private List<PeerConnection.IceServer> iceServer = new ArrayList<>();

    private CameraVideoCapturer videoCapturer;
    private VideoSource localVideoSource;
    private AudioSource localAudioSource;
    private String localTrackId = "local_track";
    private String localStreamId = "local_stream";
    private VideoTrack localVideoTrack;
    private AudioTrack localAudioTrack;
    private VideoTrack remoteVideoTrack;
    private MediaStream localStream;
    private MediaConstraints mediaConstraints = new MediaConstraints();

    //    googEchoCancellation   回音消除
    public static final String AUDIO_ECHO_CANCELLATION_CONSTRAINT = "googEchoCancellation";
    //    googNoiseSuppression   噪声抑制
    public static final String AUDIO_NOISE_SUPPRESSION_CONSTRAINT = "googNoiseSuppression";
    //    googAutoGainControl    自动增益控制
    public static final String AUDIO_AUTO_GAIN_CONTROL_CONSTRAINT = "googAutoGainControl";
    //    googHighpassFilter     高通滤波器
    public static final String AUDIO_HIGH_PASS_FILTER_CONSTRAINT = "googHighpassFilter";

    public Listener listener;

    private Boolean isRemoteViewFront = true;

    public WebRTCClient(Context context, PeerConnection.Observer observer, String username) {
        this.context = context;
        this.username = username;
        initPeerConnectionFactory();
        peerConnectionFactory = createPeerConnectionFactory();
        iceServer.add(PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer());
//        iceServer.add(PeerConnection.IceServer.builder("turn:a.relay.metered.ca:443?transport=tcp")
//                .setUsername("83eebabf8b4cce9d5dbcb649")
//                .setPassword("2D7JvfkOQtBdYW3R").createIceServer());
        peerConnection = createPeerConnection(observer);
        // 创建本地视频源
        // 指示是否启用回声消除和噪声抑制。设置为false表示不启用这些功能（通常这些功能适用于音频源）。
        localVideoSource = peerConnectionFactory.createVideoSource(false);
        // 创建本地音频源
        // 传递一个新的MediaConstraints实例，通常用于设置音频约束。
        localAudioSource = peerConnectionFactory.createAudioSource(new MediaConstraints());
        // 设置媒体约束
        // mandatory: 强制约束条件
        // 指示在创建SDP时要包含视频接收通道。
        mediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"));
//        mediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"));
//        mediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
//        mediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair(AUDIO_ECHO_CANCELLATION_CONSTRAINT,"true"));
//        mediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair(AUDIO_NOISE_SUPPRESSION_CONSTRAINT,"true"));
//        mediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair(AUDIO_AUTO_GAIN_CONTROL_CONSTRAINT,"false"));
//        mediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair(AUDIO_HIGH_PASS_FILTER_CONSTRAINT,"true"));

        peerConnection.getStats(new RTCStatsCollectorCallback() {
            @Override
            public void onStatsDelivered(RTCStatsReport rtcStatsReport) {
                for (RTCStats stats : rtcStatsReport.getStatsMap().values()) {
                    Log.d("test", "WebRTC Stats===" + stats.toString());
                    // 检查视频流的统计信息
                }
            }
        });
    }

    // 初始化peer connection section
    private void initPeerConnectionFactory() {
        PeerConnectionFactory.InitializationOptions options =
                PeerConnectionFactory.InitializationOptions.builder(context)
//                                .setFieldTrials("WebRTC-H264HighProfile/Enabled/")
//                                .setFieldTrials("WebRTC-VP8-Improved-Decoder/Enabled/")
//                                .setFieldTrials("WebRTC-VP9-Enabled/Disabled/")
//                                .setFieldTrials("WebRTC-Aec3/Enabled/")
//                                .setFieldTrials("WebRTC-H264MainProfile/Enabled/WebRTC-Aec3/Enabled/")
//                                .setFieldTrials("WebRTC-H264Simulcast/Enabled/")
                                .setFieldTrials("WebRTC-UseNewWebRTC=true")
                                .setEnableInternalTracer(true).createInitializationOptions();
        PeerConnectionFactory.initialize(options);
    }

    private PeerConnectionFactory createPeerConnectionFactory() {
        PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
        options.disableEncryption = false; // 此选项用于控制是否禁用WebRTC的加密功能
        options.disableNetworkMonitor = false; // 此选项用于控制是否禁用网络监控功能
        // egl硬件加速、支持hardware acceleration（硬件加速）启用VP8、VP9和H264编码器
        return PeerConnectionFactory.builder()
                .setVideoEncoderFactory(new DefaultVideoEncoderFactory(eglbaseContext, true, true))
                .setVideoDecoderFactory(new DefaultVideoDecoderFactory(eglbaseContext))
                .setOptions(options).createPeerConnectionFactory();
    }

    private PeerConnection createPeerConnection(PeerConnection.Observer observer) {
        PeerConnection.RTCConfiguration rtcConfig = new PeerConnection.RTCConfiguration(iceServer);
        return peerConnectionFactory.createPeerConnection(rtcConfig, observer);
//        return peerConnectionFactory.createPeerConnection(iceServer,observer);
    }

    // 切换大小
    public void toggleVideoScreenSize(SurfaceViewRenderer viewRenderer) {
        ViewGroup.LayoutParams params = viewRenderer.getLayoutParams();
        if (params.width != 360 || params.height != 450) {
            params.width = 360;
            params.height = 450;
            viewRenderer.setLayoutParams(params);
        }
    }

    // 初始化ui，渲染，摄像头
    public void initSurfaceViewRendered(SurfaceViewRenderer viewRenderer) {
        // 硬件加速
        viewRenderer.setEnableHardwareScaler(true);
        // 设置镜像
        viewRenderer.setMirror(true);
        // 设置帧率
        viewRenderer.setFpsReduction(15);
        // 初始化
        viewRenderer.init(eglbaseContext, null);
        Log.d("test", "初始化成功---1");
    }

    public void initLocalSurfaceView(SurfaceViewRenderer view) {
        initSurfaceViewRendered(view);
        startLocalVideoStreaming(view);
    }

    private void startLocalVideoStreaming(SurfaceViewRenderer view) {
        SurfaceTextureHelper helper = SurfaceTextureHelper.create(
                Thread.currentThread().getName(), eglbaseContext
        );
        // 获取视频捕获器(获取前置摄像头)
        videoCapturer = getVideoCapture();
        // 初始化视频捕获器
        videoCapturer.initialize(helper, context, localVideoSource.getCapturerObserver());
        // 开始捕获视频 宽、高、帧率
        videoCapturer.startCapture(480,360,25);
        // 创建本地视频轨道
        localVideoTrack = peerConnectionFactory.createVideoTrack(
                localTrackId + "_video", localVideoSource
        );
        // 将视频轨道添加到渲染视图
        localVideoTrack.addSink(view);

        // 创建本地音频轨道
        localAudioTrack = peerConnectionFactory.createAudioTrack(localTrackId + "_audio", localAudioSource);
        // 创建本地媒体流
        localStream = peerConnectionFactory.createLocalMediaStream(localStreamId);
        // 将音频轨道和视频轨道添加到本地媒体流
        localStream.addTrack(localAudioTrack);
        localStream.addTrack(localVideoTrack);
        // 将本地媒体流添加到PeerConnection
        peerConnection.addStream(localStream);

        // 调整编码参数
//        adjustVideoTrackBitrate(localVideoTrack, 300000, 1500000, 30, 1.0); // 设置最小比特率300kbps，最大比特率1500kbps，初始比特率1000kbps
    }

    private void adjustVideoTrackBitrate(VideoTrack videoTrack, int minBitrate, int maxBitrate, int maxFramerate, double scaleResolution) {
        RtpSender videoSender = null;
        for (RtpSender sender : peerConnection.getSenders()) {
            if (sender.track() != null && sender.track().kind().equals(videoTrack.kind())) {
                videoSender = sender;
                break;
            }
        }

        if (videoSender != null) {
            RtpParameters parameters = videoSender.getParameters();
            for (RtpParameters.Encoding encoding : parameters.encodings) {
                encoding.maxBitrateBps = maxBitrate;
                encoding.minBitrateBps = minBitrate;
                encoding.maxFramerate = maxFramerate;
                encoding.scaleResolutionDownBy = scaleResolution;
            }
            videoSender.setParameters(parameters);
        }
    }

    private CameraVideoCapturer getVideoCapture() {
        // 创建Camera2枚举器
        Camera2Enumerator enumerator = new Camera2Enumerator(context);
        // 获取设备名称列表
        String[] deviceNames = enumerator.getDeviceNames();

        // 遍历设备名称列表
        for(String device : deviceNames) {
            // 检查设备是否是前置摄像头
            if (enumerator.isFrontFacing(device)) {
                // 创建前置摄像头捕获器
                CameraVideoCapturer cameraVideoCapturer = enumerator.createCapturer(device, null);
                if (cameraVideoCapturer != null) {
                    return cameraVideoCapturer;
                }
            }
        }
        // 如果遍历所有设备名称后没有找到前置摄像头，则抛出IllegalStateException异常
        throw new IllegalStateException("front facing camera not found");
    }

    public void initRemoteSurfaceView(SurfaceViewRenderer view) {
        initSurfaceViewRendered(view);
    }

    // negotiation section like call and answer
    // target参数是目标对等方的标识符，表示要呼叫的对方
    public void call(String target){
//        try{
//            peerConnection.createOffer(new MySdpObserver(){
//                @Override
//                public void onCreateSuccess(SessionDescription sessionDescription) {
//                    super.onCreateSuccess(sessionDescription);
//                    peerConnection.setLocalDescription(new MySdpObserver(){
//                        @Override
//                        public void onSetSuccess() {
//                            super.onSetSuccess();
//                            //its time to transfer this sdp to other peer
//                            if (listener!=null){
//                                listener.onTransferDataToOtherPeer(new DataModels(
//                                        target,username,sessionDescription.description, DataModelType.Offer
//                                ));
//                            }
//                        }
//                    },sessionDescription);
//                }
//            },mediaConstraints);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        try {
            Log.d("WebRTC", "Creating offer...");
            peerConnection.createOffer(new MySdpObserver() {
                @Override
                public void onCreateSuccess(SessionDescription sessionDescription) {
                    super.onCreateSuccess(sessionDescription);
                    Log.d("WebRTC", "Offer created successfully: " + sessionDescription.description);
                    peerConnection.setLocalDescription(new MySdpObserver() {
                        @Override
                        public void onSetSuccess() {
                            super.onSetSuccess();
                            Log.d("WebRTC", "Local description set successfully.");
                            if (listener != null) {
                                // Verify the description before sending
//                                String offerDescription = sessionDescription.description;
//                                Log.d("WebRTC", "Offer description before sending: " + offerDescription);
//
//                                // Create DataModels with the description
//                                DataModels dataModel = new DataModels(
//                                        target, username, offerDescription, DataModelType.Offer
//                                );
//                                Log.d("WebRTC", "DataModels created: " + dataModel);
//
//                                listener.onTransferDataToOtherPeer(dataModel);
//                                Log.d("WebRTC", "Offer transferred to peer.");
                                listener.onTransferDataToOtherPeer(new DataModels(
                                        target,username,sessionDescription.description, DataModelType.Offer
                                ));
                            }
                        }
                    }, sessionDescription);
                }

                @Override
                public void onCreateFailure(String error) {
                    super.onCreateFailure(error);
                    Log.e("WebRTC", "Offer creation failed: " + error);
                }
            }, mediaConstraints);
        } catch (Exception e) {
            Log.e("WebRTC", "Exception during call setup", e);
        }
    }

    //  target是目标对等方的标识符，表示要响应的对方
    public void answer(String target){
        try {
            peerConnection.createAnswer(new MySdpObserver() {
                @Override
                public void onCreateSuccess(SessionDescription sessionDescription) {
                    super.onCreateSuccess(sessionDescription);
                    peerConnection.setLocalDescription(new MySdpObserver() {
                        @Override
                        public void onSetSuccess() {
                            super.onSetSuccess();
                            // its time to transfer this sdp to other peer
                            if (listener != null) {
                                listener.onTransferDataToOtherPeer(new DataModels(
                                        target, username, sessionDescription.description, DataModelType.Answer
                                ));
                            }
                        }
                    }, sessionDescription);
                }
            }, mediaConstraints);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 接收远程会话描述
//    public void onRemoteSessionReceived(SessionDescription offer, String target) {
    public void onRemoteSessionReceived(SessionDescription sessionDescription) {
        peerConnection.setRemoteDescription(new MySdpObserver(), sessionDescription);
//        peerConnection.setRemoteDescription(new MySdpObserver() {
//            @Override
//            public void onSetSuccess() {
//                super.onSetSuccess();
//                // 创建 Answer 并设置本地描述
//                peerConnection.createAnswer(new MySdpObserver() {
//                    @Override
//                    public void onCreateSuccess(SessionDescription sessionDescription) {
//                        peerConnection.setLocalDescription(new MySdpObserver(), sessionDescription);
//                        // 传输 SDP Answer 给其他 Peer
//                        if (listener != null) {
//                            listener.onTransferDataToOtherPeer(new DataModels(
//                                    target, username, sessionDescription.description, DataModelType.Answer
//                            ));
//                        }
//                    }
//                }, mediaConstraints);
//            }
//        }, offer);
    }

    /**
     * 添加ICE候选
     * @param iceCandidate
     * sdpMid：媒体描述中的媒体标识符、标识特定的媒体流，例如音频或视频
     * sdpMLineIndex：媒体行索引，指示特定的媒体流，索引从0开始，通常音频流是第一个媒体行，视频流是第二个媒体行
     * sdp：包含连接和媒体信息的字符串，用于建立WebRTC对等连接
     * serverUrl：webrtc中通常用于指定连接相关的服务器地址，用于NAT遍历和连接建立
     * PeerConnection.AdapterType adapterType：
     * PeerConnection.AdapterType是一个枚举，表示网络适配器的类型，例如以太网、Wi-Fi等
     * adapterType：变量用于指示用于连接的网络适配器类型
     */
    public void addIceCandidate(IceCandidate iceCandidate) {
        peerConnection.addIceCandidate(iceCandidate);
    }

    // 发送ICE候选
    public void sendIceCandidate(IceCandidate iceCandidate, String target) {
        addIceCandidate(iceCandidate);
        if (listener != null) {
            listener.onTransferDataToOtherPeer(new DataModels(
                    target,username,gson.toJson(iceCandidate), DataModelType.IceCandidate
            ));
        }
    }

    // 切换摄像头
    public void switchCamera(SurfaceViewRenderer localRenderer, SurfaceViewRenderer remoteRenderer) {
        videoCapturer.switchCamera(new CameraVideoCapturer.CameraSwitchHandler() {
            @Override
            public void onCameraSwitchDone(boolean isFrontCamera) {
                // 切换摄像头成功后重新设置镜像
                localRenderer.setMirror(isFrontCamera);
                remoteRenderer.setMirror(isFrontCamera);
//                initSurfaceViewRendered(localRenderer);
//                initSurfaceViewRendered(remoteRenderer);
            }

            @Override
            public void onCameraSwitchError(String error) {
                Log.e("WebRTC", "Camera switch error: " + error);
            }
        });
    }

    // 切换Video
    public void toggleVideo(Boolean shouldBeMuted) {
        localVideoTrack.setEnabled(shouldBeMuted);
    }

    // 切换Audio
    public void toggleAudio(Boolean shouldBeMuted) {
        localAudioTrack.setEnabled(shouldBeMuted);
    }

    // 切换视频流
    public void swapVideoViews(SurfaceViewRenderer remoteView, SurfaceViewRenderer localView, FrameLayout frameLayout) {
        try {
            // 获取本地视频视图的布局参数
            ViewGroup.LayoutParams localParams = localView.getLayoutParams();
            // 获取远程视频视图的布局参数
            ViewGroup.LayoutParams remoteParams = remoteView.getLayoutParams();

            // 交换布局参数
            localView.setLayoutParams(remoteParams);
            remoteView.setLayoutParams(localParams);

            frameLayout.removeView(remoteView);
            frameLayout.removeView(localView);

            if (isRemoteViewFront) {
                frameLayout.addView(localView);
                frameLayout.addView(remoteView);
            } else {
                frameLayout.addView(remoteView);
                frameLayout.addView(localView);
            }

            isRemoteViewFront = !isRemoteViewFront;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 处理接收到的远程流
    public void onRemoteStream(MediaStream stream, SurfaceViewRenderer remoteView) {
        if (stream.videoTracks.size() > 0) {
            remoteVideoTrack = stream.videoTracks.get(0);
            remoteVideoTrack.addSink(remoteView);
        }
    }

    public void closeConnection() {
        try {
//            if (videoCapturer != null) {
//                videoCapturer.stopCapture();
//                videoCapturer.dispose();
//                videoCapturer = null;
//            }
//            if (localStream != null) {
//                localStream.dispose();
//                localStream = null;
//            }
//            if (localVideoTrack != null) {
//                localVideoTrack.dispose();
//                localVideoTrack = null;
//            }
//            if (remoteVideoTrack != null) {
//                remoteVideoTrack.dispose();
//                remoteVideoTrack = null;
//            }
//            if (localAudioTrack != null) {
//                localAudioTrack.dispose();
//                localAudioTrack = null;
//            }
//            if (localVideoSource != null) {
//                localVideoSource.dispose();
//                localVideoSource = null;
//            }
//            if (localAudioSource != null) {
//                localAudioSource.dispose();
//                localAudioSource = null;
//            }
//            if (peerConnection != null) {
//                peerConnection.close();
//                peerConnection.dispose();
//                peerConnection = null;
//            }
//            if (peerConnectionFactory != null) {
//                peerConnectionFactory.dispose();
//                peerConnectionFactory = null;
//            }
            localVideoTrack.dispose();
            videoCapturer.stopCapture();
            videoCapturer.dispose();
            peerConnection.close();
//            peerConnectionFactory.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopCapture() throws InterruptedException {
//        if (videoCapturer != null) {
//            videoCapturer.stopCapture();
//        }
        // 禁用视频轨道
        Log.d("test", "startCapture: localVideoTrack===" + localVideoTrack.state());
        if (localVideoTrack != null) {
            localVideoTrack.setEnabled(false);
        }

        // 启用视频轨道
//        localVideoTrack.setEnabled(true);
    }

    public void startCapture() throws InterruptedException {
//        if (videoCapturer != null) {
//            videoCapturer.startCapture(480,360,25);
//        }
        // 禁用视频轨道
//        localVideoTrack.setEnabled(false);

        // 启用视频轨道
        Log.d("test", "startCapture: localVideoTrack===" + localVideoTrack.state());
        if (localVideoTrack != null) {
            localVideoTrack.setEnabled(true);
        }
    }

    public void onRemoteIceCandidate(IceCandidate iceCandidate) {
        if (peerConnection != null) {
            peerConnection.addIceCandidate(iceCandidate);
        }
    }

    public void reconnect(PeerConnection.Observer observer, SurfaceViewRenderer localSurfaceViewRenderer) {
        // 关闭旧的连接并释放资源
        closeConnection();

        // 初始化新的 PeerConnectionFactory 和 PeerConnection
        initPeerConnectionFactory();
        peerConnectionFactory = createPeerConnectionFactory();
        peerConnection = createPeerConnection(observer); // 传递你的 PeerConnection.Observer

        // 初始化本地视频源和音频源
        localVideoSource = peerConnectionFactory.createVideoSource(false);
        localAudioSource = peerConnectionFactory.createAudioSource(new MediaConstraints());

        // 创建本地视频轨道
        startLocalVideoStreaming(localSurfaceViewRenderer); // 传递你的本地 SurfaceViewRenderer
    }

//    public void recreateIceConnection(String target) {
//        if (peerConnection != null) {
//            peerConnection.createOffer(new MySdpObserver() {
//                @Override
//                public void onCreateSuccess(SessionDescription sessionDescription) {
//                    super.onCreateSuccess(sessionDescription);
//                    peerConnection.setLocalDescription(new MySdpObserver() {
//                        @Override
//                        public void onSetSuccess() {
//                            super.onSetSuccess();
//                            if (listener != null) {
//                                listener.onTransferDataToOtherPeer(new DataModels(
//                                        target, username, sessionDescription.description, DataModelType.Offer
//                                ));
//                            }
//                        }
//                    }, sessionDescription);
//                }
//
//                @Override
//                public void onCreateFailure(String error) {
//                    super.onCreateFailure(error);
//                    Log.e("WebRTC", "Failed to recreate offer: " + error);
//                }
//            }, mediaConstraints);
//        }
//    }

    public void restartLocalVideoStreaming(SurfaceViewRenderer view) {
        SurfaceTextureHelper helper = SurfaceTextureHelper.create(
                Thread.currentThread().getName(), eglbaseContext
        );
        // 获取视频捕获器(获取前置摄像头)
        videoCapturer = getVideoCapture();
        // 初始化视频捕获器
        videoCapturer.initialize(helper, context, localVideoSource.getCapturerObserver());
        // 开始捕获视频 宽、高、帧率
        videoCapturer.startCapture(480,360,25);
        // 创建本地视频轨道
        localVideoTrack = peerConnectionFactory.createVideoTrack(
                localTrackId + "_video", localVideoSource
        );
        // 将视频轨道添加到渲染视图
        localVideoTrack.addSink(view);

        // 添加本地视频轨道到本地媒体流
        localStream.addTrack(localVideoTrack);

        // 确保更新PeerConnection中的本地媒体流
        peerConnection.removeStream(localStream);
        peerConnection.addStream(localStream);
    }

    public interface Listener {
        void onTransferDataToOtherPeer(DataModels model);

//        void onTransferDataToOtherPeer(DataModels model, ErrorCallback errorCallback, Context context);

    }
}
