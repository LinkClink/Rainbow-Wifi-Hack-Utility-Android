package WifiTools;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import logic.ShowToast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.linkclink.gfr.R;

import java.util.ArrayList;

public class WifiListFragment extends Fragment {

    private View view;

    private Button btRefresh;
    private ListView lwWifiList;

    private ProgressBar progressBarWifiList;

    private LayoutInflater inflater;
    private ViewGroup container;

    private WifiManager wifiManager;
    private WifiReceiver wifiReceiver;

    private String currentWifiSelected;


    public static ArrayList<String> getWifiListNames() {
        return wifiListNames;
    }

    public static void setWifiListNames(ArrayList<String> wifiListNames) {
        WifiListFragment.wifiListNames = wifiListNames;
    }

    private static ArrayList<String> wifiListNames;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.inflater = inflater;
        this.container = container;
        InitialisationComponents();
        wifiManager = (WifiManager) requireContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        /* Check HotSpot enabled */
        if (CheckHotSpot.CheckHotSpotEnabled(wifiManager))
            ShowToast.showToast(getContext(), "Please disable HotSpot:");
        /* First Scan */
        wifiManager.startScan();

        btRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckPermission())
                    if (CheckHotSpot.CheckHotSpotEnabled(wifiManager))
                        ShowToast.showToast(getContext(), "Please disable HotSpot:");
                    else Refresh();
            }
        });

        lwWifiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentWifiSelected = wifiListNames.get(i);
                SetCurrentWifiBrute(currentWifiSelected);
            }
        });

        return view;
    }

    /* Components layout initialisation */
    private void InitialisationComponents() {
        view = inflater.inflate(R.layout.wifi_list_fragment1, container, false);
        btRefresh = view.findViewById(R.id.button_refresh);
        lwWifiList = view.findViewById(R.id.listview_wifi_list);
        progressBarWifiList = view.findViewById(R.id.progressBar);
    }

    /* Unregister wifi-receiver if app state pause */
    @Override
    public void onPause() {
        super.onPause();
        requireActivity().unregisterReceiver(wifiReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        wifiReceiver = new WifiReceiver(wifiManager, lwWifiList,progressBarWifiList);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        requireActivity().registerReceiver(wifiReceiver, intentFilter);
    }

    /* Refresh wifi list */
    private void Refresh() {
        lwWifiList.setAdapter(null);
        progressBarWifiList.setVisibility(View.VISIBLE);
        wifiManager.startScan();
        ShowToast.showToast(getContext(), "Refresh:");
    }

    /* Set actually wifi brute */
    private void SetCurrentWifiBrute(String wifi) {
        ShowToast.showToast(getContext(), "Set current wifi:" + wifi);
        Bundle bundle = new Bundle();
        bundle.putString("selectedWifi", wifi);
        getParentFragmentManager().setFragmentResult("selectedWifi", bundle);
        getParentFragmentManager().setFragmentResult("selectedWifiTest", bundle);
    }

    /* Check permissions to visible wifi list */
    private boolean CheckPermission() {
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{permission}, 100);
        } else return true;
        return false;
    }
}
