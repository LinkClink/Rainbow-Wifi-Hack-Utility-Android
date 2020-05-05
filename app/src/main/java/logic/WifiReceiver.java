package logic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class WifiReceiver extends BroadcastReceiver
{
    private WifiManager wifiManager;
    private ListView wifiDeviceList;

    public WifiReceiver(WifiManager wifiManager, ListView wifiDeviceList)
    {
        this.wifiManager = wifiManager;
        this.wifiDeviceList = wifiDeviceList;
    }

    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();

        if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action))
        {
            StringBuilder stringBuilder = new StringBuilder();
            List<ScanResult> wifiList = wifiManager.getScanResults();
            ArrayList<String> deviceList = new ArrayList<>();

            for (ScanResult scanResult : wifiList) {
                stringBuilder.append("\n").append(scanResult.SSID).append(" - ").append(scanResult.capabilities); /* Do not usage */
                deviceList.add(scanResult.SSID); /* deviceList.add(scanResult.SSID + " - " + scanResult.capabilities) */
            }

             ShowToast.showToast(context, "Update list");
             ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, deviceList.toArray());
             wifiDeviceList.setAdapter(arrayAdapter);
        }
    }
}


