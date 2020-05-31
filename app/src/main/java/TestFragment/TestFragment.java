package TestFragment;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.linkclink.gfr.R;

import BruteFragment.BruteFragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import logic.SaveSharedPreferences;
import logic.ShowToast;

public class TestFragment extends Fragment {

    private View view;

    private LayoutInflater inflater;
    private ViewGroup container;

    private Button buttonTest;
    private Button buttonStop;

    private EditText editTextPassword;
    private EditText editTextCount;

    private BruteFragment bruteFragment;
    private SaveSharedPreferences saveSharedPreferences;

    private String currentBruteWifiSSID = null;

    private WifiManager wifiManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.container = container;
        this.inflater = inflater;
        InitialisationComponentsPlus();
        wifiManager = (WifiManager) requireContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        /* Set saved password with SharedPreferences */
        editTextPassword.setText(saveSharedPreferences.GetPassword());

        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestInitialisation();
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestProcess.setStopBrute();
            }
        });

        getParentFragmentManager().setFragmentResultListener("selectedWifiTest", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                currentBruteWifiSSID = result.getString("selectedWifi");
            }
        });

        return view;
    }

    /* Components layout initialisation and objects */
    private void InitialisationComponentsPlus() {
        view = inflater.inflate(R.layout.test_thread_fragment, container, false);
        buttonTest = (Button) view.findViewById(R.id.button_test);
        buttonStop = (Button) view.findViewById(R.id.button_test_stop);
        editTextPassword = (EditText) view.findViewById(R.id.editText_password0);
        editTextCount = (EditText) view.findViewById(R.id.editText_test_count);
        bruteFragment = new BruteFragment();
        saveSharedPreferences = new SaveSharedPreferences(requireContext());
    }

    /* Main test init */
    private void TestInitialisation() {
        /* Check password - is empty */
        if (editTextPassword.getText().length() >= 8)
            /* Check wifi is not selected */
            if (currentBruteWifiSSID != null)
                /* Check brute already start */
                if (bruteFragment.getFlagCurrentBrute() == 0) {

                    /* Save password to SharedPreferences */
                    saveSharedPreferences.SaveYouTestPassword(editTextPassword.getText().toString());

                    /* Select test try connections */
                    int countTest;
                    if (!editTextCount.getText().toString().equals(""))
                        countTest = Integer.parseInt(editTextCount.getText().toString());
                    else countTest = 3;

                    /* Set flag to ban brute */
                    bruteFragment.setFlagCurrentBrute((byte) 1);

                    TestProcess testProcess = new TestProcess(wifiManager, currentBruteWifiSSID, getParentFragmentManager(),
                            requireActivity(), editTextPassword.getText().toString(), countTest);

                    testProcess.execute();

                } else ShowToast.showToast(requireContext(), "Don`t start test when brute start");
            else ShowToast.showToast(requireContext(), "Please select your wifi to test");
        else ShowToast.showToast(requireContext(), "Error password length");
    }
}
