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

    static class ViewHolder{
        TextView collectionNameTextView;
        TextView collectionTagsTextView;
        TextView collectionNumberCardsTextView;
    }

    private Context mContext;
    private List<Collection> collections;

    public CollectionsArrayAdapter(Context context, List<Collection> objects) {
        super(context, 0, objects);
        this.mContext = context;
        this.collections = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder view;
        View listItem = convertView;
        if(listItem == null){
            view = new ViewHolder();
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_collection, parent,false);

            view.collectionNameTextView = listItem.findViewById(R.id.collectionName);
            view.collectionTagsTextView = listItem.findViewById(R.id.tags);
            view.collectionNumberCardsTextView = listItem.findViewById(R.id.numberOfCards);

            listItem.setTag(view);
        } else {
            view = (CollectionsArrayAdapter.ViewHolder) convertView.getTag();
        }

        Collection collection = collections.get(position);

        view.collectionNameTextView.setText(collection.getName());

        view.collectionTagsTextView.setText(collection.getTags());

        view.collectionNumberCardsTextView.setText("Cards: " + collection.getNumberOfCards());

        return listItem;
    }
}
