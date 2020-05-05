package logic;

import android.content.Context;
import android.widget.Toast;

public class ShowToast {

    /* Show toast in activity context (errors and logs) first version */
    public static void showToast(Context mContext, String status) {
        Toast.makeText(mContext, status, Toast.LENGTH_SHORT).show();
    }
}
