package android.example.com.magicproject_v1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        finish();
    }
}