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

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
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

import com.jcrawley.memorycardgame.view.GamePreferences;
import com.jcrawley.memorycardgame.view.MainViewModel;
import com.jcrawley.memorycardgame.view.animation.AnimationHelper;
import com.jcrawley.memorycardgame.view.animation.AnimationManager;
import com.jcrawley.memorycardgame.card.CardTypeSetter;
import com.jcrawley.memorycardgame.card.DeckSize;
import com.jcrawley.memorycardgame.card.CardBackManager;
import com.jcrawley.memorycardgame.view.animation.CardAnimator;
import com.jcrawley.memorycardgame.service.game.CardLayoutManager;
import com.jcrawley.memorycardgame.service.game.Game;
import com.jcrawley.memorycardgame.service.GameService;
import com.jcrawley.memorycardgame.view.utils.AppearanceSetter;
import com.jcrawley.memorycardgame.view.utils.BitmapLoader;
import com.jcrawley.memorycardgame.view.dialog.FragmentManagerHelper;
import com.jcrawley.memorycardgame.view.GameViewImpl;
import com.jcrawley.memorycardgame.view.GameView;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public class MainActivity extends AppCompatActivity {

    private int screenWidth, screenHeight;
    private LinearLayout resultsLayout;
    private LinearLayout newGameLayout;
    private LinearLayout cardLayout;
    private boolean isReadyToDismissResults = false;
    private boolean isShowingNewGameDialogue;
    private MainViewModel viewModel;
    private GamePreferences gamePreferences;
    private BitmapLoader bitmapLoader;
    private CardBackManager cardBackManager;
    private AnimationManager animationManager;
    private TextView statusText;
    private ViewGroup mainLayout;
    private ViewGroup statusPanel;
    private GameService gameService;
    private final AtomicBoolean isServiceConnected = new AtomicBoolean();
    private CardAnimator cardAnimator;
    private CardLayoutManager cardLayoutManager;
    private GameView gameView;


    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            GameService.LocalBinder binder = (GameService.LocalBinder) service;
            MainActivity.this.gameService = binder.getService();
            gameService.setActivity(MainActivity.this);
            isServiceConnected.set(true);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isServiceConnected.set(false);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewModel();
        setupInsetPadding();
        initLayouts();
        initHelperClasses();
        initGameView();
        setupOptionsButton();
        setupGameService();
        initBackgroundClickListener();
        AppearanceSetter.setSavedAppearance(MainActivity.this, cardBackManager, viewModel);
    }


    private void initGameView(){
        gameView = new GameViewImpl(MainActivity.this, viewModel.cardFaceImages);
    }


    private void initHelperClasses(){
        gamePreferences = new GamePreferences(MainActivity.this);
        bitmapLoader = new BitmapLoader(MainActivity.this, viewModel);
        cardBackManager = new CardBackManager(viewModel, bitmapLoader);
        cardLayoutManager = new CardLayoutManager(MainActivity.this);
        cardAnimator = new CardAnimator(screenWidth, getApplicationContext());
    }


    private void initViewModel(){
        viewModel  = new ViewModelProvider(this).get(MainViewModel.class);
    }


    public MainViewModel getViewModel(){
        return viewModel;
    }


    private void setupGameService() {
        Intent intent = new Intent(getApplicationContext(), GameService.class);
        getApplicationContext().startService(intent);
        getApplicationContext().bindService(intent, connection, 0);
    }


    private void setupOptionsButton(){
        ImageButton optionsButton = findViewById(R.id.optionsButton);
        optionsButton.setOnClickListener(v -> FragmentManagerHelper.showOptionsDialog(MainActivity.this));
    }


    public GameView getGameView(){
        return gameView;
    }


    public GameService getGameService(){
        return gameService;
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


    public ViewGroup getCardLayout(){
        return cardLayout;
    }


    public GamePreferences getGamePreferences(){
        return this.gamePreferences;
    }


    private void setupInsetPadding(){
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        var window = getWindow();
        var insetsController = WindowCompat.getInsetsController(window, window.getDecorView());
        insetsController.setAppearanceLightNavigationBars(false);
        insetsController.setAppearanceLightStatusBars(false);
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
                cardAnimator = new CardAnimator(screenWidth, getApplicationContext());
                gameView.init(cardLayoutManager, cardAnimator);
            }});
    }


    private void initBackgroundClickListener(){
        ViewGroup background = findViewById(R.id.cardLayoutHolder);
        if(background == null){
            return;
        }
        background.setOnClickListener(v -> {
            if(gameService != null){
                Game game = gameService.getGame();
                game.immediatelyFlipBackBothCardsIfNoMatch();
            }
        });
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


    public void setCardRows(List<ViewGroup> cardRows){
        runOnUiThread(()->{
            for(ViewGroup cardRow : cardRows){
                cardLayout.addView(cardRow);
            }
        });
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


    private void log(String msg){
        System.out.println("^^^ MainActivity: " + msg);
    }


    private void removeAllCards(){
        if(cardLayout != null
                && cardLayout.getVisibility() == VISIBLE
                && animationManager != null){
            cardLayout.startAnimation(animationManager.getCardsFadeOutAnimation());
        }
    }


    public void onGameOverDialogShown(){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> isReadyToDismissResults=true, getResources().getInteger(R.integer.enable_dismiss_results_delay));
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
        if(gameService != null){
            gameService.getGame().startAgain();
        }
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
        if(isServiceConnected.get()){
            var game = gameService.getGame();
            game.onNewGameLayoutShown();
        }
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