package zastraitsolutions.B5Calendar.forgotpaswrd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import zastraitsolutions.B5Calendar.R;
import zastraitsolutions.B5Calendar.Utils.AppConstants;
import zastraitsolutions.B5Calendar.Utils.PrefManager;

public class ForgotPaswrdActivity extends AppCompatActivity {
EditText phoneNumber;
Button buttonsubmit;
   ProgressDialog progressBar;
    PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_paswrd);
        phoneNumber=findViewById(R.id.phoneNumber);
        buttonsubmit=findViewById(R.id.buttonsubmit);
        progressBar = new ProgressDialog(ForgotPaswrdActivity.this);
        prefManager=new PrefManager(ForgotPaswrdActivity.this);
        buttonsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int verify = validate();
                if (verify == 0) {
                    forgotPasword();
                }
            }
        });


    }

    private void forgotPasword() {


        final String phone = phoneNumber.getText().toString();


        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.BASE_URL+AppConstants.Forgot_pasword, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressBar.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")){
                        Intent intent=new Intent(ForgotPaswrdActivity.this,OtpActivity.class);
                        intent.putExtra("phonenumber", phone);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        Toast.makeText(ForgotPaswrdActivity.this, "success", Toast.LENGTH_SHORT).show();
                    }
                  else if(status.equals("error"))
                    {
                        Toast.makeText(ForgotPaswrdActivity.this, ""+jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("forgotpasword", "_------------forgotpassword Response----------------" + response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  setProgressDialog();
                        progressBar.dismiss();

                        Toast.makeText(ForgotPaswrdActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("mobile_number", phone);

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private int validate() {
        int flag = 0;
        if (phoneNumber.getText().toString().length() == 0) {
            phoneNumber.setError("fill this filed");
            phoneNumber.requestFocus();
            flag = 1;
        }
       else if (phoneNumber.getText().toString().length() != 10) {
            phoneNumber.setError("Phone Number required 10 digits only");
            phoneNumber.requestFocus();
            flag = 1;
        }

        return flag;
    }
}
