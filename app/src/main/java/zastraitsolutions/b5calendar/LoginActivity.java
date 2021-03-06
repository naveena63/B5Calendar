package zastraitsolutions.b5calendar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;

import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


import zastraitsolutions.b5calendar.Utils.AppConstants;
import zastraitsolutions.b5calendar.Utils.CustomTextViewNormal;
import zastraitsolutions.b5calendar.Utils.PrefManager;
import zastraitsolutions.b5calendar.forgotpaswrd.ForgotPaswrdActivity;

public class LoginActivity extends AppCompatActivity {
    EditText mobile, loginPaswrd;
    Button buttonLogin;
    CustomTextViewNormal createNewAccount;
TextView forgotPasword;
    PrefManager prefManager;
    SharedPreferences sharedpreferences,useridpref;
    SharedPreferences.Editor editor,edit;
    String PREFERENCE = "AGENT";
    ProgressBar progressBar;
    String deviceToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //getSupportActionBar().hide();
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/segoeui.ttf");
        ProgressBar progressBar = new ProgressBar(this);
        mobile = findViewById(R.id.loginMobile);
        loginPaswrd = findViewById(R.id.loginPaswrd);
        buttonLogin = findViewById(R.id.buttonLogin);
        createNewAccount = findViewById(R.id.createNewAccount);
        forgotPasword = findViewById(R.id.forgotPasword);
        mobile.setTypeface(typeface);
        loginPaswrd.setTypeface(typeface);
        buttonLogin.setTypeface(typeface);
        sharedpreferences = this.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        prefManager = new PrefManager(this);


        useridpref = getApplicationContext().getSharedPreferences("USerid", MODE_PRIVATE);



        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
        forgotPasword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPaswrdActivity.class);
                startActivity(intent);
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int verify = validate();
                if (verify == 0) {

                    loginIn();
                }
            }
        });
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                deviceToken = instanceIdResult.getToken();
                Log.d("devicetoken", "devicesToken" + deviceToken);


                prefManager.storeValue(AppConstants.REFRESH_TOKEN, deviceToken);
                prefManager.setToken(deviceToken);
                Log.d("token", "token" + prefManager.getToken());
            }
            // or directly send it to server
        });
//        Intent i = new Intent();
//        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        i.addCategory(Intent.CATEGORY_DEFAULT);
//        i.setData(Uri.parse("package:" + this.getPackageName()));
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//        this.startActivity(i);
//        Toast.makeText(this, "Please on the notifications to display notifictions on lockscreen", Toast.LENGTH_LONG).show();
//        }
    }




    private void loginIn() {

        final String phone = mobile.getText().toString();
        final String paswrd = loginPaswrd.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.BASE_URL + AppConstants.LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equals("true")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String user_role_id = jsonObject1.getString("user_role_id");
                        String name = jsonObject1.getString("user_name");
                        String email = jsonObject1.getString("email");
                        String mobile = jsonObject1.getString("phone_number");
                        String user_id = jsonObject1.getString("id_user");


                        Log.i("userid11111","userid"+user_id);
                        edit = useridpref.edit();
                        edit.putString("useridnotifications", user_id);
                        edit.commit();


                         prefManager.storeValue(AppConstants.USER_ID,user_id);
                        prefManager.setUserid(user_id);
                        Log.i("userid","userid"+prefManager.getUserid());


                        String login_type = jsonObject1.getString("user_role_id");

                        editor.putString("login_type", login_type);
                        editor.commit();

                        prefManager.storeValue(AppConstants.APP_USER_LOGIN, true);
                        Log.e("b5", sharedpreferences.getString("login_type", ""));
                        //admin
                        if (login_type.equalsIgnoreCase("1")) {

                            Intent intent = new Intent(LoginActivity.this, BottomNavigtnActivity.class);
                            startActivity(intent);
                        }
                        //user
                        else if (login_type.equalsIgnoreCase("2")) {
                            Intent intent = new Intent(LoginActivity.this, BottomNavigtnActivity.class);
                            startActivity(intent);
                        }
                    } else if (status.equalsIgnoreCase("false")) {

                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("Admin_login", "_-------------Admin Login Response----------------" + response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "No network ", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("phone_number", phone);
                map.put("password", paswrd);
                map.put("device_id",deviceToken);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private int validate() {
        int flag = 0;
        if (mobile.getText().toString().length() != 10) {
            mobile.setError("Phone Number required 10 digits only");
            mobile.requestFocus();
            flag = 1;
        }
//        else if (loginPaswrd.getText().toString().length() <8) {
//            loginPaswrd.setError("Password atleast 8 characters long");
//            loginPaswrd.requestFocus();
//            flag = 1;
//        }

        return flag;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                }).create().show();
    }
}
