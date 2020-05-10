package com.linkclink.gfr;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import logic.ShowToast;

public class AccessToPermissions extends MainActivity {

    private Context context;

    private static final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1;
    private static final int MY_PERMISSIONS_ACCESS_WIFI_STATE = 1;
    private static final int MY_PERMISSIONS_CHANGE_WIFI_STATE = 1;

    public void CheckPermissions(Context context)
    {
        ShowToast.showToast(context,"Get permissions:");
        this.context = context;

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, MY_PERMISSIONS_ACCESS_WIFI_STATE);
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CHANGE_WIFI_STATE}, MY_PERMISSIONS_CHANGE_WIFI_STATE);
        }
    }
}
