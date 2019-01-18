package android.example.com.magicproject_v1;

import android.content.Context;
import android.content.SharedPreferences;
import android.example.com.magicproject_v1.classes.Card;
import android.example.com.magicproject_v1.utils.ImageLoader;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CardsArrayAdapter extends ArrayAdapter<Card> {

    static class ViewHolder{
        ImageView cardThumbnail;
        TextView cardNameTextView;
        TextView cardDescriptionTextView;
        TextView cardManaCostTextView;
    }

    private SharedPreferences preferences;
    private Context mContext;
    private List<Card> cards;
    private HashMap<String, Integer> quantities;

    public CardsArrayAdapter(Context context, List<Card> objects) {
        super(context, 0, objects);
        this.mContext = context;
        this.cards = objects;
        quantities = new HashMap<>();
        this.quantities = filterDuplicates(objects);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
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
        ViewHolder view;
        View listItem = convertView;
        if (listItem == null) {
            System.out.println("ALLO");
            view = new ViewHolder();
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_cards, parent, false);

            view.cardThumbnail = listItem.findViewById(R.id.thumbnail);
            view.cardNameTextView = listItem.findViewById(R.id.cardName);
            view.cardDescriptionTextView = listItem.findViewById(R.id.description);
            view.cardManaCostTextView = listItem.findViewById(R.id.manaCost);

            listItem.setTag(view);
        } else {
            view = (ViewHolder) convertView.getTag();
        }

        System.out.println("BOAS");
        Card card = cards.get(position);
        int quantity = quantities.get(card.getName());


        if(preferences.getBoolean("thumbnails", true)) {
            try {
                if(card.getThumbnail() != null){
                    view.cardThumbnail.setImageBitmap(card.getThumbnail());
                }else{
                    Bitmap thumbnail = new ImageLoader().execute(card.getThumbnailURL()).get();
                    card.setThumbnail(thumbnail);
                    view.cardThumbnail.setImageBitmap(thumbnail);
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        view.cardNameTextView.setText(quantity + " x " + card.getName());

        String strDescription = card.getType();
        if(card.getType().contains("Creature")){
            strDescription += " (" + card.getPower() + "/" + card.getToughness() + ")";
        }
        view.cardDescriptionTextView.setText(strDescription);

        if(preferences.getBoolean("manaCost", true)) {
            view.cardManaCostTextView.setText(card.getManaCost().toString());
        }

        return listItem;
    }
}
