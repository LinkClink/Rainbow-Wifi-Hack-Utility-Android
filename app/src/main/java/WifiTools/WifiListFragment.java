package WifiTools;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import logic.ShowToast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.linkclink.gfr.R;

public class WifiListFragment extends Fragment {

    private View view;

    private Button btRefresh;
    private ListView lwWifiList;

    private LayoutInflater inflater;
    private ViewGroup container;

    private WifiManager wifiManager;
    private WifiReceiver wifiReceiver;

    private String currentWifiSelected;

    public static WifiListFragment newInstance() {
        return new WifiListFragment();
    }

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
                if (CheckHotSpot.CheckHotSpotEnabled(wifiManager))
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

    /* Components layout initialisation */
    private void InitialisationComponents() {
        view = inflater.inflate(R.layout.wifi_list_fragment, container, false);
        btRefresh = (Button) view.findViewById(R.id.button_refresh);
        lwWifiList = (ListView) view.findViewById(R.id.listview_wifi_list);
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
        requireActivity().registerReceiver(wifiReceiver, intentFilter);
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
        Bundle bundle = new Bundle();
        bundle.putString("selectedWifi", wifi);
        getParentFragmentManager().setFragmentResult("selectedWifi", bundle);
    }
}
