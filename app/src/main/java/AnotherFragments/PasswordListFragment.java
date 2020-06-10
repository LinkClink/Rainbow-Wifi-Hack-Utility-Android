package AnotherFragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.linkclink.gfr.R;

import BruteFragment.BruteFragment;
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

    private Bundle bundle = new Bundle();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.container = container;
        this.inflater = inflater;
        InitialisationComponentsPlus();

        /* Buttons click realisation */
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetFilePath();
            }
        });

        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResetPasswordList();
            }
        });

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if (uri != null) {
                            SetPasswordList(uri.getLastPathSegment());
                            passUri = uri.toString();
                            bundle = new Bundle();
                            bundle.putString("passwordUri", passUri);
                            getParentFragmentManager().setFragmentResult("passwordUri", bundle);
                            ShowToast.showToast(getContext(), "Success added file " + uri.getLastPathSegment());
                        } else
                            ShowToast.showToast(getContext(), "Error: please add password file ");
                    }
                });

        return view;
    }

    /* Components layout initialisation */
    private void InitialisationComponentsPlus() {
        view = inflater.inflate(R.layout.password_list_fragment, container, false);
        btAdd = view.findViewById(R.id.button_add_password);
        btReset = view.findViewById(R.id.button_reset_password);
        textViewPasswordList = view.findViewById(R.id.textView_passwords_list);
    }

    /* Get file path */
    private void GetFilePath() {
        String fileType = "text/plain";
        mGetContent.launch(fileType);
    }

    /* Add password file name to textView */
    private void SetPasswordList(String text) {
        textViewPasswordList.append(text + "\n");
    }

    /* Reset password list and set textView visible */
    private void ResetPasswordList() {
        BruteFragment bruteFragment = new BruteFragment();
        if (bruteFragment.getFlagCurrentBrute() == 0) {
            getParentFragmentManager().setFragmentResult("resetPasswordList", bundle);
            textViewPasswordList.setText("");
        } else ShowToast.showToast(requireContext(), "Error: don't reset when brute start");
    }
}
