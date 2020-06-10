package logic;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;

public class SetLog {

    private Bundle bundle = new Bundle();
    private FragmentManager fragmentManager;

    public SetLog(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    /* Set current logcat results */
    public void SetLogResult(String text) {
        bundle.putString("log", text);
        fragmentManager.setFragmentResult("log", bundle);
    }

    /* Set good brute log results */
    public void SetLogGoodResults(String text) {
        bundle.putString("logGod", text);
        fragmentManager.setFragmentResult("logGod", bundle);
    }

    /* Set current wifi brute */
    public void SetLogCurrentWifi(String text) {
        bundle.putString("currentBruteWifi", text);
        fragmentManager.setFragmentResult("currentBruteWifi", bundle);
    }

    /* Set-show actual brute-test progress */
    public void SetLogCurrentProgress(String text) {
        bundle.putString("progressLog", text);
        fragmentManager.setFragmentResult("progressLog", bundle);
    }
}
