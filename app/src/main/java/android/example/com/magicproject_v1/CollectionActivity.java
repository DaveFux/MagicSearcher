package android.example.com.magicproject_v1;

        import android.content.Context;
        import android.content.Intent;
        import android.example.com.magicproject_v1.classes.Card;
        import android.support.annotation.NonNull;
        import android.support.design.widget.BottomNavigationView;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
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

public class CollectionActivity extends AppCompatActivity {

    protected Context mContext;
    protected ArrayList<String> cardListArray = new ArrayList<>();
    protected ArrayAdapter<String> itemsAdapter;
    protected ListView cardListView;
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
            ArrayList<String> result = mDb.retrieveAll(searchBar.getText().toString());
            ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, result);
            cardListView.setAdapter(itemsAdapter);
        }
    };
    //protected EditText.On
    protected ListView.OnItemClickListener seeCard=new ListView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            startActivity(new Intent(CollectionActivity.this, CardViewActivity.class));

        }
    };
    protected ListView.OnItemLongClickListener editCard = new ListView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(
                AdapterView<?> parent,
                View view,
                int position,
                long id) {
            cardListArray.remove(position);
            ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, cardListArray);
            cardListView.setAdapter(itemsAdapter);
            return false;
        }
    };
    protected ArrayList<String> test = new ArrayList<>();

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
        /*Card carta = new Card("Bela carta", "Carta", 1, 1, "Bela", Rarity.RARE, "", "", Mana.RedMana(2));
        Card carta2 = new Card("asd", "Carta", 1, 1, "Bela", Rarity.RARE, "", "", Mana.RedMana(2));
        Card carta3 = new Card("1233", "Carta", 1, 1, "Bela", Rarity.RARE, "", "", Mana.RedMana(2));
        Card carta4 = new Card("sgdfgs", "Carta", 1, 1, "Bela", Rarity.RARE, "", "", Mana.RedMana(2));
        Card carta5 = new Card("banana", "Carta", 1, 1, "Bela", Rarity.RARE, "", "", Mana.RedMana(2));
        mDb.addCard(carta);
        mDb.addCard(carta2);
        mDb.addCard(carta3);
        mDb.addCard(carta4);
        mDb.addCard(carta5);
        ArrayList<String> results = mDb.retrieveAll();*/

        List<Card> jsonResults = new ArrayList<>();
        List<String> results = new ArrayList<>();

        try {
            InputStream json = mContext.getAssets().open("cards.json");
            int size = json.available();
            JSONParser jp = new JSONParser();
            jsonResults.addAll(jp.readJsonStream(json));
            for (Card cardjson : jsonResults) {
                results.add(cardjson.getName());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cardListArray.addAll(results);
        //searchBar = findViewById(R.id.cardSearch);
        //searchBar.addTextChangedListener(searchWatcher);
        cardListView = findViewById(R.id.cardList);
        cardListArray.add("A");
        itemsAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, cardListArray); // pls no mexer
        cardListView.setAdapter(itemsAdapter);
        cardListView.setOnItemLongClickListener(editCard);
        cardListView.setOnItemClickListener(seeCard);

        BottomNavigationView bNavView = findViewById(R.id.bottom_navigation);
        bNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.my_cards:
                        Toast.makeText(CollectionActivity.this, "Action My Cards Clicked", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.deck:
                        Toast.makeText(CollectionActivity.this, "Action DECK Clicked", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.about_us:
                        Toast.makeText(CollectionActivity.this, "Action About us Clicked", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;


            }
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
                for (String card : cardListArray) {
                    if (card.toLowerCase().contains(newText)) {
                        result.add(card);
                    }
                }
                ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, result);
                cardListView.setAdapter(itemsAdapter);
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