package BruteFragment;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import logic.SaveSharedPreferences;
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
    private Button buttonBruteAll;
    private SeekBar seekBarThread;
    private TextView textViewThreadValue;

    private WifiManager wifiManager;

    private static byte flagCurrentBrute = 0;
    private int threadValue = 100;

    private String currentBruteWifiSSID = null;

    private List<String> passwordList = new ArrayList<String>();
    private List<String> passwordUriList = new ArrayList<String>(); /* Not usage */

    private SetLog setLog;
    private CheckWifi checkWifi;
    private UpdatePasswordList updatePasswordList;
    private BruteProcess bruteProcess;
    private SaveSharedPreferences saveSharedPreferences;

    public static BruteFragment newInstance() {
        return new BruteFragment();
    }

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

        getParentFragmentManager().setFragmentResultListener("selectedWifi", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                currentBruteWifiSSID = result.getString("selectedWifi");
            }
        });

        getParentFragmentManager().setFragmentResultListener("passwordUri", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                passwordUriList.add(result.getString("passwordUri")); /* Not usage */
                try {
                    setLog.SetLogResult("Update/List Success added " + updatePasswordList.UpdateList(result.getString("passwordUri")) + " passwords");
                } catch (IOException e) {
                    ShowToast.showToast(getContext(), "Error " + e.getMessage());
                }
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
        buttonBrute = (Button) view.findViewById(R.id.button_brute);
        buttonBruteAll = (Button) view.findViewById(R.id.button_bruteAll);
        seekBarThread = (SeekBar) view.findViewById(R.id.seekBar_thread);
        textViewThreadValue = (TextView) view.findViewById(R.id.textView_threadValue);

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

        bruteProcess = new BruteProcess(passwordList, wifiManager, currentBruteWifiSSID,
                getParentFragmentManager(), requireActivity(), threadValue);

        bruteProcess.execute();
    }

    public byte getFlagCurrentBrute() {
        return flagCurrentBrute;
    }

    void setFlagCurrentBrute() {
        BruteFragment.flagCurrentBrute = (byte) 0;
    }

    private void setSeekBarValue() {
        seekBarThread.setProgress(saveSharedPreferences.GetThreadValue());
        textViewThreadValue.setText(String.valueOf(saveSharedPreferences.GetThreadValue()));
        threadValue = saveSharedPreferences.GetThreadValue();
    }

}
