package android.example.com.magicproject_v1;

import android.content.Context;
import android.content.SharedPreferences;
import android.example.com.magicproject_v1.utils.CardDB;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    private Context mContext;
    private CardDB cardDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();
    }

    private void init() {
        mContext = this;
        setTitle("Options");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        Switch thumbnailSwitch = findViewById(R.id.switchThumbnail);
        thumbnailSwitch.setChecked(preferences.getBoolean("thumbnails", true));
        thumbnailSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("thumbnails", isChecked);
            editor.apply();
        });

        Switch manaCostSwitch = findViewById(R.id.switchManaCost);
        manaCostSwitch.setChecked(preferences.getBoolean("manaCost", true));
        manaCostSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("manaCost", isChecked);
            editor.apply();
        });
    }
}
