package android.example.com.magicproject_v1;

import android.content.Context;
import android.content.Intent;
import android.example.com.magicproject_v1.classes.Card;
import android.example.com.magicproject_v1.utils.CardDB;
import android.example.com.magicproject_v1.utils.JSONParser;
import android.os.AsyncTask;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class CollectionActivity extends AppCompatActivity {

    protected Context mContext;
    protected ArrayList<Card> cardListArray = new ArrayList<>();
    protected CardsArrayAdapter itemsAdapter;
    protected ListView cardListView;
    protected CardDB mDb;
    Bundle bundle;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    FrameLayout progressBarHolder;

    protected ListView.OnItemClickListener seeCard = (parent, view, position, id) -> {
        Intent intent = new Intent(CollectionActivity.this, CardViewActivity.class);
        Bundle b = new Bundle();
        b.putString("image", cardListArray.get(position).getImage());
        intent.putExtras(b);
        startActivity(intent);
    };

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
        progressBarHolder = findViewById(R.id.progressBarHolder);

        bundle = getIntent().getExtras();

        new LoadCardsFromCollections().execute();

        cardListView.setOnItemClickListener(seeCard);
        registerForContextMenu(cardListView);

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
        mi.inflate(R.menu.menu_collection_activity, menu);
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
            case R.id.aboutUs:
                startActivity(new Intent(CollectionActivity.this, AboutUsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu_cards, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.deleteFromCollection:
                cardListArray.remove((int) info.id);
                CardsArrayAdapter cardsArrayAdapter = new CardsArrayAdapter(mContext, cardListArray);
                cardListView.setAdapter(cardsArrayAdapter);
                Toast.makeText(mContext, "Item deleted from collection", Toast.LENGTH_SHORT).show();
                return true;
                default: return super.onContextItemSelected(item);
        }
    }

    private class LoadCardsFromCollections extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            cardListView.setAdapter(itemsAdapter);
        }

        @Override
        protected Void doInBackground(Void... params) {
            if(bundle != null) {
                boolean showAllCards = bundle.getBoolean("allCards");
                String collectionName = bundle.getString("collectionName");
                int collectionId = bundle.getInt("collectionId");
                if(showAllCards){
                    try {
                        InputStream json = mContext.getAssets().open("cards.json");
                        int size = json.available();
                        JSONParser jp = new JSONParser();
                        cardListArray.addAll(jp.readJsonStream(json));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                cardListArray.addAll(mDb.retrieveAllCardsInCollection(collectionId));
                itemsAdapter = new CardsArrayAdapter(mContext, cardListArray); // pls no mexer
            }
            return null;
        }
    }
}
