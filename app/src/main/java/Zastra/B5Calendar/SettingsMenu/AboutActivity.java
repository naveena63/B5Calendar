package Zastra.B5Calendar.SettingsMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import Zastra.B5Calendar.R;

public class AboutActivity extends AppCompatActivity {
Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getSupportActionBar().hide();
        setContentView(R.layout.activity_about);
        toolbar=findViewById(R.id.toolbar_about);

        toolbar.setTitle("AboutUs");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }
}
