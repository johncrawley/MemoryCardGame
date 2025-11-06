package com.jcrawley.memorycardgame;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcrawley.memorycardgame.game.Game;
import com.jcrawley.memorycardgame.view.GamePreferences;
import com.jcrawley.memorycardgame.view.MainViewModel;
import com.jcrawley.memorycardgame.view.animation.AnimationHelper;
import com.jcrawley.memorycardgame.view.animation.AnimationManager;
import com.jcrawley.memorycardgame.card.CardTypeSetter;
import com.jcrawley.memorycardgame.card.DeckSize;
import com.jcrawley.memorycardgame.card.CardBackManager;
import com.jcrawley.memorycardgame.view.animation.CardAnimator;
import com.jcrawley.memorycardgame.view.CardLayoutManager;
import com.jcrawley.memorycardgame.view.utils.AppearanceSetter;
import com.jcrawley.memorycardgame.view.utils.BitmapLoader;
import com.jcrawley.memorycardgame.view.dialog.FragmentManagerHelper;
import com.jcrawley.memorycardgame.view.GameViewImpl;
import com.jcrawley.memorycardgame.view.GameView;


public class MainActivity extends AppCompatActivity {

    private int screenWidth, screenHeight;
    private LinearLayout resultsLayout;
    private LinearLayout newGameLayout;
    private LinearLayout cardLayout;
    private boolean isReadyToDismissResults;
    private boolean isShowingNewGameDialogue;
    private MainViewModel viewModel;
    private GamePreferences gamePreferences;
    private BitmapLoader bitmapLoader;
    private CardBackManager cardBackManager;
    private AnimationManager animationManager;
    private TextView statusText;
    private ViewGroup mainLayout;
    private ViewGroup statusPanel;
    private CardAnimator cardAnimator;
    private CardLayoutManager cardLayoutManager;
    private GameView gameView;
    private Game game;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewModel();
        setupInsetPadding();
        configureNavAndStatusBarAppearance();
        initLayouts();
        initHelperClasses();
        initGameView();
        setupOptionsButton();
        initBackgroundClickListener();
        AppearanceSetter.setSavedAppearance(MainActivity.this, cardBackManager, viewModel);
    }


    private void initViewModel(){
        viewModel  = new ViewModelProvider(this).get(MainViewModel.class);

    }

    private void initGame(){
        game = new Game(viewModel.gameModel, gameView, new GamePreferences(getApplicationContext()));
    }


    private void setupInsetPadding(){
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void configureNavAndStatusBarAppearance(){
        var window = getWindow();
        var insetsController = WindowCompat.getInsetsController(window, window.getDecorView());
        insetsController.setAppearanceLightNavigationBars(false);
        insetsController.setAppearanceLightStatusBars(false);
    }


    private void initLayouts(){
        mainLayout = findViewById(R.id.mainLayout);
        statusPanel = findViewById(R.id.statusPanelInclude);
        cardLayout = findViewById(R.id.cardLayout);
        newGameLayout = findViewById(R.id.new_game_include);
        resultsLayout = findViewById(R.id.game_over_include);
        resultsLayout.setOnClickListener(view -> dismissResults());
        initStartGameButtons();
    }


    private void initHelperClasses(){
        gamePreferences = new GamePreferences(MainActivity.this);
        bitmapLoader = new BitmapLoader(MainActivity.this, viewModel);
        cardBackManager = new CardBackManager(viewModel, bitmapLoader);
        cardLayoutManager = new CardLayoutManager(getApplicationContext(), cardBackManager, cardLayout);
        cardAnimator = new CardAnimator(getApplicationContext());
    }

    private void initGameView(){
        gameView = new GameViewImpl(MainActivity.this, viewModel.cardFaceImages);
        gameView.init(cardLayoutManager, cardAnimator);
    }


    private void setupOptionsButton(){
        ImageButton optionsButton = findViewById(R.id.optionsButton);
        optionsButton.setOnClickListener(v -> {
            if(viewModel.isOptionsButtonEnabled.get()){
                FragmentManagerHelper.showOptionsDialog(MainActivity.this);
                viewModel.isOptionsButtonEnabled.set(false);
            }
        });
    }


    public GameView getGameView(){
        return gameView;
    }


    public Game getGame(){
        return game;
    }


    public CardBackManager getCardBackManager(){
        return cardBackManager;
    }


    public BitmapLoader getBitmapLoader(){
        return bitmapLoader;
    }


    public CardTypeSetter getCardTypeSetter(){
        return viewModel.cardFaceImages;
    }


    public GamePreferences getGamePreferences(){
        return this.gamePreferences;
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
                cardAnimator.setScreenWidth(screenWidth);
                initGame();
            }});
    }


    private void initBackgroundClickListener(){
        ViewGroup background = findViewById(R.id.cardLayoutHolder);
        if(background != null){
            background.setOnClickListener(v -> game.immediatelyFlipBackBothCardsIfNoMatch());
        }
    }


    public void displayResults(int numberOfTurns, int currentRecord, int delay){
        final int actualDelay = Math.max(0, delay);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            String recordText = getRecordTextFor(numberOfTurns, currentRecord);
            TextView resultsTextView = findViewById(R.id.numberOfTurnsTakenTextView);
            TextView recordTextView = findViewById(R.id.currentRecordTurnsTextView);
            resultsTextView.setText(String.valueOf(numberOfTurns));
            recordTextView.setText(recordText);
            resultsLayout.setVisibility(VISIBLE);
            hideStatusPanel();
            if(actualDelay > 0){
                resultsLayout.startAnimation(animationManager.getResultsDropInAnimation());
            }
            else{
                onGameOverDialogShown();
            }
        }, actualDelay);
    }


    private String getRecordTextFor(int numberOfTurns, int currentRecord){
        return numberOfTurns < currentRecord ? getString(R.string.results_status_new_record)
                : numberOfTurns == currentRecord ? getString(R.string.results_status_matching_record)
                : getString(R.string.results_status_current_record) + currentRecord;
    }


    public void setAndSaveBackground(int drawableId, int backgroundIndex){
        Drawable background = AppCompatResources.getDrawable(MainActivity.this, drawableId);
        mainLayout.setBackground(background);
        gamePreferences.saveInt(GamePreferences.PREF_NAME_BACKGROUND_INDEX, backgroundIndex);
    }


    public void setBackground(Drawable drawable){
        mainLayout.setBackground(drawable);
    }


    private void removeAllCards(){
        if(cardLayout != null
                && cardLayout.getVisibility() == VISIBLE
                && animationManager != null){
            cardLayout.startAnimation(animationManager.getCardsFadeOutAnimation());
        }
    }


    public void onGameOverDialogShown(){
        new Handler(Looper.getMainLooper())
                .postDelayed(() -> isReadyToDismissResults = true, getResources().getInteger(R.integer.enable_dismiss_results_delay));
    }


    public void onCardsFadedOut(){
        for(View v : cardLayout.getTouchables()){
            v.setVisibility(View.GONE);
        }
        cardLayout.setVisibility(VISIBLE);
        cardLayout.clearAnimation();
        cardLayout.removeAllViewsInLayout();
    }


    private void initStartGameButtons(){
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
        game.startAgain();
    }


    public void dropInNewGameDialog(){
        removeAllCards();
        showNewGameLayout();
        hideStatusPanel();
    }


    public void setNewGameDialogVisible(){
        statusPanel.setVisibility(INVISIBLE);
        dismissResultsLayoutIfVisible();
        newGameLayout.setVisibility(VISIBLE);
        isShowingNewGameDialogue = true;
    }


    public void enableOptionsButton(){
        if(viewModel != null){
            viewModel.isOptionsButtonEnabled.set(true);
        }
    }


    private void showNewGameLayout(){
        if(isShowingNewGameDialogue){
            return;
        }
        isShowingNewGameDialogue = true;
        dismissResultsLayoutIfVisible();
        newGameLayout.setVisibility(VISIBLE);
        if(animationManager != null){
            var animation = animationManager.getNewGameDropInAnimation();
            newGameLayout.startAnimation(animation);
        }
        notifyGameOfNewGameDialogPresence();
    }


    private void notifyGameOfNewGameDialogPresence(){
        game.onNewGameLayoutShown();
    }


    private void dismissResultsLayoutIfVisible(){
        if(resultsLayout.getVisibility() == VISIBLE){
            resultsLayout.startAnimation(animationManager.getResultsDropOutAnimation());
        }
    }


    private void assignScreenDimensions() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
    }


    public int getScreenWidth(){
        return screenWidth;
    }


    public int getScreenHeight(){
        return screenHeight;
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


    private void dismissResults(){
        if(isReadyToDismissResults) {
            isReadyToDismissResults = false;
            resultsLayout.clearAnimation();
            resultsLayout.startAnimation(animationManager.getResultsDropOutAnimation());
        }
    }

}