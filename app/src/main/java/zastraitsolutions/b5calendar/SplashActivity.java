package zastraitsolutions.b5calendar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import zastraitsolutions.b5calendar.Calendar.DateAdapter;
import zastraitsolutions.b5calendar.Calendar.DateModel;
import zastraitsolutions.b5calendar.Calendar.EventAdapter;
import zastraitsolutions.b5calendar.Calendar.EventModel;
import zastraitsolutions.b5calendar.Utils.AppConstants;
import zastraitsolutions.b5calendar.Utils.PrefManager;

public class SplashActivity extends AppCompatActivity {

    PrefManager prefManager;

    private static final String CHANNEL_ID = "4565";
    private NotificationChannel mChannel;
    private NotificationManager notifManager;
    int m = 0;
    String unique_id;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        prefManager = new PrefManager(this);
//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (!task.isSuccessful()) {
////To do//
//                            return;
//                        }
//
//// Get the Instance ID token//
//                        String token = task.getResult().getToken();
//                        String msg = getString(R.string.fcm_token, token);
//                        Log.d("jdcdn", msg);
//
//                    }
//                });
//

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channcel_desc);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            FirebaseMessaging.getInstance().subscribeToTopic("weather")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = getString(R.string.msg_subscribed);
                            if (!task.isSuccessful()) {
                                msg = getString(R.string.msg_subscribe_failed);
                            }
                            Log.d("app", msg);
                            // Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        //this code will be written in splash screen
//        unique_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//        Log.d("deviceId", "deviceid" + unique_id);
//        prefManager.storeValue(AppConstants.DEVICE_ID, unique_id);
//        prefManager.setDeviceId(unique_id);


        //this  token will changes for Every Device this  token  will give to server
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String deviceToken = instanceIdResult.getToken();
                Log.d("devicetoken", "devicesToken" + deviceToken);
                prefManager.storeValue(AppConstants.REFRESH_TOKEN, deviceToken);
                prefManager.setToken(deviceToken);
                Log.d("token", "token" + prefManager.getToken());
            }
            // or directly send it to server
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!prefManager.getBoolean(AppConstants.APP_USER_LOGIN)) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);

                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, BottomNavigtnActivity.class);
                    startActivity(intent);

                    finish();
                }
            }
        }, 3000);


        prefManager = new PrefManager(this);

        //declartn

    }

}

