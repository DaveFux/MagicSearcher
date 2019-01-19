package android.example.com.magicproject_v1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.example.com.magicproject_v1.classes.Card;
import android.example.com.magicproject_v1.classes.Collection;
import android.example.com.magicproject_v1.utils.CardDB;
import android.example.com.magicproject_v1.utils.JSONParser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    protected Context mContext;
    protected ArrayList<Collection> allCollections = new ArrayList<>();
    protected ArrayList<Collection> collectionListArray = new ArrayList<>();
    protected CollectionsArrayAdapter itemsAdapter;
    protected ListView collectionListView;
    protected CardDB mDb;
    protected DrawerLayout mDrawerLayout;
    protected AlphaAnimation inAnimation;
    protected AlphaAnimation outAnimation;
    protected FrameLayout progressBarHolder;

    protected ListView.OnItemClickListener seeCollection = (parent, view, position, id) -> {
        Intent intent = new Intent(MainActivity.this, CollectionActivity.class);
        Bundle b = new Bundle();
        b.putString("collectionName", collectionListArray.get(position).getName());
        b.putInt("collectionId", position + 1);
        intent.putExtras(b);
        startActivity(intent);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        collectionListArray.clear();
        collectionListArray.addAll(mDb.retrieveAllCollections());
        itemsAdapter.notifyDataSetChanged();
    }

    protected void init() {
        mContext = this;
        mDb = new CardDB(mContext);

        progressBarHolder = findViewById(R.id.progressBarHolder);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        if (preferences.getBoolean("first_run", true)) {
            new parseInformation().execute();
        }

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    menuItem.setChecked(true);
                    mDrawerLayout.closeDrawers();
                    switch (menuItem.getItemId()) {
                        case R.id.searchCards:
                            Intent intent = new Intent(MainActivity.this, CollectionActivity.class);
                            Bundle b = new Bundle();
                            b.putBoolean("allCards", true);
                            intent.putExtras(b);
                            startActivity(intent);
                            break;
                        case R.id.randomCard:
                            System.out.println("OOF");
                            Intent randomCardIntent = new Intent(MainActivity.this, CardViewActivity.class);
                            Bundle randomCardBundle = new Bundle();
                            Card c = mDb.retrieveCard();
                            randomCardBundle.putString("image", c.getImage());
                            randomCardIntent.putExtras(randomCardBundle);
                            startActivity(randomCardIntent);
                            break;
                        case R.id.addCollection:
                            startActivity(new Intent(MainActivity.this, NewCollectionActivity.class));
                            break;
                        case R.id.settings:
                            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                            break;
                        case R.id.aboutUs:
                            startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                            break;
                        case R.id.howToUse:
                            startActivity(new Intent(MainActivity.this, HowToUseActivity.class));
                            break;
                    }
                    return true;
                });

        Bundle newCollectionBundle = getIntent().getExtras();
        if (newCollectionBundle != null) {
            if (newCollectionBundle.getBoolean("add")) {   //add
                String bName = newCollectionBundle.getString("name");
                String bTags = newCollectionBundle.getString("tags");

                if (bName != null && bTags != null) {
                    Collection c = new Collection(bName, bTags);
                    mDb.addCollection(c);
                }
            } else {
                int bCollectionId = newCollectionBundle.getInt("collectionId");
                String bName = newCollectionBundle.getString("name");
                String bTags = newCollectionBundle.getString("tags");

                if (bName != null && bTags != null) {
                    mDb.editCollection(bCollectionId, bName, bTags);
                }
            }

        }

        collectionListArray.addAll(mDb.retrieveAllCollections());
        allCollections.addAll(collectionListArray);
        collectionListView = findViewById(R.id.collectionList);
        itemsAdapter = new CollectionsArrayAdapter(mContext, collectionListArray);
        collectionListView.setAdapter(itemsAdapter);
        collectionListView.setOnItemClickListener(seeCollection);
        registerForContextMenu(collectionListView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = this.getMenuInflater();
        mi.inflate(R.menu.menu_search, menu);
        mi.inflate(R.menu.menu_main_activity, menu);
        MenuItem item = menu.findItem(R.id.cardSearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String str = newText.toLowerCase();
                if (str.contains(":")) {
                    collectionListArray.clear();
                    String[] strAfterSplit = str.split(":");
                    if (strAfterSplit.length > 1) {
                        switch (strAfterSplit[0].toLowerCase()) {
                            case "tags":
                                for (Collection collection : allCollections) {
                                    if (collection.getTags().toLowerCase().contains(strAfterSplit[1])) {
                                        collectionListArray.add(collection);
                                    }
                                }
                                break;
                        }
                    }
                } else {
                    for (Collection collection : allCollections) {
                        if (collection.getName().toLowerCase().contains(str)) {
                            collectionListArray.add(collection);
                        }
                    }
                }
                itemsAdapter.notifyDataSetChanged();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.deleteAllCollections:
                mDb.deleteAllCollections();
                collectionListArray.clear();
                itemsAdapter.notifyDataSetChanged();
                break;
            case R.id.addCollection:
                startActivity(new Intent(MainActivity.this, NewCollectionActivity.class));
                break;
            case R.id.settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            case R.id.aboutUs:
                startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu_collections, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.deleteCollection:
                collectionListArray.remove(info.position);
                itemsAdapter.notifyDataSetChanged();
                Toast.makeText(mContext, "Item deleted", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.editCollection:
                Intent intent = new Intent(MainActivity.this, NewCollectionActivity.class);
                Bundle b = new Bundle();
                b.putInt("collectionId", info.position + 1);
                b.putString("collectionName", collectionListArray.get(info.position).getName());
                b.putString("collectionTags", collectionListArray.get(info.position).getTags());
                intent.putExtras(b);
                startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private class parseInformation extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder = findViewById(R.id.progressBarHolder);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                InputStream json = mContext.getAssets().open("cards.json");
                int size = json.available();
                JSONParser jp = new JSONParser();
                if (size > 0) {
                    List<Card> cards = new ArrayList<>(jp.readJsonStream(json));
                    List<String> tags = new ArrayList<>();
                    tags.add("Aggro");
                    tags.add("Budget");
                    mDb.addCollection(new Collection("SUPA COLLECTION 1", tags, cards));
                    for (Card card : cards) {
                        mDb.addCard(card);
                    }
                } else {
                    //log
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
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("first_run", false);
            editor.apply();
        }

    }
}
