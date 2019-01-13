package android.example.com.magicproject_v1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.example.com.magicproject_v1.classes.Card;
import android.example.com.magicproject_v1.classes.Collection;
import android.example.com.magicproject_v1.utils.CardDB;
import android.example.com.magicproject_v1.utils.JSONParser;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /* TODO:
    *
    *   MainActivity, more like SlowActivity
    *   imensos problemas de performance para dar fix
    *   ler mensagens do android durante a execuçao
    *
    */

    protected Context mContext;
    protected ArrayList<Collection> collectionListArray = new ArrayList<>();
    protected CollectionsArrayAdapter itemsAdapter;
    protected ListView collectionListView;
    protected CardDB mDb;

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

    protected void init() {
        mContext = this;
        mDb=new CardDB(mContext);
        mDb.clear();

        List<String> tags = new ArrayList<>();
        tags.add("Aggro");
        tags.add("Budget");
        List<Card> cards = new ArrayList<>();
        try {
            InputStream json = mContext.getAssets().open("cards.json");
            int size = json.available(); // verifica se o json está disponivel
            JSONParser jp = new JSONParser();
            cards.addAll(jp.readJsonStream(json));
            for(Card card : cards){
                mDb.addCard(card);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mDb.addCollection(new Collection("SUPA COLLECTION 1", tags, cards));
        mDb.addCollection(new Collection("SUPA COLLECTION 2", tags, cards));
        mDb.addCollection(new Collection("SUPA COLLECTION 3", tags, cards));
        mDb.addCollection(new Collection("SUPA COLLECTION 4", tags, cards));

        ArrayList<Collection> results = mDb.retrieveAllCollections();

        Bundle newCollectionBundle = getIntent().getExtras();
        if(newCollectionBundle != null){
            String bName = newCollectionBundle.getString("name");
            String bTags = newCollectionBundle.getString("tags");

            if(bName != null && bTags != null){
                Collection c = new Collection(bName, bTags);
                mDb.addCollection(c);
                results.add(c);
            }
        }

        collectionListArray.addAll(results);
        collectionListView = findViewById(R.id.collectionList);
        itemsAdapter = new CollectionsArrayAdapter(mContext, collectionListArray); // pls no mexer
        collectionListView.setAdapter(itemsAdapter);
        collectionListView.setOnItemClickListener(seeCollection);

        registerForContextMenu(collectionListView);
        BottomNavigationView bNavView = findViewById(R.id.bottom_navigation);
        bNavView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.searchCards:
                    Intent intent = new Intent(MainActivity.this, CollectionActivity.class);
                    Bundle b = new Bundle();
                    b.putBoolean("allCards", true);
                    intent.putExtras(b);
                    startActivity(intent);
                    break;
                case R.id.collectionList:
                    //startActivity(new Intent(MainActivity.this, CollectionActivity.class));
                    break;
                case R.id.randomCard:
                    Card randomCard = mDb.retrieveCard();
                    Intent randomCardIntent = new Intent(MainActivity.this, CardViewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("image", randomCard.getImage());
                    randomCardIntent.putExtras(bundle);
                    startActivity(randomCardIntent);
            break;
        }
        return true;
        });
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
                List<Collection> filteredArray = new ArrayList<>();
                for (Collection collection : collectionListArray) {
                    if (collection.getName().toLowerCase().contains(str)) {
                        filteredArray.add(collection);
                    }
                }
                CollectionsArrayAdapter filteredAdapter = new CollectionsArrayAdapter(mContext, filteredArray);
                collectionListView.setAdapter(filteredAdapter);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addCollection:
                startActivity(new Intent(MainActivity.this, NewCollectionActivity.class));
                break;
            case R.id.aboutUs:
                startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                break;
            case R.id.updateAllCollections:
                collectionListArray.clear();
                collectionListArray.addAll(mDb.retrieveAllCollections());
                CollectionsArrayAdapter updateCollections = new CollectionsArrayAdapter(mContext, collectionListArray);
                collectionListView.setAdapter(updateCollections);
                break;
            case R.id.deleteAllCollections:
                mDb.deleteAllCollections();
                collectionListArray.clear();
                CollectionsArrayAdapter collectionsArrayAdapter = new CollectionsArrayAdapter(mContext, collectionListArray);
                collectionListView.setAdapter(collectionsArrayAdapter);
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
        switch (item.getItemId()){
            case R.id.deleteCollection:
                collectionListArray.remove((int) info.id);
                CollectionsArrayAdapter collectionsArrayAdapter = new CollectionsArrayAdapter(mContext, collectionListArray);
                collectionListView.setAdapter(collectionsArrayAdapter);
                Toast.makeText(mContext, "Item deleted", Toast.LENGTH_SHORT).show();
                return true;
            default: return super.onContextItemSelected(item);
        }
    }
}
