package WifiTools;

import android.net.wifi.WifiManager;

import java.lang.reflect.Method;

class CheckHotSpot {

    /* Check HotSpot enabled */
    static boolean CheckHotSpotEnabled(WifiManager wifiManager) {
        try {
            Method method = wifiManager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifiManager);
        } catch (Throwable ignored) {
        }
        return false;
    }
}
