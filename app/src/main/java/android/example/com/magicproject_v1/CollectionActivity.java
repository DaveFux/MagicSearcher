package android.example.com.magicproject_v1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.example.com.magicproject_v1.classes.Card;
import android.example.com.magicproject_v1.classes.Collection;
import android.example.com.magicproject_v1.utils.CardDB;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class CollectionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    protected Context mContext;
    protected ArrayList<Card> allCards = new ArrayList<>();
    protected ArrayList<Card> cardListArray = new ArrayList<>();
    protected CardsArrayAdapter itemsAdapter;
    protected ListView cardListView;
    protected CardDB mDb;
    protected Bundle bundle;
    protected AlphaAnimation inAnimation;
    protected AlphaAnimation outAnimation;
    protected FrameLayout progressBarHolder;
    protected DrawerLayout mDrawerLayout;
    protected ArrayList<Collection> collections;
    protected ArrayList<String> collectionsName = new ArrayList<>();
    protected Spinner spinner;
    protected int selectedCollection;

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

    @Override
    protected void onStart() {
        super.onStart();
    }

    protected void init() {
        mContext = this;
        mDb = new CardDB(mContext);
        collections = mDb.retrieveAllCollections();
        for (Collection item : collections) {
            collectionsName.add(item.getName());
        }
        cardListView = findViewById(R.id.cardList);
        progressBarHolder = findViewById(R.id.progressBarHolder);
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
                            Intent intent = new Intent(CollectionActivity.this, CollectionActivity.class);
                            Bundle b = new Bundle();
                            b.putBoolean("allCards", true);
                            intent.putExtras(b);
                            startActivity(intent);
                            break;
                        case R.id.addCollection:
                            startActivity(new Intent(CollectionActivity.this, NewCollectionActivity.class));
                            break;
                        case R.id.settings:
                            startActivity(new Intent(CollectionActivity.this, SettingsActivity.class));
                            break;
                        case R.id.aboutUs:
                            startActivity(new Intent(CollectionActivity.this, AboutUsActivity.class));
                            break;
                    }
                    return true;
                });
        bundle = getIntent().getExtras();
        String collectionName = bundle.getString("collectionName");
        setTitle(collectionName != null ? collectionName : "All cards");
        new LoadCardsFromCollections().execute();

        cardListView.setOnItemClickListener(seeCard);
        registerForContextMenu(cardListView);
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
                cardListArray.clear();
                if (str.contains(":")) {
                    String[] strAfterSplit = str.split(":");
                    if (strAfterSplit.length > 1) {
                        switch (strAfterSplit[0].toLowerCase()) {
                            case "mc":
                                for (Card card : allCards) {
                                    if (card.getManaCost().toString().toLowerCase().contains(strAfterSplit[1])) {
                                        cardListArray.add(card);
                                    }
                                }
                                break;
                            case "type":
                                for (Card card : allCards) {
                                    if (card.getType().toLowerCase().contains(strAfterSplit[1])) {
                                        cardListArray.add(card);
                                    }
                                }
                                break;
                            case "exp":
                                for (Card card : allCards) {
                                    if (card.getExpansionName().toLowerCase().contains(strAfterSplit[1])) {
                                        cardListArray.add(card);
                                    }
                                }
                                break;
                            case "power":
                                if (!Pattern.matches("[a-zA-Z]+", strAfterSplit[1])) {
                                    switch (strAfterSplit[1].charAt(0)) {
                                        case '>':
                                            if (strAfterSplit[1].length() > 1) {
                                                for (Card card : allCards) {
                                                    if (card.getPower() > Integer.parseInt(strAfterSplit[1].substring(1))) {
                                                        cardListArray.add(card);
                                                    }
                                                }
                                            }
                                            break;
                                        case '<':
                                            if (strAfterSplit[1].length() > 1) {
                                                for (Card card : allCards) {
                                                    if (card.getPower() < Integer.parseInt(strAfterSplit[1].substring(1))) {
                                                        cardListArray.add(card);
                                                    }
                                                }
                                            }
                                            break;
                                        default:
                                            for (Card card : allCards) {
                                                if (card.getPower() == Integer.parseInt(strAfterSplit[1].substring(1))) {
                                                    cardListArray.add(card);
                                                }
                                            }
                                            break;
                                    }
                                }
                                break;
                            case "toughness":
                                if (!Pattern.matches("[a-zA-Z]+", strAfterSplit[1])) {
                                    switch (strAfterSplit[1].charAt(0)) {
                                        case '>':
                                            if (strAfterSplit[1].length() > 1) {
                                                for (Card card : allCards) {
                                                    if (card.getToughness() > Integer.parseInt(strAfterSplit[1].substring(1))) {
                                                        cardListArray.add(card);
                                                    }
                                                }
                                            }
                                            break;
                                        case '<':
                                            if (strAfterSplit[1].length() > 1) {
                                                for (Card card : allCards) {
                                                    if (card.getToughness() < Integer.parseInt(strAfterSplit[1].substring(1))) {
                                                        cardListArray.add(card);
                                                    }
                                                }
                                            }
                                            break;
                                        default:
                                            for (Card card : allCards) {
                                                if (card.getToughness() == Integer.parseInt(strAfterSplit[1].substring(1))) {
                                                    cardListArray.add(card);
                                                }
                                            }
                                            break;
                                    }
                                }
                                break;
                        }
                    }
                } else {
                    for (Card card : allCards) {
                        if (card.getName().toLowerCase().contains(str)) {
                            cardListArray.add(card);
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

        switch (item.getItemId()) {
            case R.id.addToCollection:
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                String cardId = cardListArray.get(info.position).getId();
                builder.setTitle(cardListArray.get(info.position).getName());
                View viewInflated = LayoutInflater.from(mContext).inflate(R.layout.input_dialog, cardListView, false);
                System.out.println("Ganda BAnana" + selectedCollection);
                mDb.addCardInCollection(cardId, selectedCollection);
                final EditText input = viewInflated.findViewById(R.id.input);
                spinner = viewInflated.findViewById(R.id.collectionSpinner);
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                        (this, android.R.layout.simple_spinner_item,
                                collectionsName); //selected item will look like a spinner set from XML
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                        .simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerArrayAdapter);
                builder.setView(viewInflated);

                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        int mNumberOfCards = Integer.parseInt(input.getText().toString());
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                cardListArray.addAll(mDb.retrieveAllCardsInCollection(selectedCollection));
                itemsAdapter.notifyDataSetChanged();
                return true;

            case R.id.deleteFromCollection:
                cardListArray.remove((int) info.id);
                itemsAdapter.notifyDataSetChanged();
                Toast.makeText(mContext, "Item deleted from collection", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedCollection = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
            if (bundle != null) {
                boolean showAllCards = bundle.getBoolean("allCards");
                int collectionId = bundle.getInt("collectionId");
                if (showAllCards) {
                    cardListArray.addAll(mDb.retrieveCards());
                } else {
                    cardListArray.addAll(mDb.retrieveAllCardsInCollection(collectionId));
                }
                allCards.addAll(cardListArray);
                itemsAdapter = new CardsArrayAdapter(mContext, cardListArray);
            }
            return null;
        }
    }
}
