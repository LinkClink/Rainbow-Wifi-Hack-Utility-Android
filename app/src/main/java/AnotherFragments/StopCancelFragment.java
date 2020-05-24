package AnotherFragments;

import android.os.Bundle;

import BruteFragment.BruteFragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import logic.ShowToast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.linkclink.gfr.R;

public class StopCancelFragment extends Fragment {

    private Bundle bundle;

    private Button btCancel;
    private Button btStop;

    private LayoutInflater inflater;
    private ViewGroup container;

    private View view;

    private BruteFragment bruteFragment;

    public static StopCancelFragment newInstance() {
        return new StopCancelFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.container = container;
        this.inflater = inflater;
        InitiationComponentsPlus();

        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Stop();
            }
        });

        return view;
    }

    private void InitiationComponentsPlus() {
        view = inflater.inflate(R.layout.stop_cancel_fragment, container, false);
        btStop = (Button) view.findViewById(R.id.button_stop);
        btCancel = (Button) view.findViewById(R.id.button_cancel);

        bruteFragment = new BruteFragment();
    }

    private void Stop() {
        if (bruteFragment.getFlagCurrentBrute() == 1) {
            bundle = new Bundle();
            bundle.putString("log", "Stopping current process...");
            getParentFragmentManager().setFragmentResult("log", bundle);
            SetFlag();
        } else ShowToast.showToast(getContext(), "Don't stop");
    }

    private void SetFlag() {
        bundle = new Bundle();
        bundle.putString("stopBrute", "");
        getParentFragmentManager().setFragmentResult("stopBrute", bundle);
    }
}
