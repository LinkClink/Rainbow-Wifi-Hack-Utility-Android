package logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreferences {

    private SharedPreferences sharedPreferences;
    private Context context;

    public SaveSharedPreferences(Context context) {
        this.context = context;
    }

    /* Save thread-value to SharedPreferences for another time open */
    public void SaveThreadValue(int value) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("threadValue", value);
        editor.apply();
        editor.commit();
    }

    /* Get saved thread-value */
    public int GetThreadValue() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt("threadValue", 0);
    }

    /* Save your test password for test-optimal thread */
    public void SaveYouTestPassword(String password) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("password", password);
        editor.apply();
        editor.commit();
    }

    /* Get saved password */
    public String GetPassword() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString("password", "");
    }
}
