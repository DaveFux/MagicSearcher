package android.example.com.magicproject_v1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        init();
    }

    protected void init(){
        BottomNavigationView bNavView = findViewById(R.id.bottom_navigation);
        bNavView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.searchCards:
                    Intent intent = new Intent(AboutUsActivity.this, CollectionActivity.class);
                    Bundle b = new Bundle();
                    b.putBoolean("allCards", true);
                    intent.putExtras(b);
                    startActivity(intent);
                    break;
                case R.id.collectionList:
                    startActivity(new Intent(AboutUsActivity.this, CollectionActivity.class));
                    break;
                case R.id.randomCard:
                    //startActivity(new Intent(AboutUsActivity.this, AboutUsActivity.class));
                    break;
            }
            return true;
        });
    }
}
