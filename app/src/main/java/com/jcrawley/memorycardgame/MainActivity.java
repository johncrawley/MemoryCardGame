package com.jcrawley.memorycardgame;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewTreeObserver;
import android.view.Window;
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
        setStatusBarColor();

        setContentView(R.layout.activity_main);
        Game game = new Game(this, findViewById(R.id.numberOfTurnsTextView));
        final LinearLayout linearLayout = findViewById(R.id.cardLayout);
        CardLayoutPopulator cardLayoutPopulator = new CardLayoutPopulator(this, linearLayout, game);
        game.setCardLayoutPopulator(cardLayoutPopulator);
        linearLayout.getViewTreeObserver().addOnGlobalLayoutListener(cardLayoutPopulator::addCards);
    }


    private void setStatusBarColor(){
        Window window = getWindow();
        if(window != null){
            window.setStatusBarColor(getResources().getColor(R.color.status_bar_color, getTheme()));        }
    }

}