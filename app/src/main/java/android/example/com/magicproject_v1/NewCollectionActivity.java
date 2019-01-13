package android.example.com.magicproject_v1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class NewCollectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_collection);
    }

    public void createCollection(View view) {
        EditText editTextName = findViewById(R.id.editTextName);
        String name = editTextName.getText().toString();
        EditText editTextTags = findViewById(R.id.editTextTags);
        String tags = editTextTags.getText().toString();

        Intent intent = new Intent(NewCollectionActivity.this, MainActivity.class);
        Bundle b = new Bundle();
        b.putString("name", name);
        b.putString("tags", tags);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }
}
