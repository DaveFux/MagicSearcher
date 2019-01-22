package android.example.com.magicproject_v1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.example.com.magicproject_v1.classes.Card;
import android.example.com.magicproject_v1.classes.Collection;
import android.example.com.magicproject_v1.utils.CardDB;
import android.example.com.magicproject_v1.utils.DuplicatesList;
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

import java.sql.SQLOutput;
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

    private boolean mSimplifiedView = true;
    private boolean mSortByName = false;
    private boolean mSortByType = false;
    private boolean mSortByPower = false;
    private boolean mSortByToughness = false;
    private boolean mSortByManaCost = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        init();
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

            bundle = getIntent().getExtras();
            if (bundle != null) {
                mSimplifiedView = !bundle.getBoolean("allCards");
                mNavigationView.setCheckedItem(bundle.getInt("menuItemSelected"));
                mNavigationView.setNavigationItemSelectedListener(menuItem -> {
                    mDrawerLayout.closeDrawers();
                    Bundle sendBundle = new Bundle();
                    switch (menuItem.getItemId()) {
                        case R.id.idMenuCollections:
                            Intent collectionsIntent = new Intent(CollectionActivity.this, MainActivity.class);
                            sendBundle.putInt("menuItemSelected", menuItem.getItemId());
                            collectionsIntent.putExtras(sendBundle);
                            startActivity(collectionsIntent);
                            break;
                        case R.id.idMenuSearchCards:
                            boolean allCards = bundle.getBoolean("allCards");
                            if (!allCards) {
                                Intent searchCardsIntent = new Intent(CollectionActivity.this, CollectionActivity.class);
                                sendBundle.putBoolean("allCards", true);
                                sendBundle.putInt("menuItemSelected", menuItem.getItemId());
                                searchCardsIntent.putExtras(sendBundle);
                                startActivity(searchCardsIntent);
                                finish();
                            }
                            break;
                        case R.id.idMenuRandomCard:
                            Card c = mDb.retrieveCard();
                            Intent randomCardIntent = new Intent(CollectionActivity.this, CardViewActivity.class);
                            sendBundle.putString("image", c.getImage());
                            sendBundle.putInt("menuItemSelected", menuItem.getItemId());
                            randomCardIntent.putExtras(sendBundle);
                            startActivity(randomCardIntent);
                            break;
                        case R.id.idMenuAddCollection:
                            startActivity(new Intent(CollectionActivity.this, NewCollectionActivity.class));
                            break;
                        case R.id.idMenuSettings:
                            Intent settingsIntent = new Intent(CollectionActivity.this, SettingsActivity.class);
                            sendBundle.putInt("menuItemSelected", menuItem.getItemId());
                            settingsIntent.putExtras(sendBundle);
                            startActivity(settingsIntent);
                            break;
                        case R.id.idMenuAboutUs:
                            Intent aboutUsIntent = new Intent(CollectionActivity.this, AboutUsActivity.class);
                            sendBundle.putInt("menuItemSelected", menuItem.getItemId());
                            aboutUsIntent.putExtras(sendBundle);
                            startActivity(aboutUsIntent);
                            break;
                        case R.id.idMenuHowToUse:
                            Intent howToUseIntent = new Intent(CollectionActivity.this, HowToUseActivity.class);
                            sendBundle.putInt("menuItemSelected", menuItem.getItemId());
                            howToUseIntent.putExtras(sendBundle);
                            startActivity(howToUseIntent);
                            break;
                    }
                    return true;
                });

                mCardListView.setOnItemClickListener((parent, view, position, id) -> {
                    Intent intent = new Intent(CollectionActivity.this, CardViewActivity.class);
                    Bundle b = new Bundle();
                    if(mSimplifiedView){
                        DuplicatesList dl = filterResults();
                        b.putString("image", dl.getList().get(position).getImage());
                    }else{
                        b.putString("image", cardListArray.get(position).getImage());
                    }
                    intent.putExtras(b);
                    startActivity(intent);
                });

                String collectionName = bundle.getString("collectionName");
                setTitle(collectionName != null ? collectionName : "All cards");
                new LoadCardsFromCollections().execute();

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
        mi.inflate(R.menu.menu_cards, menu);
        MenuItem item = menu.findItem(R.id.idMenuItemCardSearch);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                String str = query.toLowerCase();
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
                                                if (card.getPower() == Integer.parseInt(strAfterSplit[1])) {
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
                                                if (card.getToughness() == Integer.parseInt(strAfterSplit[1])) {
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
                DuplicatesList duplicatesList = new DuplicatesList();
                if(mSimplifiedView){
                    duplicatesList = filterResults();
                } else {
                    for (Card card : cardListArray) {
                        duplicatesList.getList().add(card);
                        duplicatesList.getDuplicates().add(1);
                    }
                }
                itemsAdapter = new CardsArrayAdapter(mContext, duplicatesList, mSimplifiedView);
                mCardListView.setAdapter(itemsAdapter);
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
            case R.id.idMenuNormalView:
                mSimplifiedView = false;
                break;
            case R.id.idMenuSimplifiedView:
                mSimplifiedView = true;
                break;
            case R.id.idMenuSortName:
                cardListArray.sort((o1, o2) -> {
                    if(mSortByName){
                        return o2.getName().compareTo(o1.getName());
                    } else {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                mSortByName = !mSortByName;
                break;
            case R.id.idMenuSortType:
                cardListArray.sort((o1, o2) -> {
                    if(mSortByType){
                        return o2.getType().compareTo(o1.getType());
                    } else {
                        return o1.getType().compareTo(o2.getType());
                    }
                });
                mSortByType = !mSortByType;
                break;
            case R.id.idMenuSortManaCost:
                cardListArray.sort((o1, o2) -> {
                    if(mSortByManaCost){
                        return Integer.compare(o1.getManaCost().convertedManaCost(), o2.getManaCost().convertedManaCost());
                    } else {
                        return Integer.compare(o2.getManaCost().convertedManaCost(), o1.getManaCost().convertedManaCost());
                    }
                });
                mSortByManaCost = !mSortByManaCost;
                break;
            case R.id.idMenuSortPower:
                cardListArray.sort((o1, o2) -> {
                    if(mSortByPower){
                        return Integer.compare(o1.getPower(), o2.getPower());
                    } else {
                        return Integer.compare(o2.getPower(), o1.getPower());
                    }
                });
                mSortByPower = !mSortByPower;
                break;
            case R.id.idMenuSortToughness:
                cardListArray.sort((o1, o2) -> {
                    if(mSortByToughness){
                        return Integer.compare(o1.getToughness(), o2.getToughness());
                    } else {
                        return Integer.compare(o2.getToughness(), o1.getToughness());
                    }
                });
                mSortByToughness = !mSortByToughness;
                break;
        }
        DuplicatesList duplicatesList = new DuplicatesList();
        if(mSimplifiedView){
            duplicatesList = filterResults();
        } else {
            for (Card card : cardListArray) {
                duplicatesList.getList().add(card);
                duplicatesList.getDuplicates().add(1);
            }
        }
        itemsAdapter = new CardsArrayAdapter(mContext, duplicatesList, mSimplifiedView);
        mCardListView.setAdapter(itemsAdapter);
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
        DuplicatesList nl = filterResults();

        String cardName = cardListArray.get(info.position).getName();
        String cardId = cardListArray.get(info.position).getId();
        if(mSimplifiedView){
            cardName = nl.getList().get(info.position).getName();
            cardId = nl.getList().get(info.position).getId();
        }
        int realCollectionID = bundle.getInt("collectionId");

        switch (item.getItemId()) {
            case R.id.idContextItemAddToCollection:
                if(collections.size()>0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle(cardName);
                    View viewInflated = LayoutInflater.from(mContext).inflate(
                            R.layout.input_dialog, mCardListView, false);
                    final EditText input = viewInflated.findViewById(R.id.idUserInput);
                    mSpinner = viewInflated.findViewById(R.id.idCollectionSpinner);
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                            this, android.R.layout.simple_spinner_item, collectionsName); //selected item will look like a mSpinner set from XML
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                            .simple_spinner_dropdown_item);
                    mSpinner.setAdapter(spinnerArrayAdapter);
                    builder.setView(viewInflated);

                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            String cardId = cardListArray.get(info.position).getId();
                            String cardName = cardListArray.get(info.position).getName();
                            if (mSimplifiedView) {
                                cardName = nl.getList().get(info.position).getName();
                                cardId = nl.getList().get(info.position).getId();
                            }
                            int mNumberOfCards = Integer.parseInt(input.getText().toString());
                            int collectionIDInSpinner = collections.get(mSpinner.getSelectedItemPosition()).getId();
                            mDb.addCardInCollection(cardId, collectionIDInSpinner, mNumberOfCards);
                            if (bundle.getString("collectionName") != null) {
                                cardListArray.clear();
                                cardListArray.addAll(mDb.retrieveAllCardsInCollection(collectionIDInSpinner));

                                DuplicatesList duplicatesList = new DuplicatesList();
                                if (mSimplifiedView) {
                                    duplicatesList = filterResults();
                                } else {
                                    for (Card card : cardListArray) {
                                        duplicatesList.getList().add(card);
                                        duplicatesList.getDuplicates().add(1);
                                    }
                                }
                                itemsAdapter = new CardsArrayAdapter(mContext, duplicatesList, mSimplifiedView);
                                mCardListView.setAdapter(itemsAdapter);
                            }
                            Snackbar addCardsSnackbar = make(mCoordinatorLayout,
                                    "Added " + mNumberOfCards + " " + cardName + " to the collection "
                                            + collections.get(mSpinner.getSelectedItemPosition()).getName(),
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
                }else{
                    Snackbar noCollectionsSnackbar = make(mCoordinatorLayout,
                            "No existing collections to add cards", Snackbar.LENGTH_LONG);
                    noCollectionsSnackbar.show();
                }
                return true;
            case R.id.idContextItemDeleteOneFromCollection:
                if (mSimplifiedView) {
                    Snackbar estaNaSimplifiedView = make(mCoordinatorLayout,
                            "You must leave Simplified View to delete cards.",
                            Snackbar.LENGTH_LONG);
                    estaNaSimplifiedView.show();
                } else {
                    ArrayList<Card> cardsUpdated = mDb.retrieveAllCardsInCollection(cardId, realCollectionID);
                    mDb.deleteOneCardFromCollection(cardId, realCollectionID, cardsUpdated.size() - 1);
                    cardListArray.clear();
                    cardListArray.addAll(mDb.retrieveAllCardsInCollection(realCollectionID));

                    DuplicatesList normalList = new DuplicatesList();
                    for (Card card : cardListArray) {
                        normalList.getList().add(card);
                        normalList.getDuplicates().add(1);
                    }
                    itemsAdapter = new CardsArrayAdapter(mContext, normalList, mSimplifiedView);
                    mCardListView.setAdapter(itemsAdapter);
                    Snackbar deletedOneSnackbar = make(mCoordinatorLayout,
                            "Deleted 1 " + cardName + " from this collection",
                            Snackbar.LENGTH_LONG);
                    deletedOneSnackbar.show();
                }
                return true;
            case R.id.idContextItemDeleteAllFromCollection:
                mDb.deleteAllCardsFromCollection(cardId, realCollectionID);
                cardListArray.clear();
                cardListArray.addAll(mDb.retrieveAllCardsInCollection(realCollectionID));

                DuplicatesList duplicatesList = new DuplicatesList();
                if(mSimplifiedView){
                    duplicatesList = filterResults();
                }else{
                    for (Card card : cardListArray) {
                        duplicatesList.getList().add(card);
                        duplicatesList.getDuplicates().add(1);
                    }
                }
                itemsAdapter = new CardsArrayAdapter(mContext, duplicatesList, mSimplifiedView);
                mCardListView.setAdapter(itemsAdapter);
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
                DuplicatesList duplicatesList = new DuplicatesList();
                if(mSimplifiedView){
                    duplicatesList = filterResults();
                } else {
                    for (Card card : cardListArray) {
                        duplicatesList.getList().add(card);
                        duplicatesList.getDuplicates().add(1);
                    }
                }
                itemsAdapter = new CardsArrayAdapter(mContext, duplicatesList, mSimplifiedView);
            }
            return null;
        }
    }

    private DuplicatesList filterResults(){
        DuplicatesList retorno = new DuplicatesList();
        for (Card card : cardListArray) {
            boolean contains = false;
            for (int i=0; i < retorno.getList().size(); i++){
                contains = retorno.getList().get(i).getName().contains(card.getName());
                if (contains) {
                    retorno.getDuplicates().set(i, retorno.getDuplicates().get(i) + 1);
                    break;
                }
            }
            if (!contains) {
                retorno.getList().add(card);
                retorno.getDuplicates().add(1);
            }
        }
        return retorno;
    }
}
