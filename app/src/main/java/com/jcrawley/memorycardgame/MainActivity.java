package com.jcrawley.memorycardgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.jcrawley.memorycardgame.game.CardLayoutPopulator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CardLayoutPopulator cardLayoutPopulator = new CardLayoutPopulator(this);
    }
}