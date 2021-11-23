package com.jcrawley.memorycardgame;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcrawley.memorycardgame.game.CardLayoutPopulator;
import com.jcrawley.memorycardgame.game.Game;

public class MainActivity extends AppCompatActivity {

    private int screenWidth, screenHeight;
    private LinearLayout resultsLayout, newGameLayout;
    private Game game;
    private boolean isReadyToDismissResults = false;
    private Animation resultsDropInAnimation, resultsDropOutAnimation, newGameDropInAnimation, newGameDropOutAnimation;
    private ActionBar actionBar;
    private int numberOfCards;
    private LinearLayout cardLayout;
    private int currentCardCount;
    private int currentFadeOutCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor();
        setContentView(R.layout.activity_main);
        actionBar = getSupportActionBar();
        setupResultsLayout();
        assignScreenDimensions();
        initAnimations();
        initButtons();
        game = new Game(this, screenWidth);
        cardLayout = findViewById(R.id.cardLayout);
        newGameLayout = findViewById(R.id.newGameLayout);
        CardLayoutPopulator cardLayoutPopulator = new CardLayoutPopulator(this, cardLayout, game);
        game.setCardLayoutPopulator(cardLayoutPopulator);
        cardLayout.getViewTreeObserver().addOnGlobalLayoutListener(cardLayoutPopulator::addCardViews);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuitems, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_new){
            removeAllCards();
            showNewGameLayout();
        }
        else if( id == R.id.action_about){
            showAboutView();
        }
        return true;
    }


    private void showAboutView(){
        
    }


    private void checkToRemoveAllCardsFromLayout(){
        if(currentFadeOutCount >= currentCardCount){
            cardLayout.removeAllViewsInLayout();
        }
    }


    private void removeAllCards(){
        int cardCount = cardLayout.getChildCount();
        currentFadeOutCount = 0;
        currentCardCount = cardCount;
        for(int i=0; i< cardCount; i++){
            View v = cardLayout.getChildAt(i);
            Animation fadeOutCardsAnimation = new AlphaAnimation(1, 0);
            fadeOutCardsAnimation.setInterpolator(new AccelerateInterpolator());
            fadeOutCardsAnimation.setStartOffset(200);
            fadeOutCardsAnimation.setDuration(1100);
            fadeOutCardsAnimation.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationEnd(Animation animation) {
                    v.setVisibility(View.GONE);
                    v.clearAnimation();
                    currentFadeOutCount++;
                    checkToRemoveAllCardsFromLayout();
                }
                public void onAnimationStart(Animation animation) { }
                public void onAnimationRepeat(Animation animation) { }
            });
            v.startAnimation(fadeOutCardsAnimation);
        }
    }


    private void initButtons(){
        setupButton(R.id.cards8Button, 8);
        setupButton(R.id.cards16Button, 16);
        setupButton(R.id.cards26Button, 26);
        setupButton(R.id.cards52Button, 52);
    }


    private void setupButton(int buttonId, int cardCount){
        Button button = findViewById(buttonId);
        button.setOnClickListener(v -> startNewGame(cardCount));
    }


    private void startNewGame(int numberOfCards){
        this.numberOfCards = numberOfCards;
        newGameLayout.clearAnimation();
        newGameLayout.setVisibility(View.VISIBLE);
        newGameLayout.startAnimation(newGameDropOutAnimation);
    }


    private void initAnimations(){
        setupResultsDropInAnimation();
        setupResultsDropOutAnimation();
        setupNewGameDropInAnimation();
        setupNewGameDropOutAnimation();
    }


    private void setupResultsDropInAnimation(){
        resultsDropInAnimation = new TranslateAnimation(
                0,
                0,
                -screenHeight,
                0);
        resultsDropInAnimation.setDuration(800);
        resultsDropInAnimation.setFillAfter(true);

        resultsDropInAnimation.setAnimationListener(new Animation.AnimationListener(){
            public void onAnimationStart(Animation arg0) { }
            public void onAnimationRepeat(Animation arg0) {}
            @Override
            public void onAnimationEnd(Animation arg0) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(() -> isReadyToDismissResults=true, 500);
            }
        });
    }


    public void setupResultsDropOutAnimation(){
        resultsDropOutAnimation = createDropOutAnimation(500, () -> {
            resultsLayout.clearAnimation();
            resultsLayout.setVisibility(View.GONE);
            showNewGameLayout();
        });
    }


    private Animation createDropOutAnimation(int duration, Runnable runnable){
        Animation dropOutAnimation = new TranslateAnimation(
                0,
                0,
                0,
                screenHeight);
        dropOutAnimation.setDuration(duration);
        dropOutAnimation.setFillAfter(true);
         dropOutAnimation.setAnimationListener(new Animation.AnimationListener(){
            public void onAnimationStart(Animation arg0) { }
            public void onAnimationRepeat(Animation arg0) { }
            @Override
            public void onAnimationEnd(Animation arg0) {
                runnable.run();
            }});

        return dropOutAnimation;
    }


    public void setupNewGameDropInAnimation(){
        newGameDropInAnimation = new TranslateAnimation(
                0,
                0,
                -screenHeight,
                0);
        newGameDropInAnimation.setDuration(700);
        newGameDropInAnimation.setFillAfter(true);
    }


    public void setupNewGameDropOutAnimation(){
        newGameDropOutAnimation = createDropOutAnimation(500, ()-> {
            newGameLayout.clearAnimation();
            newGameLayout.setVisibility(View.GONE);
            game.startAgain(numberOfCards);
            setPlainTitle();
        });
    }


    private void showNewGameLayout(){
        if(resultsLayout.getVisibility() == View.VISIBLE){
            resultsLayout.startAnimation(createDropOutAnimation(500, ()-> {
                    resultsLayout.clearAnimation();
                    resultsLayout.setVisibility(View.GONE);
                    }));
        }
        newGameLayout.setVisibility(View.VISIBLE);
        newGameLayout.startAnimation(newGameDropInAnimation);
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
        setGameOverTitle();
        TextView resultsTextView = findViewById(R.id.finalNumberOfTurnsTextView);
        TextView recordTextView = findViewById(R.id.currentRecordTurnsTextView);
        resultsTextView.setText(resultsText);
        recordTextView.setText(recordText);
        resultsLayout.setVisibility(View.VISIBLE);
        resultsLayout.startAnimation(resultsDropInAnimation);
    }


    public void setTitleWithTurns(int turn){
        String turnsWithTitle = getString(R.string.title) + getString(R.string.turn) + turn;
       actionBar.setTitle(turnsWithTitle);
    }


    public void setGameOverTitle(){
        actionBar.setTitle(getString(R.string.game_over));
    }


    public void setPlainTitle(){
        actionBar.setTitle(getString(R.string.title));
    }


    private void dismissResults(){
        if(isReadyToDismissResults) {
            isReadyToDismissResults = false;
            resultsLayout.clearAnimation();
            resultsLayout.startAnimation(resultsDropOutAnimation);
        }
    }

}