package android.example.com.magicproject_v1;

import android.content.Context;
import android.example.com.magicproject_v1.classes.Card;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

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
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_collection, parent, false);
        }

        return listItem;
    }
}
