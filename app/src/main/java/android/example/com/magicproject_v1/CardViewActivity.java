package android.example.com.magicproject_v1;

import android.content.Context;
import android.example.com.magicproject_v1.classes.Card;
import android.example.com.magicproject_v1.utils.CardDB;
import android.example.com.magicproject_v1.utils.ImageLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static java.lang.Integer.parseInt;

public class CardViewActivity extends AppCompatActivity {

    protected Context mContext;
    protected ImageView mImageView;
    protected CardDB mDb;
    protected ImageView.OnClickListener testAddCards;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardview);

        init();
    }

    protected void init(){
        mContext = this;
        mDb=new CardDB(mContext);
        Bundle b = getIntent().getExtras();
        mImageView = findViewById(R.id.imageView);
        testAddCards= new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> c =b.getIntegerArrayList("ArrayIds");
                mDb.addCardInCollection((b.getString("id")),c.get(0));
                ArrayList<Card> cartas = mDb.retrieveAllCardsInCollection(c.get(0));
                String a = ""+cartas.size();
                Toast n = Toast.makeText(mContext, a, Toast.LENGTH_LONG);
                n.show();

            }
        };
        mImageView.setOnClickListener(testAddCards);

        if(b != null){
            String imageToLoad = b.getString("image");
            try {
                mImageView.setImageBitmap(new ImageLoader().execute(imageToLoad).get());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
