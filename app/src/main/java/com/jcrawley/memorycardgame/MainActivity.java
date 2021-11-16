package com.jcrawley.memorycardgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcrawley.memorycardgame.game.CardLayoutPopulator;
import com.jcrawley.memorycardgame.game.Game;

public class MainActivity extends AppCompatActivity {

    private int screenWidth, screenHeight;
    private LinearLayout resultsLayout;
    private Game game;
    private boolean isReadyToDismissResults = false;
    private Animation dropInAnimation, dropOutAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor();
        setContentView(R.layout.activity_main);
        setupResultsLayout();
        assignScreenDimensions();
        setupDropInAnimation();
        setupDropOutAnimation();
        TextView numberOfTurnsTextView = findViewById(R.id.numberOfTurnsTextView);
        game = new Game(this, numberOfTurnsTextView, screenWidth);
        final LinearLayout linearLayout = findViewById(R.id.cardLayout);
        CardLayoutPopulator cardLayoutPopulator = new CardLayoutPopulator(this, linearLayout, game);
        game.setCardLayoutPopulator(cardLayoutPopulator);
        linearLayout.getViewTreeObserver().addOnGlobalLayoutListener(cardLayoutPopulator::addCards);

    }


    private void setupDropInAnimation(){
        dropInAnimation = new TranslateAnimation(
                0,
                0,
                -screenHeight,
                0);
        dropInAnimation.setDuration(500);
        dropInAnimation.setFillAfter(true);

        dropInAnimation.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(() -> isReadyToDismissResults=true, 800);
            }
        });
    }


    public void setupDropOutAnimation(){
        dropOutAnimation = new TranslateAnimation(
                0,
                0,
                 0,
                screenHeight);
        dropOutAnimation.setDuration(500);
        dropOutAnimation.setFillAfter(true);

        dropOutAnimation.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                resultsLayout.clearAnimation();
                resultsLayout.setVisibility(View.GONE);
                game.startAgain();
            }
        });
    }


    private void setupResultsLayout(){
        resultsLayout = findViewById(R.id.resultsLayout);
        resultsLayout.setOnClickListener(view -> dismissResults());
    }


    private void setStatusBarColor(){
        Window window = getWindow();
        if(window != null){
            window.setStatusBarColor(getResources().getColor(R.color.status_bar_color, getTheme()));        }
    }


    private void assignScreenDimensions() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
    }


    public void displayResults(String resultsText, String recordText){
        TextView resultsTextView = findViewById(R.id.finalNumberOfTurnsTextView);
        TextView recordTextView = findViewById(R.id.currentRecordTurnsTextView);
        resultsTextView.setText(resultsText);
        recordTextView.setText(recordText);
        resultsLayout.setVisibility(View.VISIBLE);
        resultsLayout.startAnimation(dropInAnimation);
    }


    private void dismissResults(){
        if(isReadyToDismissResults) {
            isReadyToDismissResults = false;
            resultsLayout.clearAnimation();
            resultsLayout.startAnimation(dropOutAnimation);
        }
    }

}