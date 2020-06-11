package zastraitsolutions.b5calendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import zastraitsolutions.b5calendar.Calendar.MyCalendarFragment;
import zastraitsolutions.b5calendar.Notification.NotificationFragment;
import zastraitsolutions.b5calendar.Notification.NotifictaionAdapter;
import zastraitsolutions.b5calendar.Notification.NotifictaionModel;
import zastraitsolutions.b5calendar.SettingsMenu.SettingsFragment;
import zastraitsolutions.b5calendar.Utils.AppConstants;
import zastraitsolutions.b5calendar.Utils.PrefManager;


public class BottomNavigtnActivity extends AppCompatActivity {
    String sessionId;
    TextView textviewScroll,notifications,badgeMark;
    HomeFragment homeFragment = new HomeFragment();
    NotificationFragment notificationFragment=new NotificationFragment();
    SettingsFragment settingsFragment=new SettingsFragment();
    MyCalendarFragment myCalendarFragment=new MyCalendarFragment();
    View v;
    View badge;

    PrefManager prefManager;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    FragmentManager fragmentManager1 = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                    fragmentTransaction1.replace(R.id.fragment_container, homeFragment).commit();
                    return true;
                case R.id.navigation_notifications:
                    FragmentManager fragmentManager2 = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                    fragmentTransaction2.replace(R.id.fragment_container, notificationFragment).commit();
                    notifictionReadunread();
                  //  v.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.navigation_settings:
                    FragmentManager fragmentManager3 = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction3 = fragmentManager3.beginTransaction();
                    fragmentTransaction3.replace(R.id.fragment_container, settingsFragment).commit();
                    return true;
                    case R.id.navigation_myclaendare:
                    FragmentManager fragmentManager4= getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction4 = fragmentManager4.beginTransaction();
                    fragmentTransaction4.replace(R.id.fragment_container, myCalendarFragment).commit();
                    return true;

            }

            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigtn);
        getUserData();
        prefManager=new PrefManager(this);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_view);
        notifications=(TextView) MenuItemCompat.getActionView(navigation.getMenu().
                findItem(R.id.navigation_settings));
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        TextView tv = (TextView) this.findViewById(R.id.textviewScroll);
        TextView tv1 = (TextView) this.findViewById(R.id.textviewScroll1);
        tv.setSelected(true);
        tv1.setSelected(true);

        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) navigation.getChildAt(0);
         v = bottomNavigationMenuView.getChildAt(2);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;
        v.setVisibility(View.VISIBLE);
         badge = LayoutInflater.from(this)
                .inflate(R.layout.notification_badge, itemView, true);

         badgeMark=v.findViewById(R.id.notificationsbadge);

        sessionId = getIntent().getStringExtra("session");
        if (sessionId=="1")
        {
            Log.e("sessionId",sessionId);
            FragmentManager fragmentManageri = getSupportFragmentManager();
            FragmentTransaction fragmentTransactioni = fragmentManageri.beginTransaction();
            fragmentTransactioni.replace(R.id.fragment_container, myCalendarFragment).commit();
        }
        else
        {
            FragmentManager fragmentManager1 = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
            fragmentTransaction1.replace(R.id.fragment_container, homeFragment).commit();
        }



    }

    private void notifictionReadunread() {

        StringRequest stringRequest=new StringRequest(Request.Method.POST,  AppConstants.notifictaionNotstatus, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("notifi","notf"+response);
                badgeMark.setVisibility(View.INVISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> maps=new HashMap<>();

                maps.put("id",prefManager.getUserid());
                Log.i("getstatus","getStatu"+prefManager.getUserid());
                return maps;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void getUserData() {
        String url_formation = AppConstants.notifictaion;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_formation, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("notificationresposne", "response" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("success")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("notifications");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            String title = jsonObject2.getString("title");
                            String message = jsonObject2.getString("message");
                            String statusmessafe = jsonObject2.getString("status");
                            String id = jsonObject2.getString("id");
                            String user_id = jsonObject2.getString("user_id");


                        }

                    }

                    String notifictaionStatus=jsonObject.getString("notification_status");
                    if (notifictaionStatus.equals("read")){

                        badgeMark.setVisibility(View.INVISIBLE);
                    }else if(notifictaionStatus.equals("unread"))
                    {
                        badgeMark.setVisibility(View.VISIBLE);
                    }


                    Log.i("notifictaionSttus","notifi"+notifictaionStatus);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BottomNavigtnActivity.this, "Couldn't Find Network", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", prefManager.getUserid());
                Log.i("user_id","useR_id"+prefManager.getUserid());
                return param;
            }
        };
      RequestQueue  requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
