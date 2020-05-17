package zastraitsolutions.B5Calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import zastraitsolutions.B5Calendar.Utils.AppConstants;
import zastraitsolutions.B5Calendar.Utils.GlobalVariable;
import zastraitsolutions.B5Calendar.Utils.PrefManager;

public class LaunchingActivity extends AppCompatActivity {
    Handler handler;
    ImageView imageView;
    PrefManager prefManager;
    String version;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launching);
        imageView = findViewById(R.id.spalsgLogo);
        prefManager = new PrefManager(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        GlobalVariable.deviceWidth = displayMetrics.widthPixels;
        GlobalVariable.deviceHeight = displayMetrics.heightPixels;
        apicall();

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

    public void apicall() {
        getVersionInfo();
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.BASE_URL + AppConstants.Version, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String version_code=jsonObject.getString("version_code");

                    Log.e("versoncode",version_code+" "+version);
                    if (version_code.equals(version)){
                        handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {


                                if (!prefManager.getBoolean(AppConstants.APP_USER_LOGIN)) {
                                    Intent intent = new Intent(LaunchingActivity.this, LoginActivity.class);
                                    startActivity(intent);

                                    finish();
                                } else {
                                    Intent intent = new Intent(LaunchingActivity.this, BottomNavigtnActivity.class);
                                    startActivity(intent);

                                    finish();
                                }
                            }
                        }, 1000);
                    }
                    else {
                        showDialog(LaunchingActivity.this);
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
