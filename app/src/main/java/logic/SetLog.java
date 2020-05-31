package logic;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;

public class SetLog {

    private Bundle bundle;
    private FragmentManager fragmentManager;

    public SetLog(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    /* Set current logcat results */
    public void SetLogResult(String text) {
        bundle = new Bundle();
        bundle.putString("log", text);
        fragmentManager.setFragmentResult("log", bundle);
    }

    /* Set good brute log results */
    public void SetLogGoodResults(String text) {
        bundle = new Bundle();
        bundle.putString("logGod", text);
        fragmentManager.setFragmentResult("logGod", bundle);
    }

    /* Set current wifi brute */
    public void SetLogCurrentWifi(String text) {
        bundle = new Bundle();
        bundle.putString("currentBruteWifi", text);
        fragmentManager.setFragmentResult("currentBruteWifi", bundle);
    }

    /* Set-show actual brute-test progress */
    public void SetLogBruteProgress(String text) {
        bundle = new Bundle();
        bundle.putString("progressLog", text);
        fragmentManager.setFragmentResult("progressLog", bundle);
    }
}
