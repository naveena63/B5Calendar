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
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import zastraitsolutions.B5Calendar.LoginActivity;
import zastraitsolutions.B5Calendar.R;
import zastraitsolutions.B5Calendar.Utils.AppConstants;
import zastraitsolutions.B5Calendar.Utils.PrefManager;

public class ChangePaswordActivity extends AppCompatActivity {
    EditText newPaswrd, cnfrmPaswrd;
    Button buttonSubmit;
    ProgressDialog progressBar;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pasword);
        newPaswrd = findViewById(R.id.newPaswrd);
        cnfrmPaswrd = findViewById(R.id.cnfrmPaswrd);
        prefManager = new PrefManager(this);
        progressBar = new ProgressDialog(ChangePaswordActivity.this);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int verify = validate();
                if (verify == 0) {
                    changePaswrd();
                }
            }
        });


    }


    private void changePaswrd() {
//        final String phone = prefManager.getPhone_number();
        final String phonenum = getIntent().getExtras().getString("phonenumber");
        Log.i("phonenumchnge", "phone" + phonenum);
        final String new_pwd = newPaswrd.getText().toString();
        final String change_pwd = cnfrmPaswrd.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.BASE_URL+AppConstants.CHANGEPASWRD,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);

                            String status = object.getString("status");

                            String msg = object.getString("msg");
                            switch (status) {
                                case "success":
                                    Toast.makeText(ChangePaswordActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ChangePaswordActivity.this, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case "0":
                                    Toast.makeText(ChangePaswordActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                                    break;
                                case "error":
                                    Toast.makeText(ChangePaswordActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                                    break;
                                case "error1":
                                    Toast.makeText(ChangePaswordActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("DELETE CART", "_--------------DELETE Response----------------" + response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                        Toast.makeText(ChangePaswordActivity.this, "Something Went wrong.. try again", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("phone_number", phonenum);
                map.put("new_pass", new_pwd);
                map.put("confirm_pass", change_pwd);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private int validate() {
        int flag = 0;
        if (newPaswrd.getText().toString().length()==0) {
            newPaswrd.setError("Password Required");
            newPaswrd.requestFocus();
            flag = 1;
        } else if (newPaswrd.getText().toString().length() < 8) {
            newPaswrd.setError("password requires atleast 8 characters");
            newPaswrd.requestFocus();
            flag = 1;
        }else if (cnfrmPaswrd.getText().toString().length()== 0) {
            cnfrmPaswrd.setError("Password Required");
            cnfrmPaswrd.requestFocus();
            flag = 1;
        }
        return flag;
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(ChangePaswordActivity.this, ForgotPaswrdActivity.class));
        finish();

    }

}
