package com.jcrawley.memorycardgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;


import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcrawley.memorycardgame.animation.AnimationManager;
import com.jcrawley.memorycardgame.background.BackgroundFactory;
import com.jcrawley.memorycardgame.card.Card;
import com.jcrawley.memorycardgame.card.DeckSize;
import com.jcrawley.memorycardgame.card.cardType.CardType;
import com.jcrawley.memorycardgame.card.CardBackManager;
import com.jcrawley.memorycardgame.game.CardLayoutPopulator;
import com.jcrawley.memorycardgame.game.Game;
import com.jcrawley.memorycardgame.list.BackgroundRecyclerAdapter;
import com.jcrawley.memorycardgame.list.CardTypeRecyclerAdapter;
import com.jcrawley.memorycardgame.utils.BitmapLoader;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private int screenWidth, screenHeight;
    private LinearLayout resultsLayout, newGameLayout, cardLayout, cardLayoutHolder;
    private ConstraintLayout settingsLayout, aboutLayout;
    private Game game;
    private boolean isReadyToDismissResults = false;
    private DeckSize deckSize;
    private int currentCardCount;
    private int currentFadeOutCount = 0;
    private boolean isShowingNewGameDialogue;
    private boolean isShowingAboutDialogue;
    private boolean isShowingSettingsDialogue;
    private MainViewModel viewModel;
    private GamePreferences gamePreferences;
    private BitmapLoader bitmapLoader;
    private CardBackManager cardBackManager;
    private AnimationManager animationManager;
    private TextView statusText;
    private ViewGroup mainLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor();
        setContentView(R.layout.activity_main);
        setupInsetPadding();
        mainLayout = findViewById(R.id.mainLayout);
        gamePreferences = new GamePreferences(MainActivity.this);
        initButtons();
        initLayouts();
        statusText = findViewById(R.id.statusText);
        viewModel  = new ViewModelProvider(this).get(MainViewModel.class);
        bitmapLoader = new BitmapLoader(MainActivity.this, viewModel);
        cardBackManager = new CardBackManager(viewModel, bitmapLoader);
        setupSettings();
    }


    private void setupInsetPadding(){
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    @Override
    public void onResume(){
        super.onResume();
        initCardsAfterLayoutCreation();
    }


    private void initCardsAfterLayoutCreation(){
        cardLayout.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                cardLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                assignScreenDimensions();
                animationManager = new AnimationManager(MainActivity.this, screenHeight);
                cardLayout.removeAllViewsInLayout();
                game = new Game(MainActivity.this, cardBackManager, bitmapLoader, screenWidth);
                CardLayoutPopulator cardLayoutPopulator = new CardLayoutPopulator(MainActivity.this, cardLayout, game, cardBackManager);
                game.initCards(cardLayoutPopulator);
                showNewGameIfAllCardsGone();
            }});
    }


    public void setBackground(int drawableId, int backgroundIndex){
        Drawable background = AppCompatResources.getDrawable(MainActivity.this, drawableId);
        mainLayout.setBackground(background);
       // cardLayoutHolder.setBackground(background);
        gamePreferences.saveInt(GamePreferences.PREF_NAME_BACKGROUND_INDEX, backgroundIndex);
    }


    private void showNewGameIfAllCardsGone(){
        if(viewModel.cards == null){
            return;
        }
        for(Card card : viewModel.cards){
            if(card.isVisible()){
                return;
            }
        }
        showNewGameLayout();
    }


    private void initLayouts(){
        cardLayout = findViewById(R.id.cardLayout);
        cardLayoutHolder = findViewById(R.id.cardLayoutHolder);
        newGameLayout = findViewById(R.id.new_game_include);
        aboutLayout = findViewById(R.id.about_include);
        settingsLayout = findViewById(R.id.settings_include);
        resultsLayout = findViewById(R.id.game_over_include);
        resultsLayout.setOnClickListener(view -> dismissResults());
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


    public GamePreferences getGamePreferences(){
        return this.gamePreferences;
    }


    private void setupSettings(){
        setupFaceTypesRecyclerView();
        setupBackTypesRecyclerView();
        setupBackgroundRecyclerView();
    }


    private void setupFaceTypesRecyclerView(){
        RecyclerView cardFacesRecyclerView = findViewById(R.id.cardTypeRecycleView);
        CardTypeRecyclerAdapter cardTypeRecyclerAdapter = new CardTypeRecyclerAdapter(getCardFaceTypes(),
                bitmapLoader,
                getViewModel().cardDeckImages,
                ()-> bitmapLoader.clearCardFaceCache());

        cardTypeRecyclerAdapter.init(cardFacesRecyclerView,
                MainActivity.this,
                gamePreferences,
                GamePreferences.PREF_NAME_CARD_FACE_INDEX);
    }


    private List<CardType> getCardFaceTypes(){
        List<CardType> cardFaceTypes = new ArrayList<>();
        for(CardType cardType : CardType.values()){
            if(!cardType.isCardBack()){
                cardFaceTypes.add(cardType);
            }
        }
        return cardFaceTypes;
    }


    public void onResultsDialogShown(){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> isReadyToDismissResults=true, getResources().getInteger(R.integer.enable_dismiss_results_delay));
    }


    private void setupBackTypesRecyclerView(){
        RecyclerView cardBacksRecyclerView = findViewById(R.id.cardBackRecycleView);

        CardTypeRecyclerAdapter cardTypeRecyclerAdapter = new CardTypeRecyclerAdapter(cardBackManager.getSelectableCardBackTypes(),
                bitmapLoader,
                cardBackManager,
                ()->{
                    game.switchBacksOnFaceDownCards();
                    bitmapLoader.clearCardBackCache();
                } );
        cardTypeRecyclerAdapter.init(cardBacksRecyclerView, MainActivity.this, gamePreferences, GamePreferences.PREF_NAME_CARD_BACK_INDEX);
    }


    private void setupBackgroundRecyclerView(){
        RecyclerView backgroundRecyclerView = settingsLayout.findViewById(R.id.backgroundRecyclerView);
        BackgroundRecyclerAdapter backgroundRecyclerAdapter = new BackgroundRecyclerAdapter(BackgroundFactory.getAll());
        backgroundRecyclerAdapter.init(backgroundRecyclerView, MainActivity.this, gamePreferences.getInt(GamePreferences.PREF_NAME_BACKGROUND_INDEX));
    }


    private void showAboutView(){
        dismissSettingsDialog();
        if(isShowingAboutDialogue){
            return;
        }
        aboutLayout.setVisibility(View.VISIBLE);
        isShowingAboutDialogue = true;
        aboutLayout.startAnimation(animationManager.getAboutDialogDropInAnimation());
    }


    private void showSettingsView(){
        if(isShowingSettingsDialogue){
            return;
        }
        dismissAboutDialog();
        settingsLayout.setVisibility(View.VISIBLE);
        isShowingSettingsDialogue = true;
        settingsLayout.startAnimation(animationManager.getSettingsDropInAnimation());
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
        cardLayout.startAnimation(animationManager.getCardsFadeOutAnimation());
    }


    public void onCardsFadedOut(){
        for(View v : cardLayout.getTouchables()){
            v.setVisibility(View.GONE);
        }
        cardLayout.setVisibility(View.VISIBLE);
        cardLayout.clearAnimation();
        currentFadeOutCount++;
        checkToRemoveAllCardsFromLayout();
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
        aboutLayout.startAnimation(animationManager.getAboutDialogDropOutAnimation());
    }


    public void dismissSettingsDialog(){
        if(!isShowingSettingsDialogue){
            return;
        }
        isShowingSettingsDialogue = false;
        settingsLayout.clearAnimation();
        settingsLayout.startAnimation(animationManager.getSettingsDropOutAnimation());
    }


    public void onSettingsDialogDismissed(){
        settingsLayout.clearAnimation();
        settingsLayout.setVisibility(View.INVISIBLE);
    }


    public void onAboutDialogDismissed(){
        aboutLayout.clearAnimation();
        aboutLayout.setVisibility(View.INVISIBLE);
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
        gamePreferences.saveNumberOfCards(deckSize.getValue());
        isShowingNewGameDialogue = false;
        newGameLayout.clearAnimation();
        newGameLayout.setVisibility(View.VISIBLE);
        newGameLayout.startAnimation(animationManager.getNewGameDropOutAnimation());
    }


    public void onResultsDismissed(){
        resultsLayout.clearAnimation();
        resultsLayout.setVisibility(View.GONE);
        showNewGameLayout();
    }


    public void onNewGameScreenDismissed(){
        newGameLayout.clearAnimation();
        newGameLayout.setVisibility(View.GONE);
        game.startAgain(deckSize);
        setPlainTitle();
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
        newGameLayout.startAnimation(animationManager.getNewGameDropInAnimation());
    }


    private void dismissResultsLayoutIfVisible(){
        if(resultsLayout.getVisibility() == View.VISIBLE){
            resultsLayout.startAnimation(animationManager.getResultsDropOutAnimation());
        }
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
        resultsLayout.startAnimation(animationManager.getResultsDropInAnimation());
    }


    public void setTitleWithTurns(int turn){
        String turnsWithTitle = getString(R.string.turn) + turn;
        statusText.setText(turnsWithTitle);
       //actionBar.setTitle(turnsWithTitle);
    }


    public void setGameOverTitle(){
        //actionBar.setTitle(getString(R.string.game_over));
    }


    public void setPlainTitle(){

        //actionBar.setTitle(getString(R.string.app_name));
    }


    private void dismissResults(){
        if(isReadyToDismissResults) {
            isReadyToDismissResults = false;
            resultsLayout.clearAnimation();
            resultsLayout.startAnimation(animationManager.getResultsDropOutAnimation());
        }
    }

}