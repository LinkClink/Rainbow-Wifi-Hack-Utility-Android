package WifiListFragment;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import logic.ShowToast;
import logic.WifiReceiver;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.linkclink.gfr.R;

import java.lang.reflect.Method;

public class WifiListFragment extends Fragment {

    private View view;

    private Button btRefresh;
    private ListView lwWifiList;

    private LayoutInflater inflater;
    private ViewGroup container;

    private WifiManager wifiManager;
    private WifiReceiver wifiReceiver;

    private String currentWifiSelected;

    private Bundle bundle;

    public static WifiListFragment newInstance() {
        return new WifiListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.inflater = inflater;
        this.container = container;
        InitialisationComponents();
        wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        /* Check wifi enabled */
        CheckWifiEnabled();
        /* Check HotSpot enabled */
        if (CheckHotSpotEnabled())
            ShowToast.showToast(getContext(), "Please disable HotSpot:");
        /* First Scan */
        wifiManager.startScan();

        btRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckHotSpotEnabled())
                    ShowToast.showToast(getContext(), "Please disable HotSpot:");
                else Refresh();
            }
        });

        lwWifiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentWifiSelected = (String) lwWifiList.getItemAtPosition(i);
                SetCurrentWifiBrute(currentWifiSelected);
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().unregisterReceiver(wifiReceiver); /* getActivity().unregisterReceiver(wifiReceiver); */
    }

    @Override
    public void onResume() {
        super.onResume();
        wifiReceiver = new WifiReceiver(wifiManager, lwWifiList);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        getActivity().registerReceiver(wifiReceiver, intentFilter);
    }

    /* Do not use */
    private void CheckWifiEnabled() {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
    }

    private void InitialisationComponents() {
        view = inflater.inflate(R.layout.wifi_list_fragment, container, false);
        btRefresh = (Button) view.findViewById(R.id.button_refresh);
        lwWifiList = (ListView) view.findViewById(R.id.listview_wifi_list);
    }

    /* Refresh wifi list */
    private void Refresh() {
        lwWifiList.setAdapter(null);
        wifiManager.startScan();
        ShowToast.showToast(getContext(), "Refresh:");
    }

    /* Set actually wifi brute */
    private void SetCurrentWifiBrute(String wifi) {
        ShowToast.showToast(getContext(), "Set current wifi:" + wifi);
        bundle = new Bundle();
        bundle.putString("selectedWifi", wifi);
        getParentFragmentManager().setFragmentResult("selectedWifi", bundle);
    }

    /* Check HotSpot enabled */
    private boolean CheckHotSpotEnabled() {
        try {
            Method method = wifiManager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifiManager);
        } catch (Throwable ignored) {
        }
        return false;
    }
}
