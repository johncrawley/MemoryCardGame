package com.jcrawley.memorycardgame;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
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

import com.jcrawley.memorycardgame.background.BackgroundFactory;
import com.jcrawley.memorycardgame.card.DeckSize;
import com.jcrawley.memorycardgame.card.cardType.CardType;
import com.jcrawley.memorycardgame.card.CardBackManager;
import com.jcrawley.memorycardgame.game.CardLayoutPopulator;
import com.jcrawley.memorycardgame.game.Game;
import com.jcrawley.memorycardgame.list.BackgroundRecyclerAdapter;
import com.jcrawley.memorycardgame.list.CardTypeRecyclerAdapter;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int screenWidth, screenHeight;
    private LinearLayout resultsLayout, newGameLayout, cardLayout, aboutLayout;//, settingsLayout;
    private Game game;
    private boolean isReadyToDismissResults = false;
    private Animation resultsDropInAnimation, resultsDropOutAnimation, newGameDropInAnimation, newGameDropOutAnimation;
    private ActionBar actionBar;
    private DeckSize deckSize;
    private int currentCardCount;
    private int currentFadeOutCount = 0;
    private boolean isShowingNewGameDialogue;
    private boolean isShowingAboutDialogue;
    private boolean isShowingSettingsDialogue;
    private enum AnimationDirection {DROP_IN, DROP_OUT}
    private MainViewModel viewModel;
    private RecordKeeper recordKeeper;
    private BitmapLoader bitmapLoader;
    private CardBackManager cardBackManager;
    private ConstraintLayout settingsLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor();
        setContentView(R.layout.activity_main);
        recordKeeper = new RecordKeeper(MainActivity.this);
        actionBar = getSupportActionBar();
        setupResultsLayout();
        assignScreenDimensions();
        initAnimations();
        initButtons();
        setupLayouts();
        viewModel  = new ViewModelProvider(this).get(MainViewModel.class);
        bitmapLoader = new BitmapLoader(MainActivity.this, viewModel);
        cardBackManager = new CardBackManager(viewModel, bitmapLoader);
        game = new Game(this, cardBackManager, bitmapLoader, screenWidth);
        CardLayoutPopulator cardLayoutPopulator = new CardLayoutPopulator(this, cardLayout, game, cardBackManager);
        setupSettings();
        cardLayout.getViewTreeObserver().addOnGlobalLayoutListener(()-> game.initCards(cardLayoutPopulator));
    }


    public void setBackground(int drawableId){
        cardLayout.setBackground(AppCompatResources.getDrawable(MainActivity.this, drawableId));
    }


    private void setupLayouts(){
        cardLayout = findViewById(R.id.cardLayout);
        newGameLayout = findViewById(R.id.new_game_include);
        aboutLayout = findViewById(R.id.about_include);
        settingsLayout = findViewById(R.id.settings_include);
    }


    public MainViewModel getViewModel(){
        return viewModel;
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
        else if( id == R.id.action_settings){
          showSettingsView();
        }
        return true;
    }


    public RecordKeeper getRecordKeeper(){
        return this.recordKeeper;
    }


    private void setupSettings(){
        setupFaceTypesRecyclerView();
        setupBackTypesRecyclerView();
        setupBackgroundRecyclerView();
    }


    private void setupFaceTypesRecyclerView(){
        RecyclerView cardFacesRecyclerView = findViewById(R.id.cardTypeRecycleView);
        List<CardType> cardTypes = Arrays.asList(CardType.STANDARD, CardType.SIMPLE);
        CardTypeRecyclerAdapter cardTypeRecyclerAdapter = new CardTypeRecyclerAdapter(cardTypes, bitmapLoader, getViewModel().cardDeckImages, ()->{});
        cardTypeRecyclerAdapter.init(cardFacesRecyclerView, MainActivity.this);

    }


    private void setupBackTypesRecyclerView(){
        RecyclerView cardBacksRecyclerView = findViewById(R.id.cardBackRecycleView);
        List<CardType> cardTypes = Arrays.asList(CardType.BACK_1,
                CardType.BACK_2,
                CardType.BACK_GREEN_DIAGONALS,
                CardType.BACK_CRISSCROSS_RED_BLUE,
                CardType.BACK_GREEN_DIAGONALS_LIGHT);
        CardTypeRecyclerAdapter cardTypeRecyclerAdapter = new CardTypeRecyclerAdapter(cardTypes, bitmapLoader, cardBackManager, ()-> game.switchBacksOnFaceDownCards());
        cardTypeRecyclerAdapter.init(cardBacksRecyclerView, MainActivity.this);
    }


    private void setupBackgroundRecyclerView(){
        RecyclerView backgroundRecyclerView = settingsLayout.findViewById(R.id.backgroundRecyclerView);

        BackgroundRecyclerAdapter backgroundRecyclerAdapter = new BackgroundRecyclerAdapter(BackgroundFactory.getAll());
        backgroundRecyclerAdapter.init(backgroundRecyclerView, MainActivity.this);
    }


    private void showAboutView(){
        dismissSettingsDialog();
        if(isShowingAboutDialogue){
            return;
        }
        aboutLayout.setVisibility(View.VISIBLE);
        isShowingAboutDialogue = true;
        aboutLayout.startAnimation(createDropAnimation(AnimationDirection.DROP_IN, () -> {}));
    }


    private void showSettingsView(){
        if(isShowingSettingsDialogue){
            return;
        }
        dismissAboutDialog();
        settingsLayout.setVisibility(View.VISIBLE);
        isShowingSettingsDialogue = true;
        settingsLayout.startAnimation(createDropAnimation(AnimationDirection.DROP_IN, () -> {}));

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
            fadeOutCardsAnimation.setStartOffset(getInt(R.integer.fade_out_cards_start_offset));
            fadeOutCardsAnimation.setDuration(getInt(R.integer.fade_out_cards_duration));
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
        setupButton(R.id.cards8Button, DeckSize.EIGHT);
        setupButton(R.id.cards16Button, DeckSize.SIXTEEN);
        setupButton(R.id.cards26Button, DeckSize.TWENTY_SIX);
        setupButton(R.id.cards52Button, DeckSize.FIFTY_TWO);
        findViewById(R.id.dismissAboutButton).setOnClickListener((View v)-> dismissAboutDialog());
        findViewById(R.id.dismissSettingsButton).setOnClickListener((View v)-> dismissSettingsDialog());
    }


    public void dismissAboutDialog(){
        if(!isShowingAboutDialogue){
            return;
        }
        isShowingAboutDialogue = false;
        aboutLayout.clearAnimation();

        aboutLayout.startAnimation(createDropAnimation(AnimationDirection.DROP_OUT, ()-> {
            aboutLayout.clearAnimation();
            aboutLayout.setVisibility(View.INVISIBLE);
        }));
    }


    public void dismissSettingsDialog(){
        if(!isShowingSettingsDialogue){
            return;
        }
        isShowingSettingsDialogue = false;
        settingsLayout.clearAnimation();

        settingsLayout.startAnimation(createDropAnimation(AnimationDirection.DROP_OUT, ()-> {
            settingsLayout.clearAnimation();
            settingsLayout.setVisibility(View.INVISIBLE);
        }));
    }


    private void setupButton(int buttonId, DeckSize deck_size){
        Button button = findViewById(buttonId);
        button.setOnClickListener(v -> startNewGame(deck_size));
    }


    private void startNewGame(DeckSize deckSize){
        if(isShowingAboutDialogue){
            return;
        }
        this.deckSize = deckSize;
        recordKeeper.saveLastUsedNumberOfCards(deckSize.name());
        System.out.println("MainActivity: last number of cards used : " + recordKeeper.getLastUsedNumberOfCards());
        isShowingNewGameDialogue = false;
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
        resultsDropInAnimation = createDropAnimation(AnimationDirection.DROP_IN, () -> {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> isReadyToDismissResults=true,
                    getInt(R.integer.enable_dismiss_results_delay));
        });
    }


    private int getInt(int resId){
        return getResources().getInteger(resId);
    }


    public void setupResultsDropOutAnimation(){
        resultsDropOutAnimation = createDropAnimation(AnimationDirection.DROP_OUT, () -> {
            resultsLayout.clearAnimation();
            resultsLayout.setVisibility(View.GONE);
            showNewGameLayout();
        });
    }


    public void setupNewGameDropInAnimation(){
        newGameDropInAnimation = createDropAnimation(AnimationDirection.DROP_IN, ()->{});
    }


    public void setupNewGameDropOutAnimation(){
        newGameDropOutAnimation = createDropAnimation(AnimationDirection.DROP_OUT, ()-> {
            newGameLayout.clearAnimation();
            newGameLayout.setVisibility(View.GONE);
            game.startAgain(deckSize);
            setPlainTitle();
        });
    }


    private Animation createDropAnimation(AnimationDirection direction, Runnable onAnimationEnd ){
        Animation dropOutAnimation = new TranslateAnimation(
                0,
                0,
                direction == AnimationDirection.DROP_IN ? -screenHeight : 0,
                direction == AnimationDirection.DROP_OUT ? screenHeight : 0);
        dropOutAnimation.setDuration(getResources().getInteger(R.integer.view_drop_duration));
        dropOutAnimation.setFillAfter(true);
        dropOutAnimation.setAnimationListener(new Animation.AnimationListener(){
            public void onAnimationStart(Animation arg0) { }
            public void onAnimationRepeat(Animation arg0) { }
            @Override
            public void onAnimationEnd(Animation arg0) {
                onAnimationEnd.run();
            }});

        return dropOutAnimation;
    }


    private void showNewGameLayout(){
        dismissAboutDialog();
        dismissSettingsDialog();
        if(isShowingNewGameDialogue){
            return;
        }
        setPlainTitle();
        isShowingNewGameDialogue = true;
        dismissResultsLayoutIfVisible();
        newGameLayout.setVisibility(View.VISIBLE);
        newGameLayout.startAnimation(newGameDropInAnimation);
    }


    private void dismissResultsLayoutIfVisible(){
        if(resultsLayout.getVisibility() == View.VISIBLE){
            resultsLayout.startAnimation(createDropAnimation(AnimationDirection.DROP_OUT, ()-> {
                resultsLayout.clearAnimation();
                resultsLayout.setVisibility(View.GONE);
            }));
        }
    }


    private void setupResultsLayout(){
        resultsLayout = findViewById(R.id.game_over_include);
        resultsLayout.setOnClickListener(view -> dismissResults());
    }


    private void setStatusBarColor(){
        Window window = getWindow();
        if(window != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setStatusBarColor(getResources().getColor(R.color.status_bar_color, getTheme()));
        }
    }


    private void assignScreenDimensions() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
    }


    public void displayResults(int numberOfTurns, String recordText){
        setGameOverTitle();
        TextView resultsTextView = findViewById(R.id.numberOfTurnsTakenTextView);
        TextView recordTextView = findViewById(R.id.currentRecordTurnsTextView);
        resultsTextView.setText(String.valueOf(numberOfTurns));
        recordTextView.setText(recordText);
        resultsLayout.setVisibility(View.VISIBLE);
        resultsLayout.startAnimation(resultsDropInAnimation);
    }


    public void setTitleWithTurns(int turn){
        String turnsWithTitle = getString(R.string.turn) + turn;
       actionBar.setTitle(turnsWithTitle);
    }


    public void setGameOverTitle(){
        actionBar.setTitle(getString(R.string.game_over));
    }


    public void setPlainTitle(){
        actionBar.setTitle(getString(R.string.app_name));
    }


    private void dismissResults(){
        if(isReadyToDismissResults) {
            isReadyToDismissResults = false;
            resultsLayout.clearAnimation();
            resultsLayout.startAnimation(resultsDropOutAnimation);
        }
    }

}