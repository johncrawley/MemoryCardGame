package com.jcrawley.memorycardgame;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcrawley.memorycardgame.game.CardLayoutPopulator;
import com.jcrawley.memorycardgame.game.Game;

import java.time.Instant;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        Game game = new Game(this);
        final LinearLayout linearLayout = findViewById(R.id.cardLayout);
        if(outOfTime()){
            displaySorryMessage(linearLayout);
            return;
        }
        CardLayoutPopulator cardLayoutPopulator = new CardLayoutPopulator(this, linearLayout, game);
        game.setCardLayoutPopulator(cardLayoutPopulator);
        linearLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
               cardLayoutPopulator.addCards();
            }
        });
    }

    private boolean outOfTime(){
        return System.currentTimeMillis() > 1629990000_000L;
    }

    private void displaySorryMessage(LinearLayout linearLayout){
        TextView textView = new TextView(this);
        textView.setText("This version is now obsolete, thank you for your participation");
        linearLayout.addView(textView);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() ->{
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.sorry);
            mediaPlayer.start();
        }, 1500);
    }
}