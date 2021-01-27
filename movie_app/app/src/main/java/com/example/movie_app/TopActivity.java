package com.example.movie_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TopActivity extends AppCompatActivity {

    GridView gridViewTop;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public List<String> topList;
    ImageButton btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        topList = new ArrayList<>();
        btn = findViewById(R.id.back_top);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        db.collection("News").document("top10")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    topList = (List<String>) document.get("movies");
                    gridViewTop = findViewById(R.id.grid_view2);
                    gridViewTop.setAdapter(new TopAdapter(getApplicationContext()));

                    gridViewTop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            sendData(i,topList);
                        }
                    });
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
    public void sendData(int position, List<String> premieresList){
        Intent intent = new Intent(this, TicketActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("premiereList", (Serializable) premieresList);
        startActivity(intent);
    }
}