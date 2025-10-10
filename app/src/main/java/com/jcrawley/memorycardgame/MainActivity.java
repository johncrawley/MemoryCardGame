package com.jcrawley.memorycardgame;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcrawley.memorycardgame.animation.AnimationHelper;
import com.jcrawley.memorycardgame.animation.AnimationManager;
import com.jcrawley.memorycardgame.card.Card;
import com.jcrawley.memorycardgame.card.CardTypeSetter;
import com.jcrawley.memorycardgame.card.DeckSize;
import com.jcrawley.memorycardgame.card.CardBackManager;
import com.jcrawley.memorycardgame.game.CardAnimator;
import com.jcrawley.memorycardgame.game.CardLayoutManager;
import com.jcrawley.memorycardgame.service.Game;
import com.jcrawley.memorycardgame.service.GameService;
import com.jcrawley.memorycardgame.utils.AppearanceSetter;
import com.jcrawley.memorycardgame.utils.BitmapLoader;
import com.jcrawley.memorycardgame.dialog.FragmentManagerHelper;
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


    public GameView getGameView(){
        return gameView;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupInsetPadding();
        initLayouts();
        viewModel  = new ViewModelProvider(this).get(MainViewModel.class);
        initHelperClasses();
        gameView = new GameViewImpl(MainActivity.this);
        setupOptionsButton();
        setupGameService();
        initBackgroundClickListener();
        AppearanceSetter.setSavedAppearance(MainActivity.this, cardBackManager, viewModel);
    }


    private void initHelperClasses(){
        gamePreferences = new GamePreferences(MainActivity.this);
        bitmapLoader = new BitmapLoader(MainActivity.this, viewModel);
        cardBackManager = new CardBackManager(viewModel, bitmapLoader);
        cardLayoutManager = new CardLayoutManager(MainActivity.this);
        cardAnimator = new CardAnimator(screenWidth, getApplicationContext());
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
        return viewModel.cardDeckImages;
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
                cardAnimator = new CardAnimator(screenWidth, getApplicationContext());
                gameView.init(cardLayoutManager, cardAnimator);
            }});
    }


    public ViewGroup getCardLayout(){
        return cardLayout;
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


    public void displayResults(int numberOfTurns, int currentRecord){
            String recordText =
                    numberOfTurns < currentRecord ? getString(R.string.results_status_new_record)
                            : numberOfTurns == currentRecord ? getString(R.string.results_status_matching_record)
                                : getString(R.string.results_status_current_record) + currentRecord;

            displayResults(numberOfTurns, recordText);
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


    public MainViewModel getViewModel(){
        return viewModel;
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


    private void showNewGameLayout(){
        if(isShowingNewGameDialogue){
            return;
        }
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


    private void setCardFaceDown(ImageView imageView){
        cardBackManager.setCardBackOf(imageView);
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