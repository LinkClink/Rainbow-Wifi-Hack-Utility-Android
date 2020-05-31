package WifiTools;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.linkclink.gfr.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import logic.ShowToast;

public class WifiOnOffFragment extends Fragment {

    private LayoutInflater inflater;
    private ViewGroup container;

    private Button button_on;
    private Button button_off;

    private View view;

    private WifiManager wifiManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.container = container;
        this.inflater = inflater;
        InitialisationComponents();
        wifiManager = (WifiManager) requireContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        /* Buttons click realisation */
        button_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckHotSpot.CheckHotSpotEnabled(wifiManager))
                    ShowToast.showToast(getContext(), "Please disable HotSpot:");
                WifiOn();
            }
        });
        button_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckHotSpot.CheckHotSpotEnabled(wifiManager))
                    ShowToast.showToast(getContext(), "Please disable HotSpot:");
                WifiOff();
            }
        });

        return view;
    }

    /* Components layout initialisation */
    private void InitialisationComponents() {
        view = inflater.inflate(R.layout.wifi_on_of_fragment, container, false);
        button_off = view.findViewById(R.id.button_wifi_off);
        button_on = view.findViewById(R.id.button_wifi_on);
    }

    /* Check and enable Wifi */
    private void WifiOn() {
        CheckHotSpot.CheckHotSpotEnabled(wifiManager);
        if (!wifiManager.isWifiEnabled())
            wifiManager.setWifiEnabled(true);
        else ShowToast.showToast(getContext(), "Already wifi enabled");
    }

    /* Check and disable Wifi */
    private void WifiOff() {
        CheckHotSpot.CheckHotSpotEnabled(wifiManager);
        if (wifiManager.isWifiEnabled())
            wifiManager.setWifiEnabled(false);
        else ShowToast.showToast(getContext(), "Already wifi disabled");
    }
}
