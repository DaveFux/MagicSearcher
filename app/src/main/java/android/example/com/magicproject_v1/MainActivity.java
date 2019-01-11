package android.example.com.magicproject_v1;

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
    protected EditText searchBar;
    protected CardDB mDb;
    protected TextWatcher searchWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // ? Aqui nao acontece nada, por enquanto

        }

        @Override
        public void afterTextChanged(Editable s) {
            // ? Aqui nao acontece nada, por enquanto
            //Exemplos para esta funcao: Fazer um historico de procuras
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            ArrayList<Collection> result = new ArrayList<>();
            for (Collection collection : collectionListArray) {
                if(collection.getName().contains(s)){
                    result.add(collection);
                }
            }
            CollectionsArrayAdapter itemsAdapter = new CollectionsArrayAdapter(mContext, result);
            collectionListView.setAdapter(itemsAdapter);
        }
    };

    //protected EditText.On
    protected ListView.OnItemClickListener seeCollection = (parent, view, position, id) -> {
        Intent intent = new Intent(MainActivity.this, CollectionActivity.class);
        Bundle b = new Bundle();
        b.putString("collectionName", collectionListArray.get(position).getName());
        b.putInt("collectionId", position + 1);
        intent.putExtras(b);
        startActivity(intent);
    };

    protected ListView.OnItemLongClickListener editCollection = new ListView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            /*collectionListArray.remove(position);
            ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, collectionListArray);
            collectionListView.setAdapter(itemsAdapter);*/
            return false;
        }
    };

    //protected Menu pMenu;
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
            int size = json.available();
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

        collectionListArray.addAll(results);
        //searchBar = findViewById(R.id.cardSearch);
        //searchBar.addTextChangedListener(searchWatcher);
        collectionListView = findViewById(R.id.collectionList);
        itemsAdapter = new CollectionsArrayAdapter(mContext, collectionListArray); // pls no mexer
        collectionListView.setAdapter(itemsAdapter);
        collectionListView.setOnItemLongClickListener(editCollection);
        collectionListView.setOnItemClickListener(seeCollection);

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
                    startActivity(new Intent(MainActivity.this, CollectionActivity.class));
                    break;
                case R.id.aboutUs:
                    Toast.makeText(MainActivity.this, "Action About us Clicked", Toast.LENGTH_SHORT).show();
            break;
        }
        return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = this.getMenuInflater();
        mi.inflate(R.menu.menu_search, menu);
        mi.inflate(R.menu.menu_1, menu);
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
                //itemsAdapter.getFilter().filter(newText);
                List<String> result = new ArrayList<>();
                /*for (String card : collectionListArray) {
                    if (card.toLowerCase().contains(newText)) {
                        result.add(card);
                    }
                }*/
                ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, result);
                collectionListView.setAdapter(itemsAdapter);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addCards:
                break;
            case R.id.help:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
