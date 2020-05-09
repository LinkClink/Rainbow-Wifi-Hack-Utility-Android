package com.linkclink.gfr;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        AccessToPermissions accessToPermissions = new AccessToPermissions();
        accessToPermissions.CheckPermissions(getApplicationContext());
        */
    }

}

