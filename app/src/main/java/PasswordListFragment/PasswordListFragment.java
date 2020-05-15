package PasswordListFragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.linkclink.gfr.R;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import logic.ShowToast;

public class PasswordListFragment extends Fragment {

    private View view;

    private LayoutInflater inflater;
    private ViewGroup container;

    private Button btAdd;
    private Button btReset;

    private TextView textViewPasswordList;

    private ActivityResultLauncher<String> mGetContent;

    private String passUri;
    private String fileType = "text/plain";

    private Bundle bundle;

    public static PasswordListFragment newInstance() {
        return new PasswordListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.container = container;
        this.inflater = inflater;
        InitialisationComponents();

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetTxtPath();
            }
        });

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        SetPasswordList(uri.getLastPathSegment());
                        passUri = uri.toString();
                        bundle= new Bundle();
                        bundle.putString("passwordUri",passUri);
                        getParentFragmentManager().setFragmentResult("passwordUri",bundle);
                        ShowToast.showToast(getContext(),passUri);
                    }
                });

        return view;
    }

    private void InitialisationComponents() {
        view = inflater.inflate(R.layout.password_list_fragment, container, false);
        btAdd = (Button) view.findViewById(R.id.button_add_password);
        btReset = (Button) view.findViewById(R.id.button_reset_password);
        textViewPasswordList = (TextView) view.findViewById(R.id.textView_passwords_list);
    }

    private void GetTxtPath() {
        mGetContent.launch(fileType);
    }

    private void SetPasswordList(String text) {
        textViewPasswordList.append(text + "\n");
    }
}
