package com.jcrawley.memorycardgame;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcrawley.memorycardgame.animation.AnimationHelper;
import com.jcrawley.memorycardgame.animation.AnimationManager;
import com.jcrawley.memorycardgame.background.Background;
import com.jcrawley.memorycardgame.background.BackgroundFactory;
import com.jcrawley.memorycardgame.card.Card;
import com.jcrawley.memorycardgame.card.CardTypeSetter;
import com.jcrawley.memorycardgame.card.DeckSize;
import com.jcrawley.memorycardgame.card.CardBackManager;
import com.jcrawley.memorycardgame.game.CardLayoutPopulator;
import com.jcrawley.memorycardgame.game.Game;
import com.jcrawley.memorycardgame.utils.BitmapLoader;
import com.jcrawley.memorycardgame.dialog.FragmentManagerHelper;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private int screenWidth, screenHeight;
    private LinearLayout resultsLayout;
    private LinearLayout newGameLayout;
    private LinearLayout cardLayout;
    private Game game;
    private boolean isReadyToDismissResults = false;
    private DeckSize deckSize;
    private int currentCardCount;
    private int currentFadeOutCount = 0;
    private boolean isShowingNewGameDialogue;
    private MainViewModel viewModel;
    private GamePreferences gamePreferences;
    private BitmapLoader bitmapLoader;
    private CardBackManager cardBackManager;
    private AnimationManager animationManager;
    private TextView statusText;
    private ViewGroup mainLayout;
    private ViewGroup statusPanel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor();
        setContentView(R.layout.activity_main);
        setupInsetPadding();
        mainLayout = findViewById(R.id.mainLayout);
        statusPanel = findViewById(R.id.statusPanelInclude);
        gamePreferences = new GamePreferences(MainActivity.this);
        initButtons();
        initLayouts();
        viewModel  = new ViewModelProvider(this).get(MainViewModel.class);
        bitmapLoader = new BitmapLoader(MainActivity.this, viewModel);
        cardBackManager = new CardBackManager(viewModel, bitmapLoader);
        setupOptionsButton();
        setSavedBackground();
    }


    private void setupOptionsButton(){
        Button optionsButton = findViewById(R.id.optionsButton);
        optionsButton.setOnClickListener(v -> {
            FragmentManagerHelper.showOptionsDialog(MainActivity.this);
        });
    }


    public CardBackManager getCardBackManager(){
        return cardBackManager;
    }

    public BitmapLoader getBitmapLoader(){
        return bitmapLoader;
    }


    public CardTypeSetter getCardTypeSetter(){
        return viewModel.cardDeckImages;
    }


    public Game getGame(){
        return game;
    }

    private static void log(String msg){
        System.out.println("^^^ MainActivity : " + msg);
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


    private void setSavedBackground(){
        List<Background> backgrounds = BackgroundFactory.getAll();
        int savedBgIndex = gamePreferences.getInt(GamePreferences.PREF_NAME_BACKGROUND_INDEX);
        Background savedBackground = backgrounds.get(savedBgIndex);
        Drawable background = AppCompatResources.getDrawable(MainActivity.this, savedBackground.getResourceId());
        mainLayout.setBackground(background);
    }


    public void setBackground(int drawableId, int backgroundIndex){
        Drawable background = AppCompatResources.getDrawable(MainActivity.this, drawableId);
        mainLayout.setBackground(background);
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
        newGameLayout = findViewById(R.id.new_game_include);
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


    public void showNewGameDialog(){
        removeAllCards();
        showNewGameLayout();
        hideStatusPanel();
    }




    public GamePreferences getGamePreferences(){
        return this.gamePreferences;
    }


    public void onResultsDialogShown(){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> isReadyToDismissResults=true, getResources().getInteger(R.integer.enable_dismiss_results_delay));
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
        if(viewModel.cards != null){
            for(Card card : viewModel.cards){
                card.setVisible(false);
            }
        }
    }


    public void onCardsFadedOut(){
        for(View v : cardLayout.getTouchables()){
            v.setVisibility(View.GONE);
        }
        cardLayout.setVisibility(VISIBLE);
        cardLayout.clearAnimation();
        currentFadeOutCount++;
        checkToRemoveAllCardsFromLayout();
    }


    private void initButtons(){
        setupButton(R.id.cards8Button, DeckSize.EIGHT);
        setupButton(R.id.cards16Button, DeckSize.SIXTEEN);
        setupButton(R.id.cards26Button, DeckSize.TWENTY_SIX);
        setupButton(R.id.cards52Button, DeckSize.FIFTY_TWO);
    }


    private void setupButton(int buttonId, DeckSize deck_size){
        Button button = findViewById(buttonId);
        button.setOnClickListener(v -> startNewGame(deck_size));
    }


    private void startNewGame(DeckSize deckSize){
        this.deckSize = deckSize;
        gamePreferences.saveNumberOfCards(deckSize.getValue());
        isShowingNewGameDialogue = false;
        newGameLayout.clearAnimation();
        newGameLayout.setVisibility(VISIBLE);
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
        if(isShowingNewGameDialogue){
            return;
        }
        setPlainTitle();
        isShowingNewGameDialogue = true;
        dismissResultsLayoutIfVisible();
        newGameLayout.setVisibility(VISIBLE);
        newGameLayout.startAnimation(animationManager.getNewGameDropInAnimation());
    }


    private void dismissResultsLayoutIfVisible(){
        if(resultsLayout.getVisibility() == VISIBLE){
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
        TextView resultsTextView = findViewById(R.id.numberOfTurnsTakenTextView);
        TextView recordTextView = findViewById(R.id.currentRecordTurnsTextView);
        resultsTextView.setText(String.valueOf(numberOfTurns));
        recordTextView.setText(recordText);
        resultsLayout.setVisibility(VISIBLE);
        hideStatusPanel();
        resultsLayout.startAnimation(animationManager.getResultsDropInAnimation());
    }


    public void setTitleWithTurns(int turn){
        if(statusText == null){
            statusText = findViewById(R.id.statusText);
        }
        String turnsWithTitle = String.valueOf(turn);
        statusText.setText(turnsWithTitle);
        if(turn > 0 && statusPanel.getVisibility() != VISIBLE){
            showStatusPanel();
        }
    }


    private void showStatusPanel(){
        if(statusPanel.getVisibility() == VISIBLE){
            return;
        }
        Animation fadeInAnimation = AnimationHelper.createFadeInAnimation(MainActivity.this);
        statusPanel.clearAnimation();
        statusPanel.setAnimation(fadeInAnimation);
        statusPanel.setVisibility(VISIBLE);
        statusPanel.animate();
    }


    private void hideStatusPanel(){
        if(statusPanel.getVisibility() == INVISIBLE){
            return;
        }
        Animation fadeOutAnimation = AnimationHelper.createFadeOutAnimation(MainActivity.this, statusPanel);
        statusPanel.clearAnimation();
        statusPanel.setAnimation(fadeOutAnimation);
        statusPanel.animate();
    }


    public void setPlainTitle(){

    }


    private void dismissResults(){
        if(isReadyToDismissResults) {
            isReadyToDismissResults = false;
            resultsLayout.clearAnimation();
            resultsLayout.startAnimation(animationManager.getResultsDropOutAnimation());
        }
    }

}