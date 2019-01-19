package android.example.com.magicproject_v1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.example.com.magicproject_v1.classes.Card;
import android.example.com.magicproject_v1.classes.Collection;
import android.example.com.magicproject_v1.utils.CardDB;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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

import java.util.ArrayList;
import java.util.regex.Pattern;

import static android.support.design.widget.Snackbar.make;

public class CollectionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    protected Context mContext;
    protected ArrayList<Card> allCards = new ArrayList<>();
    protected ArrayList<Card> cardListArray = new ArrayList<>();
    protected CardsArrayAdapter itemsAdapter;
    protected ListView mCardListView;
    protected CardDB mDb;
    protected Bundle bundle;
    protected AlphaAnimation inAnimation;
    protected AlphaAnimation outAnimation;
    protected FrameLayout mProgressBarHolder;
    protected DrawerLayout mDrawerLayout;
    protected ArrayList<Collection> collections;
    protected ArrayList<String> collectionsName = new ArrayList<>();
    protected Spinner mSpinner;
    protected Toolbar mToolbar;
    protected NavigationView mNavigationView;
    protected CoordinatorLayout mCoordinatorLayout;

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
        boolean bDataMembersInitialized = initDataMembers();

        if (bDataMembersInitialized) {
            setSupportActionBar(mToolbar);
            ActionBar actionbar = getSupportActionBar();
            if (actionbar != null) {
                actionbar.setDisplayHomeAsUpEnabled(true);
                actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
            }

            collections = mDb.retrieveAllCollections();
            for (Collection item : collections) {
                collectionsName.add(item.getName());
            }

            Bundle receivedBundle = getIntent().getExtras();
            if (receivedBundle != null) {
                mNavigationView.setCheckedItem(receivedBundle.getInt("menuItemSelected"));
                mNavigationView.setNavigationItemSelectedListener(menuItem -> {
                    mDrawerLayout.closeDrawers();
                    Bundle bundle = new Bundle();
                    switch (menuItem.getItemId()) {
                        case R.id.idMenuCollections:
                            Intent collectionsIntent = new Intent(CollectionActivity.this, MainActivity.class);
                            bundle.putInt("menuItemSelected", menuItem.getItemId());
                            collectionsIntent.putExtras(bundle);
                            startActivity(collectionsIntent);
                            break;
                        case R.id.idMenuSearchCards:
                            if (receivedBundle.getBoolean("allCards", false)) {
                                Intent searchCardsIntent = new Intent(CollectionActivity.this, CollectionActivity.class);
                                bundle.putBoolean("allCards", true);
                                bundle.putInt("menuItemSelected", menuItem.getItemId());
                                searchCardsIntent.putExtras(bundle);
                                startActivity(searchCardsIntent);
                            }
                            break;
                        case R.id.idMenuRandomCard:
                            Card c = mDb.retrieveCard();
                            Intent randomCardIntent = new Intent(CollectionActivity.this, CardViewActivity.class);
                            bundle.putString("image", c.getImage());
                            bundle.putInt("menuItemSelected", menuItem.getItemId());
                            randomCardIntent.putExtras(bundle);
                            startActivity(randomCardIntent);
                            break;
                        case R.id.idMenuAddCollection:
                            startActivity(new Intent(CollectionActivity.this, NewCollectionActivity.class));
                            break;
                        case R.id.idMenuSettings:
                            Intent settingsIntent = new Intent(CollectionActivity.this, SettingsActivity.class);
                            bundle.putInt("menuItemSelected", menuItem.getItemId());
                            settingsIntent.putExtras(bundle);
                            startActivity(settingsIntent);
                            break;
                        case R.id.idMenuAboutUs:
                            Intent aboutUsIntent = new Intent(CollectionActivity.this, AboutUsActivity.class);
                            bundle.putInt("menuItemSelected", menuItem.getItemId());
                            aboutUsIntent.putExtras(bundle);
                            startActivity(aboutUsIntent);
                            break;
                        case R.id.idMenuHowToUse:
                            Intent howToUseIntent = new Intent(CollectionActivity.this, HowToUseActivity.class);
                            bundle.putInt("menuItemSelected", menuItem.getItemId());
                            howToUseIntent.putExtras(bundle);
                            startActivity(howToUseIntent);
                            break;
                    }
                    return true;
                });
                bundle = getIntent().getExtras();
                String collectionName = bundle.getString("collectionName");
                setTitle(collectionName != null ? collectionName : "All cards");
                new LoadCardsFromCollections().execute();

                mCardListView.setOnItemClickListener(seeCard);
                registerForContextMenu(mCardListView);
            }
        }
    }

    private boolean initDataMembers() {
        mContext = this;
        mDb = new CardDB(mContext);
        mCardListView = findViewById(R.id.idCardListView);
        mProgressBarHolder = findViewById(R.id.idProgressBarHolder);
        mDrawerLayout = findViewById(R.id.idDrawerLayout);
        mToolbar = findViewById(R.id.idToolbar);
        mNavigationView = findViewById(R.id.idNavigationView);
        mCoordinatorLayout = findViewById(R.id.idCoordinatorLayout);

        Object[] objects = {mContext, mDb, mCardListView, mProgressBarHolder, mDrawerLayout,
                mToolbar, mNavigationView, mCoordinatorLayout};

        for (Object o : objects) {
            if (o == null) return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = this.getMenuInflater();
        mi.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.idMenuItemCardSearch);
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
                            case "tgh":
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
        String cardName = cardListArray.get(info.position).getName();
        switch (item.getItemId()) {
            case R.id.idContextItemAddToCollection:
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                String cardId = cardListArray.get(info.position).getId();
                builder.setTitle(cardName);
                View viewInflated = LayoutInflater.from(mContext).inflate(R.layout.input_dialog, mCardListView, false);
                final EditText input = viewInflated.findViewById(R.id.idUserInput);
                mSpinner = viewInflated.findViewById(R.id.idCollectionSpinner);
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                        (this, android.R.layout.simple_spinner_item,
                                collectionsName); //selected item will look like a mSpinner set from XML
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                        .simple_spinner_dropdown_item);
                mSpinner.setAdapter(spinnerArrayAdapter);
                builder.setView(viewInflated);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        int mNumberOfCards = Integer.parseInt(input.getText().toString());
                        int collectionID = mSpinner.getSelectedItemPosition() + 1;
                        mDb.addCardInCollection(cardId, collectionID, mNumberOfCards);
                        if (bundle.getString("collectionName") != null) {
                            cardListArray.clear();
                            cardListArray.addAll(mDb.retrieveAllCardsInCollection(collectionID));
                            itemsAdapter.notifyDataSetChanged();
                        }
                        Snackbar addCardsSnackbar = make(mCoordinatorLayout,
                                "Added " + mNumberOfCards + " of " + cardName + "  to the "
                                        + mDb.retrieveAllCollections().get(collectionID - 1).getName(),
                                Snackbar.LENGTH_LONG);
                        addCardsSnackbar.show();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return true;
            case R.id.idContextItemDeleteOneFromCollection:
                cardListArray.remove(info.position);
                itemsAdapter.notifyDataSetChanged();
                Snackbar deletedOneSnackbar = make(mCoordinatorLayout,
                        "Deleted 1 " + cardName + " from this collection",
                        Snackbar.LENGTH_LONG);
                deletedOneSnackbar.show();
                return true;
            case R.id.idContextItemDeleteAllFromCollection:
                for (Card card : allCards) {
                    if(card.getName().equals(cardName)){
                        cardListArray.remove(card);
                    }
                }
                itemsAdapter.notifyDataSetChanged();
                Snackbar deletedAllSnackbar = make(mCoordinatorLayout,
                        "Deleted all " + cardName + " from this collection",
                        Snackbar.LENGTH_LONG);
                deletedAllSnackbar.show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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
            mProgressBarHolder.setAnimation(inAnimation);
            mProgressBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            mProgressBarHolder.setAnimation(outAnimation);
            mProgressBarHolder.setVisibility(View.GONE);
            mCardListView.setAdapter(itemsAdapter);
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
