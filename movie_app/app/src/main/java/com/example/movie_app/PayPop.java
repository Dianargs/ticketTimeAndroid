package com.example.movie_app;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class PayPop extends Activity {

    ImageButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pop);

        btn = findViewById(R.id.btnMbw);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.8));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTicket();
            }
        });

    }
    public void openTicket(){
        Intent intent = new Intent(this, TicketQrcodeActivity.class);
        startActivity(intent);
    }

}