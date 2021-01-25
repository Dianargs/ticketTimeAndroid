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

public class TopActivity extends AppCompatActivity {

    GridView gridViewTop;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public List<String> topList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        topList = new ArrayList<>();

        db.collection("News").document("top10")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    topList = (List<String>) document.get("movies");
                    gridViewTop = findViewById(R.id.grid_view2);
                    gridViewTop.setAdapter(new TopAdapter(getApplicationContext()));
                }
            }
        });
    }
    public class TopAdapter extends BaseAdapter {
        private Context mContext;

        public TopAdapter(Context c){
            mContext=c;
        }
        @Override
        public int getCount() {
            return topList.size();
        }

        @Override
        public Object getItem(int i) {
            return topList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ImageView imageViewTop;
            if(view==null){
                imageViewTop = new ImageView(mContext);
                imageViewTop.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,500 ));
                imageViewTop.setScaleType(ImageView.ScaleType.FIT_XY);
                imageViewTop.setPadding(2,2,2,2);
            }else{
                imageViewTop=(ImageView) view;
            }
            String url = (String) getItem(i);
            Picasso.with(mContext).load(url).fit().into(imageViewTop);

            return imageViewTop;
        }
    }
}