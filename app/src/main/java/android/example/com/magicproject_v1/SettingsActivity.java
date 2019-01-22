package android.example.com.magicproject_v1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.example.com.magicproject_v1.classes.Card;
import android.example.com.magicproject_v1.utils.BuildDatabase;
import android.example.com.magicproject_v1.utils.CardDB;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

import java.io.IOException;
import java.io.InputStream;

public class SettingsActivity extends AppCompatActivity {

    protected CoordinatorLayout mCoordinatorLayout;
    protected DrawerLayout mDrawerLayout;
    protected CardDB mDb;
    protected Context mContext;
    protected Toolbar mToolbar;
    protected NavigationView mNavigationView;

    protected Switch mThumbnailSwitch;
    protected Switch mManaCostSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();
    }

    private void init() {
        boolean bDataMembersInitialized = initDataMembers();

        if (bDataMembersInitialized) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);

            mThumbnailSwitch.setChecked(preferences.getBoolean("thumbnails", true));
            mThumbnailSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("thumbnails", isChecked);
                editor.apply();
            });

            mManaCostSwitch.setChecked(preferences.getBoolean("manaCost", true));
            mManaCostSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("manaCost", isChecked);
                editor.apply();
            });

            setSupportActionBar(mToolbar);
            ActionBar actionbar = getSupportActionBar();
            if(actionbar != null) {
                actionbar.setDisplayHomeAsUpEnabled(true);
                actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
            }

            Bundle receivedBundle = getIntent().getExtras();
            if(receivedBundle != null) {
                mNavigationView.setCheckedItem(receivedBundle.getInt("menuItemSelected"));
            }
            mNavigationView.setNavigationItemSelectedListener(menuItem -> {
                mDrawerLayout.closeDrawers();
                Bundle bundle = new Bundle();
                switch (menuItem.getItemId()) {
                    case R.id.idMenuCollections:
                        Intent collectionsIntent = new Intent(SettingsActivity.this, MainActivity.class);
                        bundle.putInt("menuItemSelected", menuItem.getItemId());
                        collectionsIntent.putExtras(bundle);
                        startActivity(collectionsIntent);
                        break;
                    case R.id.idMenuSearchCards:
                        Intent searchCardsIntent = new Intent(SettingsActivity.this, CollectionActivity.class);
                        bundle.putBoolean("allCards", true);
                        bundle.putInt("menuItemSelected", menuItem.getItemId());
                        searchCardsIntent.putExtras(bundle);
                        startActivity(searchCardsIntent);
                        break;
                    case R.id.idMenuRandomCard:
                        Card c = mDb.retrieveCard();
                        Intent randomCardIntent = new Intent(SettingsActivity.this, CardViewActivity.class);
                        bundle.putString("image", c.getImage());
                        bundle.putInt("menuItemSelected", menuItem.getItemId());
                        randomCardIntent.putExtras(bundle);
                        startActivity(randomCardIntent);
                        break;
                    case R.id.idMenuAddCollection:
                        startActivity(new Intent(SettingsActivity.this, NewCollectionActivity.class));
                        break;
                    case R.id.idMenuAboutUs:
                        Intent aboutUsIntent = new Intent(SettingsActivity.this, AboutUsActivity.class);
                        bundle.putInt("menuItemSelected", menuItem.getItemId());
                        aboutUsIntent.putExtras(bundle);
                        startActivity(aboutUsIntent);
                        break;
                    case R.id.idMenuHowToUse:
                        Intent howToUseIntent = new Intent(SettingsActivity.this, HowToUseActivity.class);
                        bundle.putInt("menuItemSelected", menuItem.getItemId());
                        howToUseIntent.putExtras(bundle);
                        startActivity(howToUseIntent);
                        break;
                }
                return true;
            });
        }
    }

    private boolean initDataMembers() {
        mContext = this;
        mDb = new CardDB(mContext);
        mDrawerLayout = findViewById(R.id.idDrawerLayout);
        mToolbar = findViewById(R.id.idToolbar);
        mNavigationView = findViewById(R.id.idNavigationView);
        mCoordinatorLayout = findViewById(R.id.idCoordinatorLayout);
        mThumbnailSwitch = findViewById(R.id.idSwitchThumbnails);
        mManaCostSwitch = findViewById(R.id.idSwitchManaCost);

        Object[] objects = {mContext, mDb, mDrawerLayout, mToolbar,  mNavigationView,
                mCoordinatorLayout};

        for (Object o : objects) {
            if (o == null) return false;
        }
        return true;
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

    public void reinstalldb(View view){
        mDb.clear();
        InputStream json = null;
        try {
            json = mContext.getAssets().open("cards.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        new BuildDatabase(SettingsActivity.this).execute(json);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("first_run", false);
        editor.apply();
    }
}
