package BruteFragment;

import android.net.wifi.WifiManager;

import java.lang.reflect.Method;

class CheckWifi extends BruteFragment {

    private WifiManager wifiManager;

    CheckWifi(WifiManager wifiManager) {
        this.wifiManager = wifiManager;
    }

    /* Check HotSpot enabled */
    boolean CheckHotSpotEnabled() {
        try {
            Method method = wifiManager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifiManager);
        } catch (Throwable ignored) {
        }
        return false;
    }

    /* Check wifi enabled */
    boolean CheckWifiEnabled() {
        return !wifiManager.isWifiEnabled();
    }

    /* Check nullable wif */
    boolean CheckCurrentWifi(String currentBruteWifiSSID) {
        return currentBruteWifiSSID != null;
    }
}
