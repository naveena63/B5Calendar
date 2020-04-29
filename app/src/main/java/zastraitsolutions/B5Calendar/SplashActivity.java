package zastraitsolutions.B5Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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

import zastraitsolutions.B5Calendar.Calendar.DateAdapter;
import zastraitsolutions.B5Calendar.Calendar.DateModel;
import zastraitsolutions.B5Calendar.Calendar.EventAdapter;
import zastraitsolutions.B5Calendar.Calendar.EventModel;
import zastraitsolutions.B5Calendar.Utils.AppConstants;
import zastraitsolutions.B5Calendar.Utils.PrefManager;

public class SplashActivity extends AppCompatActivity {

    PrefManager prefManager;

    String version;
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
        unique_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("deviceId", "deviceid" + unique_id);
        prefManager.storeValue(AppConstants.DEVICE_ID, unique_id);
        prefManager.setDeviceId(unique_id);


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
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (!prefManager.getBoolean(AppConstants.APP_USER_LOGIN)) {
//                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                    startActivity(intent);
//
//                    finish();
//                } else {
//                    Intent intent = new Intent(SplashActivity.this, BottomNavigtnActivity.class);
//                    startActivity(intent);
//
//                    finish();
//                }
//            }
//        }, 3000);
//

        prefManager = new PrefManager(this);

        //declartn
      //  apicall();
    }

    private void apicall() {
        getVersionInfo();
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String version_code=jsonObject.getString("version_code");

                    Log.e("versoncode",version_code+" "+version);
                    if (version_code.equals(version)) {
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
                    }
                    else {
                        showDialog(SplashActivity.this);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
        };
        requestQueue.add(stringRequest);
    }

    private void getVersionInfo() {
        String versionName = "";
        int versionCode = -1;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = String.valueOf(versionCode);


//        TextView textViewVersionInfo = (TextView) findViewById(R.id.textview_version_info);
//        textViewVersionInfo.setText(String.format("Version name = %s \nVersion code = %d", versionName, versionCode));
    }
    public void showDialog(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.show();
        dialog.setContentView(R.layout.update_dailog);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_update);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://play.google.com/store/apps/details?id=com.zastrait.qrcode";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }
}

