package zastraitsolutions.b5calendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import zastraitsolutions.b5calendar.Calendar.MyCalendarFragment;
import zastraitsolutions.b5calendar.Notification.NotificationFragment;
import zastraitsolutions.b5calendar.SettingsMenu.SettingsFragment;


public class BottomNavigtnActivity extends AppCompatActivity {
    String sessionId;
    TextView textviewScroll;
    HomeFragment homeFragment = new HomeFragment();
    NotificationFragment notificationFragment=new NotificationFragment();
    SettingsFragment settingsFragment=new SettingsFragment();
    MyCalendarFragment myCalendarFragment=new MyCalendarFragment();

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
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        TextView tv = (TextView) this.findViewById(R.id.textviewScroll);
        TextView tv1 = (TextView) this.findViewById(R.id.textviewScroll1);
        tv.setSelected(true);
        tv1.setSelected(true);

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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
