package android.example.com.magicproject_v1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    private Context mContext;

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
    }
}
