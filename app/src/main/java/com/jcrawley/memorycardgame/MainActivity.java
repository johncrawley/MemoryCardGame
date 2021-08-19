package com.jcrawley.memorycardgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.jcrawley.memorycardgame.game.CardLayoutPopulator;
import com.jcrawley.memorycardgame.game.Game;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Game game = new Game(this);

        final LinearLayout linearLayout = findViewById(R.id.cardLayout);

        CardLayoutPopulator cardLayoutPopulator = new CardLayoutPopulator(this, linearLayout, game);
        linearLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
               cardLayoutPopulator.addCards();
            }
        });
    }
}