package com.example.movie_app;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;


//import com.example.chilltime.movies.DetailsMovie;
//import com.example.chilltime.R;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.example.movie_app.QRCode;


public class PlayVideo extends YouTubeBaseActivity {
    // Views
    Button scan_Again;
    YouTubePlayerView video;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        video = findViewById(R.id.video);

        // buscar o id do video no yotube
        final Intent intent = getIntent();

        

        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                System.out.println("DEU");
                final String videoId = intent.getStringExtra(QRCode.EXTRA_MESSAGE);
                youTubePlayer.loadVideo(videoId);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                System.out.println("FAIL");
            }
        };

        // Inicializar Youtube Player
        video.initialize(YoutubeConfig.getApiKey(), onInitializedListener);


    }
}