package android.example.com.magicproject_v1;

import android.content.Context;
import android.example.com.magicproject_v1.classes.Card;
import android.example.com.magicproject_v1.utils.ImageLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CardsArrayAdapter extends ArrayAdapter<Card> {

    private Context mContext;
    private List<Card> cards;

    public CardsArrayAdapter(Context context, List<Card> objects) {
        super(context, 0, objects);
        this.mContext = context;
        this.cards = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_cards, parent, false);
        }

        Card card = cards.get(position);

        //TODO: mostrar cartas repetidas

        ImageView thumbnail = listItem.findViewById(R.id.thumbnail);
        try {
            thumbnail.setImageBitmap(new ImageLoader().execute(card.getThumbnail()).get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TextView cardName = listItem.findViewById(R.id.cardName);
        cardName.setText(card.getName());

        TextView description = listItem.findViewById(R.id.description);
        String strDescription = card.getType();
        if(card.getType().contains("Creature")){
            strDescription += " (" + card.getPower() + "/" + card.getToughness() + ")";
        }
        description.setText(strDescription);

        TextView manaCost = listItem.findViewById(R.id.manaCost);
        manaCost.setText(card.getManaCost().toString());

        return listItem;
    }
}
