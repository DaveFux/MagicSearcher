package android.example.com.magicproject_v1;

import android.content.Context;
import android.content.Intent;
import android.example.com.magicproject_v1.classes.Card;
import android.example.com.magicproject_v1.classes.Collection;
import android.example.com.magicproject_v1.utils.CardDB;
import android.example.com.magicproject_v1.utils.JSONParser;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class CollectionActivity extends AppCompatActivity {

    protected Context mContext;
    protected ArrayList<Card> cardListArray = new ArrayList<>();
    protected CardsArrayAdapter itemsAdapter;
    protected ListView cardListView;
    protected CardDB mDb;

    //protected EditText.On
    protected ListView.OnItemClickListener seeCard = (parent, view, position, id) -> {
        Intent intent = new Intent(CollectionActivity.this, CardViewActivity.class);
        Bundle b = new Bundle();
        b.putString("image", cardListArray.get(position).getImage());
        intent.putExtras(b);
        startActivity(intent);
    };

    protected ListView.OnItemLongClickListener editCard = (parent, view, position, id) -> {
        /*cardListArray.remove(position);
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, cardListArray);
        cardListView.setAdapter(itemsAdapter);*/
        return false;
    };

    //protected Menu pMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        init();
    }

    protected void init() {
        mContext = this;
        mDb = new CardDB(mContext);

        cardListView = findViewById(R.id.cardList);

        Bundle b = getIntent().getExtras();
        List<Card> results = new ArrayList<>();
        //List<String> results = new ArrayList<>();

        if(b != null) {
            boolean showAllCards = b.getBoolean("allCards");
            String collectionName = b.getString("collectionName");
            int collectionId = b.getInt("collectionId");
            if(showAllCards){
                try {
                    InputStream json = mContext.getAssets().open("cards.json");
                    int size = json.available();
                    JSONParser jp = new JSONParser();
                    results.addAll(jp.readJsonStream(json));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //results.addAll(mDb.retrieveCards(collectionName));
            results.addAll(mDb.retrieveAllCardsInCollection(collectionId));
        }

        //ArrayList<String> results = mDb.retrieveAll();

        cardListArray.addAll(results);
        itemsAdapter = new CardsArrayAdapter(mContext, cardListArray); // pls no mexer
        cardListView.setAdapter(itemsAdapter);
        cardListView.setOnItemLongClickListener(editCard);
        cardListView.setOnItemClickListener(seeCard);

        BottomNavigationView bNavView = findViewById(R.id.bottom_navigation);
        bNavView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.searchCards:
                    Intent intent = new Intent(CollectionActivity.this, CollectionActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("allCards", true);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case R.id.collectionList:
                    startActivity(new Intent(CollectionActivity.this, MainActivity.class));
                    break;
                case R.id.randomCard:
                    startActivity(new Intent(CollectionActivity.this, AboutUsActivity.class));
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
                List<Card> filteredArray = new ArrayList<>();
                for (Card card : cardListArray) {
                    if (card.getName().toLowerCase().contains(str)) {
                        filteredArray.add(card);
                    }
                }
                CardsArrayAdapter filteredAdapter = new CardsArrayAdapter(mContext, filteredArray);
                cardListView.setAdapter(filteredAdapter);
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
            case R.id.aboutUs:
                startActivity(new Intent(CollectionActivity.this, AboutUsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
