package BruteFragment;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import logic.SaveSharedPreferences;
import logic.SetLog;
import logic.ShowToast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.linkclink.gfr.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BruteFragment extends Fragment {

    private LayoutInflater inflater;
    private ViewGroup container;

    private View view;

    private Button buttonBrute;
    private Button buttonStop;
    private SeekBar seekBarThread;
    private TextView textViewThreadValue;

    private WifiManager wifiManager;

    private static byte flagCurrentBrute = 0;
    private int threadValue = 100;

    private String currentBruteWifiSSID = null;

    private List<String> passwordList = new ArrayList<String>();

    private SetLog setLog;
    private CheckWifi checkWifi;
    private UpdatePasswordList updatePasswordList;
    private SaveSharedPreferences saveSharedPreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.container = container;
        this.inflater = inflater;
        wifiManager = (WifiManager) requireContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        InitialisationComponentsPlus();

        setSeekBarValue();

        buttonBrute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BruteMainCheck();
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StopBrute();
            }
        });

        seekBarThread.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textViewThreadValue.setText(String.valueOf(i + 100));
                threadValue = i + 100;
                saveSharedPreferences.SaveThreadValue(threadValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        getParentFragmentManager().setFragmentResultListener("setThreadTestResult", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                seekBarThread.setProgress(result.getInt("setThreadTestResult") - 100);
            }
        });

        getParentFragmentManager().setFragmentResultListener("selectedWifi", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                currentBruteWifiSSID = result.getString("selectedWifi");
            }
        });

        getParentFragmentManager().setFragmentResultListener("passwordUri", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                int size = 0;
                Bundle bundle = new Bundle();
                try {
                    size = updatePasswordList.UpdateList(result.getString("passwordUri"));
                    setLog.SetLogResult("Update/List Success added " + passwordList.size() + " passwords and delete " + (size - passwordList.size()));
                } catch (IOException e) {
                    ShowToast.showToast(getContext(), "Error " + e.getMessage());
                }
                bundle.putInt("updatePasswordListSize", passwordList.size());
                getParentFragmentManager().setFragmentResult("updatePasswordListSize", bundle);
            }
        });

        getParentFragmentManager().setFragmentResultListener("resetPasswordList", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                passwordList.clear();
                ShowToast.showToast(getContext(), "Success reset password list");
            }
        });

        getParentFragmentManager().setFragmentResultListener("stopBrute", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                flagCurrentBrute = 0;
                BruteProcess.setStopBrute();
            }
        });

        return view;
    }

    /* Components layout initialisation and objects */
    private void InitialisationComponentsPlus() {
        view = inflater.inflate(R.layout.brute_fragment, container, false);
        buttonBrute = view.findViewById(R.id.button_brute);
        buttonStop = view.findViewById(R.id.button_brute_stop);
        seekBarThread = view.findViewById(R.id.seekBar_thread);
        textViewThreadValue = view.findViewById(R.id.textView_threadValue);
        setLog = new SetLog(getParentFragmentManager());
        checkWifi = new CheckWifi(wifiManager);
        updatePasswordList = new UpdatePasswordList(passwordList, requireContext());
        saveSharedPreferences = new SaveSharedPreferences(requireContext());
    }

    /* Check all ready to start brute */
    private void BruteMainCheck() {
        /* Check HotSpot enabled and disable */
        if (!checkWifi.CheckHotSpotEnabled()) {
            /* Check wifi enabled */
            if (!checkWifi.CheckWifiEnabled()) {
                /* Check empty wif and brute */
                if (!passwordList.isEmpty()) {
                    /* Check empty wifi and brute */
                    if (checkWifi.CheckCurrentWifi(currentBruteWifiSSID)) {
                        /* Check brute already start */
                        if (flagCurrentBrute == 0) {
                            InitialisationAsyncTask();
                        } else ShowToast.showToast(getContext(), "Already brute start:");
                    } else ShowToast.showToast(getContext(), "Please selected wifi to brute:");

                } else ShowToast.showToast(getContext(), "Please add passwords list:");

            } else ShowToast.showToast(getContext(), "Please enable Wifi:");

        } else ShowToast.showToast(getContext(), "Please disable HotSpot:");
    }

    /* Start brute with asyncTask */
    private void InitialisationAsyncTask() {
        flagCurrentBrute = 1;
        setLog.SetLogResult("Brute/Start brute wifi: " + currentBruteWifiSSID + " thread: " + threadValue);
        setLog.SetLogCurrentWifi(currentBruteWifiSSID);

        Bundle bundle = new Bundle();
        bundle.putInt("setProgressBarMax", passwordList.size());
        getParentFragmentManager().setFragmentResult("setProgressBarMax", bundle);

        BruteProcess bruteProcess = new BruteProcess(passwordList, wifiManager, currentBruteWifiSSID,
                getParentFragmentManager(), requireActivity(), threadValue);

        bruteProcess.execute();
    }

    public byte getFlagCurrentBrute() {
        return flagCurrentBrute;
    }

    public void setFlagCurrentBrute(byte flag) {
        BruteFragment.flagCurrentBrute = flag;
    }

    /* Set seekBar-thread value */
    private void setSeekBarValue() {
        seekBarThread.setProgress(saveSharedPreferences.GetThreadValue());
        textViewThreadValue.setText(String.valueOf(saveSharedPreferences.GetThreadValue()));
        threadValue = saveSharedPreferences.GetThreadValue();
    }

    /* Stop current brute process */
    private void StopBrute() {
        Bundle bundle = new Bundle();
        if (flagCurrentBrute == 1) {
            bundle.putString("log", "Stopping current process...");
            getParentFragmentManager().setFragmentResult("log", bundle);
            flagCurrentBrute = 0;
            BruteProcess.setStopBrute();
        } else ShowToast.showToast(getContext(), "Don't stop");
    }
}
