package android.example.com.magicproject_v1;

import android.content.Context;
import android.content.Intent;
import android.example.com.magicproject_v1.utils.CardDB;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    protected Context mContext;
    protected ArrayList<String> collectionListArray = new ArrayList<>();
    protected ArrayAdapter<String> itemsAdapter;
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
            ArrayList<String> result = new ArrayList<>();
            for (String collectionName : collectionListArray) {
                if(collectionName.contains(s)){
                    result.add(collectionName);
                }
            }
            ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, result);
            collectionListView.setAdapter(itemsAdapter);
        }
    };

    //protected EditText.On
    protected ListView.OnItemClickListener seeCollection = (parent, view, position, id) -> {
        Intent intent = new Intent(MainActivity.this, CollectionActivity.class);
        Bundle b = new Bundle();
        b.putString("collectionName", parent.getItemAtPosition(position).toString());
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

        mDb.addCollection("SUPA COLLECTION 1");
        mDb.addCollection("SUPA COLLECTION 2");
        mDb.addCollection("SUPA COLLECTION 3");
        mDb.addCollection("SUPA COLLECTION 4");

        ArrayList<String> results = mDb.retrieveAllCollections();

        collectionListArray.addAll(results);
        //searchBar = findViewById(R.id.cardSearch);
        //searchBar.addTextChangedListener(searchWatcher);
        collectionListView = findViewById(R.id.collectionList);
        itemsAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, collectionListArray); // pls no mexer
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
                for (String card : collectionListArray) {
                    if (card.toLowerCase().contains(newText)) {
                        result.add(card);
                    }
                }
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
