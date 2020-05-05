package WifiListFragment;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import logic.ShowToast;
import logic.WifiReceiver;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.linkclink.gfr.R;

public class WifiListFragment extends Fragment {

    private static final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1;
    private View view;

    private Button btRefresh;
    private ListView lwWifiList;

    private LayoutInflater inflater;
    private ViewGroup container;

    private WifiManager wifiManager;
    private WifiReceiver wifiReceiver;


    public static WifiListFragment newInstance() {
        return new WifiListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.inflater = inflater;
        this.container = container;

        wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        /* Check wifi enabled */
        CheckWifiEnabled();
        /* Components Initialisation */
        InitialisationComponents();

        btRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Refresh();
            }
        });

        /* First Scan */
        wifiManager.startScan();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(wifiReceiver);
    }

    public void CheckPermissions() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        wifiReceiver = new WifiReceiver(wifiManager, lwWifiList);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        getActivity().registerReceiver(wifiReceiver, intentFilter);
        //getWifi();
    }

    private void CheckWifiEnabled() {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        if (!wifiManager.isWifiEnabled())
            ShowToast.showToast(getContext(), "Permissions not granted!!!");
    }


    private void InitialisationComponents() {
        view = inflater.inflate(R.layout.wifi_list_fragment, container, false);
        btRefresh = (Button) view.findViewById(R.id.button_refresh);
        lwWifiList = (ListView) view.findViewById(R.id.listview_wifi_list);
    }

    private void Refresh() {
        lwWifiList.setAdapter(null);
        wifiManager.startScan();

        ShowToast.showToast(getContext(), "Refresh");
    }
}
