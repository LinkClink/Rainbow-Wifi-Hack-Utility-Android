package WifiTools;

import com.linkclink.gfr.R;

class SetWifiImageSignal {

    private int[] wifiSignalImages = {R.drawable.ic_signal_wifi_min_bar_black_24dp,
            R.drawable.ic_signal_wifi_medium_bar_black_24dp, R.drawable.ic_signal_wifi_full_bar_black_24dp};

    int CheckWifiSignal(int level) {
        int standardImage = R.drawable.ic_wifi_black_24dp;
        level = level * -1; // +

        if (level < 50)
            return wifiSignalImages[2];
        else if (level < 65)
            return wifiSignalImages[1];
        else if (level > 65)
            return wifiSignalImages[0];

        return standardImage;
    }
}
