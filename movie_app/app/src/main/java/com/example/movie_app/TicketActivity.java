package com.example.movie_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class TicketActivity extends AppCompatActivity {
    Intent intentPosition;
    int position;
    List<String> premiereList;
    ImageView imageView;
    String movie;
    Button btnYes,btnNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        intentPosition = getIntent();
        position = intentPosition.getIntExtra("position",0);
        premiereList = intentPosition.getStringArrayListExtra("premiereList");
        imageView = findViewById(R.id.movie_cover);
        btnNo = findViewById(R.id.btn_no);
        btnYes = findViewById(R.id.btn_yes);

        movie=premiereList.get(position);
        Picasso.with(this)
                .load(movie)
                .into(imageView );

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TicketActivity.this,Pop.class));
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }
}

