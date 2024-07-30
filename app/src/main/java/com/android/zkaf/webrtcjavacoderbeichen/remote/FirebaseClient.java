package com.android.zkaf.webrtcjavacoderbeichen.remote;

import com.android.zkaf.webrtcjavacoderbeichen.utils.DataModels;
import com.android.zkaf.webrtcjavacoderbeichen.utils.ErrorCallback;
import com.android.zkaf.webrtcjavacoderbeichen.utils.NewEventCallBack;
import com.android.zkaf.webrtcjavacoderbeichen.utils.SuccessCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.Objects;

import androidx.annotation.NonNull;

public class FirebaseClient {

   private final Gson gson = new Gson();
   private final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
   private String currentUsername;
   private static final String LATEST_EVENT_FIELD_NAME = "latest_event";
   public void login(String username, SuccessCallback callback) {
      dbRef.child(username).setValue("").addOnCompleteListener(task -> {
         currentUsername = username;
         callback.onSuccess();
      });
   }

   public void  sendMessageToOtherUser(DataModels dataModel, ErrorCallback errorCallback) {
      dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.child(dataModel.getTarget()).exists()){
               //send the signal to other user
               dbRef.child(dataModel.getTarget()).child(LATEST_EVENT_FIELD_NAME)
                       .setValue(gson.toJson(dataModel));

            }else {
               errorCallback.onError();
            }
         }

         @Override
         public void onCancelled(@NonNull DatabaseError error) {
            errorCallback.onError();
         }
      });
   }

   public void observeIncomingLatestEvent(NewEventCallBack newEventCallBack) {
      dbRef.child(currentUsername).child(LATEST_EVENT_FIELD_NAME).addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot snapshot) {
            try {
               String data = Objects.requireNonNull(snapshot.getValue()).toString();
               DataModels dataModels = gson.fromJson(data, DataModels.class);
               newEventCallBack.onNewEventReceived(dataModels, currentUsername);
            } catch (Exception e) {
               e.printStackTrace();
            }
         }

         @Override
         public void onCancelled(@NonNull DatabaseError error) {

         }
      });
   }
}
