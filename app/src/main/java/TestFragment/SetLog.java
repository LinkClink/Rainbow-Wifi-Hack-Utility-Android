package TestFragment;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;

class SetLog {

    private Bundle bundle;
    private FragmentManager fragmentManager;

    SetLog(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    void SetLogBruteProgress(String text) {
        bundle = new Bundle();
        bundle.putString("progressLog", text);
        fragmentManager.setFragmentResult("progressLog", bundle);
    }

    /* Set current logcat results */
    void SetLogResult(String text) {
        bundle = new Bundle();
        bundle.putString("log", text);
        fragmentManager.setFragmentResult("log", bundle);
    }

    void SetTestThreadResult(int thread) {
        bundle = new Bundle();
        bundle.putInt("setThreadTestResult", thread);
        fragmentManager.setFragmentResult("setThreadTestResult", bundle);

    }
}
