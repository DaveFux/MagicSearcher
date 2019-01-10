package android.example.com.magicproject_v1;

import android.content.Context;
import android.example.com.magicproject_v1.classes.Collection;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CollectionsArrayAdapter extends ArrayAdapter<Collection> {

    private Context mContext;
    private List<Collection> collections;

    public CollectionsArrayAdapter(Context context, List<Collection> objects) {
        super(context, 0, objects);
        this.mContext = context;
        this.collections = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null){
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_collection, parent,false);
        }

        Collection collection = collections.get(position);

        TextView collectionName = listItem.findViewById(R.id.collectionName);
        collectionName.setText(collection.getName());

        TextView tags = listItem.findViewById(R.id.tags);
        tags.setText(collection.getTags());

        TextView totalNumberOfCards = listItem.findViewById(R.id.numberOfCards);
        totalNumberOfCards.setText("Cards: " + collection.getNumberOfCards());

        return listItem;
    }
}
