package android.example.com.magicproject_v1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewCollectionActivity extends AppCompatActivity {

    protected Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_collection);
        init();
    }

    protected void init(){
        bundle = getIntent().getExtras();
        if (bundle!=null) {
            EditText editTextName = findViewById(R.id.editTextName);
            editTextName.setText(bundle.getString("collectionName"));
            EditText editTextTags = findViewById(R.id.editTextTags);
            editTextTags.setText(bundle.getString("collectionTags"));
            ((Button)findViewById(R.id.button)).setText(R.string.button_edit);
        } else ((Button)findViewById(R.id.button)).setText(R.string.button_create);
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
        if (bundle!=null) {
            b.putBoolean("add", false);
            b.putInt("collectionId", bundle.getInt("collectionId"));
        } else b.putBoolean("add", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }
}
