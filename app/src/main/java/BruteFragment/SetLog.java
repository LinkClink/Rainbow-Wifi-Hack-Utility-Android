package BruteFragment;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;

class SetLog {

    private Bundle bundle;
    private FragmentManager fragmentManager;

    SetLog(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    /* Set current logcat results */
    void SetLogResult(String text) {
        bundle = new Bundle();
        bundle.putString("log", text);
        fragmentManager.setFragmentResult("log", bundle);
    }

    /* Set good brute log results */
    void SetLogGoodResults(String text) {
        bundle = new Bundle();
        bundle.putString("logGod", text);
        fragmentManager.setFragmentResult("logGod", bundle);
    }

    /* Set current wifi brute */
    void SetLogCurrentWifi(String text) {
        bundle = new Bundle();
        bundle.putString("currentBruteWifi", text);
        fragmentManager.setFragmentResult("currentBruteWifi", bundle);
    }

    void SetLogBruteProgress(String text) {
        bundle = new Bundle();
        bundle.putString("progressLog", text);
        fragmentManager.setFragmentResult("progressLog", bundle);
    }
}
