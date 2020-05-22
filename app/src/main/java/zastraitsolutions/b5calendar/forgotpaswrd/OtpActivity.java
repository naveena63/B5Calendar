package zastraitsolutions.b5calendar.forgotpaswrd;

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
import zastraitsolutions.b5calendar.R;
import zastraitsolutions.b5calendar.Utils.AppConstants;
import zastraitsolutions.b5calendar.Utils.PrefManager;

public class OtpActivity extends AppCompatActivity {

EditText OtpEt;
Button submitOTP;
    ProgressDialog progressBar;
    PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        OtpEt=findViewById(R.id.OtpEt);
        prefManager=new PrefManager(OtpActivity.this);
        submitOTP=findViewById(R.id.submitOTP);
        progressBar = new ProgressDialog(OtpActivity.this);
        submitOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int verify= validate();
                if (verify==0)
                {
                    otpVerify();
                }
            }
        });

    }

    private void otpVerify() {

        final String otp = OtpEt.getText().toString();
        //final String phonenum=prefManager.getPhone_number();
        final String phonenum = getIntent().getExtras().getString("phonenumber");
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.BASE_URL+AppConstants.Forgot_otp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressBar.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")){
                        Intent intent=new Intent(OtpActivity.this,ChangePaswordActivity.class);
                        intent.putExtra("phonenumber", phonenum);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        Toast.makeText(OtpActivity.this, ""+jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                    else if(status.equals("error"))
                    {
                        Toast.makeText(OtpActivity.this, ""+jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("Otp", "_------------Otp Response----------------" + response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  setProgressDialog();
                        progressBar.dismiss();

                        Toast.makeText(OtpActivity.this, ""+error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("mobile_number", phonenum);
                map.put("otp", otp);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private int validate() {
        int flag=0;
        if(OtpEt.getText().toString().length()==0)
        {
            OtpEt.setError("enter this field");
            OtpEt.requestFocus();
            flag=1;
        }
       else if (OtpEt.getText().toString().length() != 4) {
            OtpEt.setError("otp requires 4 digits");
            OtpEt.requestFocus();
            flag = 1;
        }
      return   flag;
    }
}
