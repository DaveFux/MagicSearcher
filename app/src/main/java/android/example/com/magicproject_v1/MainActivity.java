package android.example.com.magicproject_v1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.example.com.magicproject_v1.classes.Card;
import android.example.com.magicproject_v1.classes.Collection;
import android.example.com.magicproject_v1.utils.BuildDatabase;
import android.example.com.magicproject_v1.utils.CardDB;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.support.design.widget.Snackbar.make;

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
    protected FrameLayout mProgressBarHolder;
    protected Toolbar mToolbar;
    protected NavigationView mNavigationView;
    protected CoordinatorLayout mCoordinatorLayout;

    private boolean mSortByNumberOfCards = false;
    private boolean mSortByName = false;

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
        boolean bDataMembersInitialized = initDataMembers();
        this.setTitle("Collections");

        if (bDataMembersInitialized) {
            setSupportActionBar(mToolbar);
            ActionBar actionbar = getSupportActionBar();
            if(actionbar != null){
                actionbar.setDisplayHomeAsUpEnabled(true);
                actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
            }

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            if (preferences.getBoolean("first_run", true)) {
                InputStream json = null;
                try {
                    json = mContext.getAssets().open("cards.json");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                new BuildDatabase(MainActivity.this).execute(json);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("first_run", false);
                editor.apply();
            }

            mNavigationView.setCheckedItem(0);
            mNavigationView.setNavigationItemSelectedListener(menuItem -> {
                mDrawerLayout.closeDrawers();
                Bundle bundle = new Bundle();
                switch (menuItem.getItemId()) {
                    case R.id.idMenuSearchCards:
                        Intent searchCardsIntent = new Intent(MainActivity.this, CollectionActivity.class);
                        bundle.putBoolean("allCards", true);
                        bundle.putInt("menuItemSelected", menuItem.getItemId());
                        searchCardsIntent.putExtras(bundle);
                        startActivity(searchCardsIntent);
                        break;
                    case R.id.idMenuRandomCard:
                        Card c = mDb.retrieveCard();
                        Intent randomCardIntent = new Intent(MainActivity.this, CardViewActivity.class);
                        bundle.putString("image", c.getImage());
                        bundle.putInt("menuItemSelected", menuItem.getItemId());
                        randomCardIntent.putExtras(bundle);
                        startActivity(randomCardIntent);
                        break;
                    case R.id.idMenuAddCollection:
                        startActivity(new Intent(MainActivity.this, NewCollectionActivity.class));
                        break;
                    case R.id.idMenuSettings:
                        Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                        bundle.putInt("menuItemSelected", menuItem.getItemId());
                        settingsIntent.putExtras(bundle);
                        startActivity(settingsIntent);
                        break;
                    case R.id.idMenuAboutUs:
                        Intent aboutUsIntent = new Intent(MainActivity.this, AboutUsActivity.class);
                        bundle.putInt("menuItemSelected", menuItem.getItemId());
                        aboutUsIntent.putExtras(bundle);
                        startActivity(aboutUsIntent);
                        break;
                    case R.id.idMenuHowToUse:
                        Intent howToUseIntent = new Intent(MainActivity.this, HowToUseActivity.class);
                        bundle.putInt("menuItemSelected", menuItem.getItemId());
                        howToUseIntent.putExtras(bundle);
                        startActivity(howToUseIntent);
                        break;
                }
                return true;
            });

            collectionListView.setOnItemClickListener((parent, view, position, id) -> {
                Intent intent = new Intent(MainActivity.this, CollectionActivity.class);
                Bundle b = new Bundle();
                b.putString("collectionName", collectionListArray.get(position).getName());
                System.out.println(collectionListArray.get(position).getId());
                b.putInt("collectionId", collectionListArray.get(position).getId());
                System.out.println( collectionListArray.get(position).getId());
                intent.putExtras(b);
                startActivity(intent);
            });

            Bundle newCollectionBundle = getIntent().getExtras();
            if (newCollectionBundle != null) {
                if (newCollectionBundle.getBoolean("add")) {   //add
                    String bName = newCollectionBundle.getString("name");
                    String bTags = newCollectionBundle.getString("tags");

                    if (bName != null && bTags != null) {
                        Collection c = new Collection(bName, bTags);
                        mDb.addCollection(c);
                        collectionListArray.clear();
                        collectionListArray.addAll(mDb.retrieveAllCollections());
                        itemsAdapter.notifyDataSetChanged();
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
            collectionListView.setAdapter(itemsAdapter);
            registerForContextMenu(collectionListView);
        }
    }

    private boolean initDataMembers() {
        mContext = this;
        mDb = new CardDB(mContext);
        mProgressBarHolder = findViewById(R.id.idProgressBarHolder);
        mDrawerLayout = findViewById(R.id.idDrawerLayout);
        mToolbar = findViewById(R.id.idToolbar);
        collectionListView = findViewById(R.id.idCollectionList);
        mNavigationView = findViewById(R.id.idNavigationView);
        mCoordinatorLayout = findViewById(R.id.idCoordinatorLayout);
        itemsAdapter = new CollectionsArrayAdapter(mContext, collectionListArray);

        Object[] objects = {mContext, mDb, mProgressBarHolder, mDrawerLayout,
                mToolbar, collectionListView, mNavigationView, mCoordinatorLayout, itemsAdapter};

        for (Object o : objects) {
            if (o == null) return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = this.getMenuInflater();
        mi.inflate(R.menu.menu_search, menu);
        mi.inflate(R.menu.menu_collections, menu);
        MenuItem item = menu.findItem(R.id.idMenuItemCardSearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                collectionListArray.clear();
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
            case R.id.idMenuSortName:
                collectionListArray.sort((o1, o2) -> {
                    if(mSortByName){
                        return o2.getName().compareTo(o1.getName());
                    } else {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                mSortByName = !mSortByName;
                itemsAdapter.notifyDataSetChanged();
                break;
            case R.id.idMenuSortNumberOfCards:
                collectionListArray.sort((o1, o2) -> {
                    if(mSortByNumberOfCards){
                        return Integer.compare(o1.getNumberOfCards(), o2.getNumberOfCards());
                    } else {
                        return Integer.compare(o2.getNumberOfCards(), o1.getNumberOfCards());
                    }
                });
                mSortByNumberOfCards = !mSortByNumberOfCards;
                itemsAdapter.notifyDataSetChanged();
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
        int collectionId = collectionListArray.get(info.position).getId();
        switch (item.getItemId()) {
            case R.id.idContextItemDeleteCollection:
                String collectionName = collectionListArray.get(info.position).getName();
                collectionListArray.remove(info.position);
                mDb.deleteCollection(collectionId);
                itemsAdapter.notifyDataSetChanged();
                Snackbar itemDeletedSnackbar = make(mCoordinatorLayout,
                        "Deleted collection " + collectionName,
                        Snackbar.LENGTH_LONG);
                itemDeletedSnackbar.show();
                return true;
            case R.id.idContextItemDeleteAllCollection:
                collectionListArray.clear();
                mDb.deleteAllCollections();
                itemsAdapter.notifyDataSetChanged();
                Snackbar deletedAllSnackbar = make(mCoordinatorLayout,
                        "Deleted all collections",
                        Snackbar.LENGTH_LONG);
                deletedAllSnackbar.show();
                return true;
            case R.id.idContextItemEditCollection:
                Intent intent = new Intent(MainActivity.this, NewCollectionActivity.class);
                Bundle b = new Bundle();
                b.putInt("collectionId", collectionId);
                b.putString("collectionName", collectionListArray.get(info.position).getName());
                b.putString("collectionTags", collectionListArray.get(info.position).getTags());
                intent.putExtras(b);
                startActivity(intent);
                return true;
            case R.id.idContextItemClearCollection:
                mDb.deleteAllCardsFromCollection(collectionId);
                collectionListArray.clear();
                collectionListArray.addAll(mDb.retrieveAllCollections());
                itemsAdapter.notifyDataSetChanged();
                Snackbar clearedSnackbar = make(mCoordinatorLayout,
                        "Cleared all items in collection " + collectionListArray.get(info.position).getName(),
                        Snackbar.LENGTH_LONG);
                clearedSnackbar.show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
