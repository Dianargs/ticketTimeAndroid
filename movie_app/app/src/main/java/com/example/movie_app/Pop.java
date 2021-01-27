package com.example.movie_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Pop extends Activity {

    List<String> cinemaList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListView list_cinema;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_pop);

        cinemaList = new ArrayList<>();
        list_cinema =  findViewById(R.id.list_cinemas);



        db.collection("cinemas_location").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot doc : task.getResult()){
                        String name = doc.getString("name");
                        cinemaList.add(name);
                    }
                    ArrayAdapter<String> adapter =  new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,cinemaList);
                    list_cinema.setAdapter(adapter);
                    list_cinema.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            startActivity(new Intent(Pop.this,PayPop.class));
                        }
                    });
                }
            }
        });
    }
}
