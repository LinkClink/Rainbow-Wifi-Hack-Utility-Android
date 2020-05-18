package AnotherFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

    public static StopCancelFragment newInstance() {
        return new StopCancelFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.container = container;
        this.inflater = inflater;
        InitiationComponents();

        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Stop();
            }
        });

        return view;
    }

    private void InitiationComponents() {
        view = inflater.inflate(R.layout.stop_cancel_fragment, container, false);
        btStop = (Button) view.findViewById(R.id.button_cancel);
        btCancel = (Button) view.findViewById(R.id.button_stop);
    }

    private void Stop() {
        bundle = new Bundle();
        bundle.putString("log", "Stopping current process....");
        getParentFragmentManager().setFragmentResult("log", bundle);
        SetFlag();

    }

    private void SetFlag() {
        bundle = new Bundle();
        bundle.putString("stopCurrentProcess", "");
        getParentFragmentManager().setFragmentResult("stopCurrentProcess", bundle);
    }
}
