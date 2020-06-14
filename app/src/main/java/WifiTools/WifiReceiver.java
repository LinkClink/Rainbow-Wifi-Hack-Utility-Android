package WifiTools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import com.linkclink.gfr.R;

import java.util.ArrayList;
import java.util.HashMap;
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
            ArrayList<HashMap<String, String>> wifiListWithDetails = new ArrayList<>();
            ArrayList<String> wifiListNames = new ArrayList<>();

            SetWifiImageSignal setWifiImageSignal = new SetWifiImageSignal();

            for (ScanResult scanResult : wifiList) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("image", String.valueOf(setWifiImageSignal.CheckWifiSignal(scanResult.level)));
                hashMap.put("name", String.valueOf(scanResult.SSID));
                hashMap.put("mac","Mac:" + String.valueOf(scanResult.BSSID));
                hashMap.put("capabilities",String.valueOf(scanResult.capabilities));
                wifiListNames.add(scanResult.SSID);
                wifiListWithDetails.add(hashMap);
            }

            SimpleAdapter simpleAdapter = new SimpleAdapter(context, wifiListWithDetails, R.layout.wifi_list_adapter, new String[]
                    {"name","mac","capabilities","image"}, new int[]{R.id.textView_adapter0, R.id.textView_adapter1,R.id.textView_adapter2,R.id.imageView_adapter0});

            wifiDeviceList.setAdapter(simpleAdapter);
            /* set wifi names-list */
            WifiListFragment.setWifiListNames(wifiListNames);

            /* if array list has wifi list */
            if (!simpleAdapter.isEmpty()) {
                ShowToast.showToast(context, "Update wifi list:");
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }
}


