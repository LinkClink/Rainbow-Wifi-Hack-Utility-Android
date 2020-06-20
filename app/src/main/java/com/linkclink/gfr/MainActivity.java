package com.linkclink.gfr;

import AnotherFragments.LogFragment;
import AnotherFragments.PasswordListFragment;
import BruteFragment.BruteFragment;
import TestFragment.TestFragment;
import WifiTools.WifiListFragment;
import WifiTools.WifiOnOffFragment;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final int ACCESS_FINE_LOCATION = 100;

    private int[] fragmentState = {1, 1, 1, 1, 1, 1};

    private FrameLayout frameLayoutWifiList;
    private FrameLayout frameLayoutWifiOnOff;
    private FrameLayout frameLayoutBrute;
    private FrameLayout frameLayoutTest;
    private FrameLayout frameLayoutPassword;
    private FrameLayout frameLayoutLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_with_button);
        Initialisations();

        checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, ACCESS_FINE_LOCATION);

        loadFragment(new WifiListFragment(), R.id.frame_wifi_list);
        loadFragment(new WifiOnOffFragment(), R.id.frame_wifi_onn_off);
        loadFragment(new BruteFragment(), R.id.frame_brute);
        loadFragment(new TestFragment(), R.id.frame_test);
        loadFragment(new PasswordListFragment(), R.id.frame_password);
        loadFragment(new LogFragment(), R.id.frame_log);
    }

    private void Initialisations() {
        frameLayoutWifiList = (FrameLayout) findViewById(R.id.frame_wifi_list);
        frameLayoutWifiOnOff = (FrameLayout) findViewById(R.id.frame_wifi_onn_off);
        frameLayoutBrute = (FrameLayout) findViewById(R.id.frame_brute);
        frameLayoutTest = (FrameLayout) findViewById(R.id.frame_test);
        frameLayoutPassword = (FrameLayout) findViewById(R.id.frame_password);
        frameLayoutLog = (FrameLayout) findViewById(R.id.frame_log);
    }

    public void loadFragment(Fragment fragment, int frame) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frame, fragment);
        fragmentTransaction.commit();
    }

    public void OpenFragmentSettingsDialog(View view) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_fragment_settings);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void HideFragments(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.switch_wifi_list: {
                if (fragmentState[0] == 1) {
                    fragmentState[0] = 0;
                    frameLayoutWifiList.setVisibility(View.GONE);
                } else {
                    fragmentState[0] = 1;
                    frameLayoutWifiList.setVisibility(View.VISIBLE);
                } break;
            }
            case R.id.switch_wifi_on_off: {
                if (fragmentState[1] == 1) {
                    fragmentState[1] = 0;
                    frameLayoutWifiOnOff.setVisibility(View.GONE);
                } else {
                    fragmentState[1] = 1;
                    frameLayoutWifiOnOff.setVisibility(View.VISIBLE);
                } break;
            }
            case R.id.switch_brute: {
                if (fragmentState[2] == 1) {
                    fragmentState[2] = 0;
                    frameLayoutBrute.setVisibility(View.GONE);
                } else {
                    fragmentState[2] = 1;
                    frameLayoutBrute.setVisibility(View.VISIBLE);
                } break;
            }

            case R.id.switch_test: {
                if (fragmentState[3] == 1) {
                    fragmentState[3] = 0;
                    frameLayoutTest.setVisibility(View.GONE);
                } else {
                    fragmentState[3] = 1;
                    frameLayoutTest.setVisibility(View.VISIBLE);
                } break;
            }

            case R.id.switch_password: {
                if (fragmentState[4] == 1) {
                    fragmentState[4] = 0;
                    frameLayoutPassword.setVisibility(View.GONE);
                } else {
                    fragmentState[4] = 1;
                    frameLayoutPassword.setVisibility(View.VISIBLE);
                } break;
            }

            case R.id.switch_log: {
                if (fragmentState[5] == 1) {
                    fragmentState[5] = 0;
                    frameLayoutLog.setVisibility(View.GONE);
                } else {
                    fragmentState[5] = 1;
                    frameLayoutLog.setVisibility(View.VISIBLE);
                } break;
            }
        }
    }

    /* First check permissions for visible wifi list  Android 6+*/
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
        }
    }

    /* Get result */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Location Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Please granted permission to visible wifi list", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


