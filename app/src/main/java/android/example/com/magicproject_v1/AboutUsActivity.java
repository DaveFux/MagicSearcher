package android.example.com.magicproject_v1;

import android.content.Context;
import android.content.Intent;
import android.example.com.magicproject_v1.classes.Card;
import android.example.com.magicproject_v1.utils.CardDB;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

public class AboutUsActivity extends AppCompatActivity {
    protected DrawerLayout mDrawerLayout;
    protected CardDB mDb;
    protected Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        mContext = this;
        mDb = new CardDB(mContext);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(
                menuItem -> {

                    menuItem.setChecked(true);
                    mDrawerLayout.closeDrawers();
                    switch (menuItem.getItemId()) {
                        case R.id.searchCards:
                            Intent intent = new Intent(AboutUsActivity.this, CollectionActivity.class);
                            Bundle b = new Bundle();
                            b.putBoolean("allCards", true);
                            intent.putExtras(b);
                            startActivity(intent);
                            break;
                        case R.id.randomCard:
                            System.out.println("OOF");
                            Intent randomCardIntent = new Intent(AboutUsActivity.this, CardViewActivity.class);
                            Bundle randomCardBundle = new Bundle();
                            Card c = mDb.retrieveCard();
                            randomCardBundle.putString("image", c.getImage());
                            randomCardIntent.putExtras(randomCardBundle);
                            startActivity(randomCardIntent);
                            break;
                        case R.id.addCollection:
                            startActivity(new Intent(AboutUsActivity.this, NewCollectionActivity.class));
                            break;
                        case R.id.settings:
                            startActivity(new Intent(AboutUsActivity.this, SettingsActivity.class));
                            break;
                        case R.id.aboutUs:
                            startActivity(new Intent(AboutUsActivity.this, AboutUsActivity.class));
                            break;
                        case R.id.howToUse:
                            startActivity(new Intent(AboutUsActivity.this, HowToUseActivity.class));
                            break;
                    }
                    return true;
                });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}