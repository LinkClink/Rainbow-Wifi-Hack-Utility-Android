package BruteFragment;

import android.content.Context;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

class UpdatePasswordList {

    private InputStream inputStream;
    private BufferedReader bufferedReader;

    private String line;
    private String encode = "UTF-8";

    private int fileSize;

    private List<String> passwordList = new ArrayList<String>();

    private Context context;

    UpdatePasswordList(List<String> passwordList, Context context) {
        this.passwordList = passwordList;
        this.context = context;
    }

    /* Open file and append to passwords list */
    int UpdateList(String fileUri) throws IOException {
        fileSize = 0;
        inputStream = context.getContentResolver().openInputStream(Uri.parse(fileUri));
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = bufferedReader.readLine()) != null) {
            passwordList.add(line);
            fileSize++;
        }
        inputStream.close();
        return fileSize;
    }
}
