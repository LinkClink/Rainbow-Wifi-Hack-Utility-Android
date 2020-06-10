package WifiTools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import logic.ShowToast;

public class WifiReceiver extends BroadcastReceiver {

    private WifiManager wifiManager;
    private ListView wifiDeviceList;

    private ProgressBar progressBar;

    public WifiReceiver(WifiManager wifiManager, ListView wifiDeviceList, ProgressBar progressBar) {
        this.wifiManager = wifiManager;
        this.wifiDeviceList = wifiDeviceList;
        this.progressBar = progressBar;
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {

            List<ScanResult> wifiList = wifiManager.getScanResults();
            ArrayList<String> deviceList = new ArrayList<>();

            for (ScanResult scanResult : wifiList) {
                deviceList.add(scanResult.SSID);
            }

            ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, deviceList.toArray());
            wifiDeviceList.setAdapter(arrayAdapter);

            /* if array list has wifi list */
            if (!arrayAdapter.isEmpty()) {
                ShowToast.showToast(context, "Update wifi list:");
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }
}


