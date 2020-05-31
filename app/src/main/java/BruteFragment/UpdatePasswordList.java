package BruteFragment;

import android.content.Context;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

class UpdatePasswordList {

    private List<String> passwordList;

    private Context context;

    /* Realization */
    UpdatePasswordList(List<String> passwordList, Context context) {
        this.passwordList = passwordList;
        this.context = context;
    }

    /* Open file and append to passwords list */
    int UpdateList(String fileUri) throws IOException {
        int fileSize = 0;
        InputStream inputStream = context.getContentResolver().openInputStream(Uri.parse(fileUri));
        assert inputStream != null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            passwordList.add(line);
            fileSize++;
        }
        inputStream.close();
        return fileSize;
    }
}
