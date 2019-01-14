package android.example.com.magicproject_v1;

import android.content.Context;
import android.content.SharedPreferences;
import android.example.com.magicproject_v1.classes.Card;
import android.example.com.magicproject_v1.utils.ImageLoader;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CardsArrayAdapter extends ArrayAdapter<Card> {

    private Context mContext;
    private List<Card> cards;
    private HashMap<String, Integer> quantities;

    public CardsArrayAdapter(Context context, List<Card> objects) {
        super(context, 0, objects);
        this.mContext = context;
        this.cards = objects;
        quantities = new HashMap<>();
        this.quantities = filterDuplicates(objects);
    }

    private HashMap<String, Integer> filterDuplicates(List<Card> cards){
        List<Card> cardsToRemove = new ArrayList<>();
        for (Card card : cards) {
            if(quantities.containsKey(card.getName())){
                quantities.replace(card.getName(), quantities.get(card.getName()) + 1);
                cardsToRemove.add(card);
            }else{
                quantities.put(card.getName(), 1);
            }
        }
        this.cards.removeAll(cardsToRemove);
        return quantities;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_cards, parent, false);
        }

        Card card = cards.get(position);
        int quantity = quantities.get(card.getName());


        //TODO: mostrar cartas repetidas

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        if(preferences.getBoolean("thumbnails", true)) {
            ImageView thumbnail = listItem.findViewById(R.id.thumbnail);
            try {
                thumbnail.setImageBitmap(new ImageLoader().execute(card.getThumbnail()).get());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        TextView cardName = listItem.findViewById(R.id.cardName);
        cardName.setText(quantity + " x " + card.getName());

        TextView description = listItem.findViewById(R.id.description);
        String strDescription = card.getType();
        if(card.getType().contains("Creature")){
            strDescription += " (" + card.getPower() + "/" + card.getToughness() + ")";
        }
        description.setText(strDescription);

        if(preferences.getBoolean("manaCost", true)) {
            TextView manaCost = listItem.findViewById(R.id.manaCost);
            manaCost.setText(card.getManaCost().toString());
        }

        return listItem;
    }
}
