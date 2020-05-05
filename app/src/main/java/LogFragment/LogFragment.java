package LogFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linkclink.gfr.R;

public class LogFragment extends Fragment {


    public static LogFragment newInstance() {
        return new LogFragment();
    }

    private TextView textViewLogCat;

    private LayoutInflater inflater;
    private ViewGroup container;

    private View view;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        this.inflater = inflater;
        this.container = container;

        InitialisationComponents();

        /* test */
        Set("Work");


        return view;
    }

    private void InitialisationComponents()
    {
        view  = inflater.inflate(R.layout.log_fragment, container, false);
        textViewLogCat = (TextView) view.findViewById(R.id.textView_logCat);
    }



    public  void Set(String text)
    {
        textViewLogCat.setText(text);
    }






}
