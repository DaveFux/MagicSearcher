package android.example.com.magicproject_v1;

import android.content.Context;
import android.example.com.magicproject_v1.utils.ImageLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.concurrent.ExecutionException;

public class CardViewActivity extends AppCompatActivity {

    protected Context mContext;
    protected ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardview);

        init();
    }

    protected void init(){
        mContext = this;
        mImageView = findViewById(R.id.imageView);

        Bundle b = getIntent().getExtras();
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
