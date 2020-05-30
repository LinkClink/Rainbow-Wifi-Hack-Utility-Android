package TestFragment;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import BruteFragment.BruteFragment;
import androidx.fragment.app.FragmentManager;

public class TestProcess extends AsyncTask {

    private List<String> passwordList = new ArrayList<String>();

    private int listLength;
    private int listPlace;

    private int passwordLength;

    private int maxThread = 3100;
    private int currentThread;
    private int optimalThread;
    private int netId;
    private int testCount;

    private byte flagBrute = 0;
    private static byte flagStop = 0;
    private byte flagTryConnection;

    private WifiManager wifiManager;
    private WifiConfiguration wifiConfiguration;

    private FragmentManager fragmentManager;

    private String currentBruteWifiSSID;
    private String passwordMy;

    private StringBuilder passwordGenerate = new StringBuilder();

    private Activity activity;

    private SetLog setLog;

    private Random generator = new Random();

    TestProcess(WifiManager wifiManager, String currentBruteWifiSSID,
                FragmentManager fragmentManager, Activity activity, String password, int testCount) {

        this.currentBruteWifiSSID = currentBruteWifiSSID;
        this.passwordMy = password;
        this.fragmentManager = fragmentManager;
        this.wifiManager = wifiManager;
        this.activity = activity;
        this.testCount = testCount;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        TestOptimalThread();
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        setLog.SetLogResult("Finish test optimal thread:" + optimalThread);
        setLog.SetTestThreadResult(optimalThread);

        BruteFragment bruteFragment = new BruteFragment();
        bruteFragment.setFlagCurrentBrute((byte) 0);
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);

        setLog.SetLogBruteProgress("Test progress " + values[0].toString() + " with < " + maxThread + " thread " + "try connect " + testCount);
    }

    private void TestOptimalThread() {
        setLog = new SetLog(fragmentManager);
        passwordList.clear();
        GeneratedPasswordList();
        TestBrute();
    }

    private void TestBrute() {

        currentThread = 50;
        flagStop = 0;

        while (currentThread < maxThread & flagBrute == 0) {

            flagTryConnection = 0;

            for (int a = 0; a < 3; a++) {

                for (int i = 0; i < passwordList.size(); i++) {

                    if (flagBrute == 0) {

                        flagBrute = 1;
                        wifiManager.removeNetwork(netId);

                        /* Config */
                        WifiGeneratedConfig(i);
                        /* Try connect */
                        TryConnect();

                        /* Thread */
                        try {
                            Thread.sleep(currentThread);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        publishProgress(currentThread);

                        /* Success brute */
                        if (CheckSuccessConnect()) {
                            optimalThread = currentThread;
                            flagTryConnection += 1;
                            break;
                        }
                    }
                }
            }
            if (flagTryConnection == 3 | flagStop == 1)
                break;


            currentThread += 50;
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


    /* Password length 8-12, List length 10-20 */
    private void GeneratedPasswordList() {

        listLength = generator.nextInt(10) + 10;
        listPlace = (generator.nextInt(10) + 10) / 2;

        for (int i = 0; i < listLength; i++) {

            if (i == listPlace)
                passwordList.add(passwordMy);
            else PasswordGenerate();
        }
    }

    private void PasswordGenerate() {
        passwordGenerate.setLength(0);
        passwordLength = generator.nextInt(4) + 8;

        for (int i = 0; i < passwordLength; i++) {
            passwordGenerate.append(generator.nextInt(9));
        }
        passwordList.add(String.valueOf(passwordGenerate));
    }

    private boolean CheckSuccessConnect() {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = Objects.requireNonNull(connectivityManager).getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        flagBrute = 0;
        return Objects.requireNonNull(networkInfo).isConnected();
    }

    static void setStopBrute() {
        TestProcess.flagStop = (byte) 1;
    }
}
