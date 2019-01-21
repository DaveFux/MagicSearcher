package android.example.com.magicproject_v1.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.example.com.magicproject_v1.classes.Card;
import android.os.AsyncTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BuildDatabase extends AsyncTask<InputStream, Void, Void> {

    private ProgressDialog dialog;
    private CardDB mDb;

    public BuildDatabase(Activity activity) {
        this.dialog = new ProgressDialog(activity);
        this.mDb = new CardDB(activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Building database, please wait..");
        dialog.show();
    }

    @Override
    protected Void doInBackground(InputStream... streams) {
        try {
            InputStream json = streams[0];
            int size = json.available();
            JSONParser jp = new JSONParser();
            if (size > 0) {
                List<Card> cards = new ArrayList<>(jp.readJsonStream(json));
                for (Card card : cards) {
                    mDb.addCard(card);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

}
