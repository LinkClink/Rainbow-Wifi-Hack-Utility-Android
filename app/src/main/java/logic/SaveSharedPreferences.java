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

    public void SaveThreadValue(int value) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("threadValue", value);
        editor.apply();
        editor.commit();
    }

    public int GetThreadValue() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt("threadValue", 0);
    }

}
