package id.akakom.bimo.shareloc.module.Firebase;

import android.content.Intent;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import id.akakom.bimo.shareloc.data.models.SharelocData;

public class FirebaseMService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMService";
    Gson gson = new Gson();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived: "+remoteMessage.getData());
        SharelocData ntf = gson.fromJson(remoteMessage.getData().get("data"), SharelocData.class);

        sendBroadcast(new Intent(remoteMessage.getData().get("type")).putExtra("data", gson.toJson(ntf)));
    }
}
