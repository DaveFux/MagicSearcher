package android.example.com.magicproject_v1;

import android.content.Context;
import android.content.SharedPreferences;
import android.example.com.magicproject_v1.classes.Card;
import android.example.com.magicproject_v1.utils.DuplicatesList;
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
    private DuplicatesList objects;
    private boolean allowDuplicates;

    public CardsArrayAdapter(Context context, DuplicatesList objects, boolean allowDuplicates) {
        super(context, 0, objects.getList());
        this.allowDuplicates = allowDuplicates;
        this.mContext = context;
        this.objects = objects;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder view;
        View listItem = convertView;
        if (listItem == null) {
            view = new ViewHolder();
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_cards, parent, false);

            view.cardThumbnail = listItem.findViewById(R.id.idThumbnailImageView);
            view.cardNameTextView = listItem.findViewById(R.id.idCardName);
            view.cardDescriptionTextView = listItem.findViewById(R.id.idDescription);
            view.cardManaCostTextView = listItem.findViewById(R.id.idManaCost);

            listItem.setTag(view);
        } else {
            view = (ViewHolder) convertView.getTag();
        }

        Card card = objects.getList().get(position);
        int quantity = 0;
        if (allowDuplicates){
             quantity = objects.getDuplicates().get(position);
        }

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

        String cardName = card.getName();
        if(allowDuplicates){
            cardName = quantity + " x " + card.getName();
        }
        view.cardNameTextView.setText(cardName);

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
