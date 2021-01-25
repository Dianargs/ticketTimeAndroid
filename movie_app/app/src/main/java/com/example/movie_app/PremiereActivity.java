package com.example.movie_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PremiereActivity extends AppCompatActivity {

    GridView gridViewPre;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public List<String> premieresList;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premiere);

        premieresList= new ArrayList<>();


        //getting premiereList from firebase
        db.collection("News").document("Premieres")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    premieresList = (List<String>) document.get("movies");
                    gridViewPre = findViewById(R.id.grid_view);
                    gridViewPre.setAdapter(new premieresAdapter(getApplicationContext()));
                }
            }
        });
    }


    public class premieresAdapter extends BaseAdapter {
        private Context mContext;

        public premieresAdapter(Context c){
            mContext=c;
        }
        @Override
        public int getCount() {
            return premieresList.size();
        }

        @Override
        public Object getItem(int i) {
            return premieresList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ImageView imageView;
            if(view==null){
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,500 ));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setPadding(2,2,2,2);
            }else{
                imageView=(ImageView) view;
            }
            String url = (String) getItem(i);
            Picasso.with(mContext).load(url).fit().into(imageView);

            return imageView;
        }
    }
}