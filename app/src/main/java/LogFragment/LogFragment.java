package LogFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.linkclink.gfr.R;

public class LogFragment extends Fragment {

    public static LogFragment newInstance() {
        return new LogFragment();
    }

    private TextView textViewLogCat;
    private TextView textViewGodResults;
    private TextView textViewCurrentWifi;

    private Button btReset;

    private LayoutInflater inflater;
    private ViewGroup container;

    private View view;

    private String allLogResults;
    private String allGodLogResults;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.inflater = inflater;
        this.container = container;
        InitialisationComponents();

        /* Set log results to TextView */
        getParentFragmentManager().setFragmentResultListener("log", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                allLogResults += result.getString("log") + "\n"; /* Not usage */
                SetLog(result.getString("log"));
            }
        });
        getParentFragmentManager().setFragmentResultListener("logGod", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                allGodLogResults += result.getString("logGod") + "\n"; /* Not usage */
                SetLogGod(result.getString("logGod"));
            }
        });
        getParentFragmentManager().setFragmentResultListener("currentBruteWifi", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                SetCurrentWifi(result.getString("currentBruteWifi"));
            }
        });

        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResetLog();
            }
        });

        return view;
    }

    private void InitialisationComponents() {
        view = inflater.inflate(R.layout.log_fragment, container, false);
        textViewLogCat = (TextView) view.findViewById(R.id.textView_logCat);
        textViewGodResults = (TextView) view.findViewById(R.id.textView_GodResults);
        textViewCurrentWifi = (TextView) view.findViewById(R.id.textView_currentWifi);
        btReset = (Button) view.findViewById(R.id.button_reset_log);
    }

    private void ResetLog() {
        textViewLogCat.setText("");
    }

    private void SetLog(String logText) {
        textViewLogCat.append(logText + "\n");
    }

    private void SetLogGod(String godLogText) {
        textViewGodResults.append(godLogText + "\n");
    }

    private void SetCurrentWifi(String currentWifi) {
        textViewCurrentWifi.setText(currentWifi);
    }
}
