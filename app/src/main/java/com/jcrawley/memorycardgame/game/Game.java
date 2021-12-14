package com.jcrawley.memorycardgame.game;

import android.animation.Animator;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.jcrawley.memorycardgame.BitmapLoader;
import com.jcrawley.memorycardgame.MainActivity;
import com.jcrawley.memorycardgame.MainViewModel;
import com.jcrawley.memorycardgame.R;
import com.jcrawley.memorycardgame.card.Card;
import com.jcrawley.memorycardgame.card.CardFactory;
import com.jcrawley.memorycardgame.card.DeckSize;

import java.util.Collections;

public class Game {

    private ImageView firstSelectedCard, secondSelectedCard;
    private final BitmapLoader bitmapLoader;
    private CardLayoutPopulator cardLayoutPopulator;
    private final CardAnimator cardAnimator;
    private final RecordKeeper recordKeeper;
    private final MainActivity mainActivity;
    private final Context context;
    private Handler handler;
    private final MainViewModel viewModel;
    private boolean isFirstRunSinceCreate;
    private final CardBackManager cardBackManager;


    public Game(MainActivity mainActivity, CardBackManager cardBackManager, BitmapLoader bitmapLoader, int screenWidth){
        this.mainActivity = mainActivity;
        this.cardBackManager = cardBackManager;
        isFirstRunSinceCreate = true;
        viewModel = mainActivity.getViewModel();
        context = mainActivity.getApplicationContext();
        initHandler();
        this.recordKeeper = new RecordKeeper(context);
        this.bitmapLoader = bitmapLoader;
        initModel();
        cardAnimator = new CardAnimator(screenWidth, context);
    }


    private void initModel(){
        if(viewModel.isAlreadyInitialised){
            mainActivity.setTitleWithTurns(viewModel.numberOfTurns);
            return;
        }
        viewModel.isAlreadyInitialised = true;
        viewModel.deck = CardFactory.createDecks();
        viewModel.cards = viewModel.deck.get(DeckSize.SIXTEEN);
        viewModel.gameState = GameState.NOTHING_SELECTED;
        assert viewModel.cards != null;
        viewModel.numberOfCards = viewModel.cards.size();
        viewModel.remainingCards = viewModel.numberOfCards;
        shuffleCards();
    }


    public void shuffleCards(){
        log("Entered shuffleCards()");
        Collections.shuffle(viewModel.cards);
    }


    public int getNumberOfCards(){
        return viewModel.numberOfCards;
    }


    public void initCards(CardLayoutPopulator cardLayoutPopulator){
        if(isFirstRunSinceCreate){
            this.cardLayoutPopulator = cardLayoutPopulator;
            cardLayoutPopulator.addCardViews();
            if(viewModel.gameState == GameState.FIRST_CARD_SELECTED){
                quickFlipFirstSelectedCard();
            }
        }
        isFirstRunSinceCreate = false;
    }


    private void quickFlipFirstSelectedCard(){
        log("Entered quickFlipFirstSelectedCard()");
        firstSelectedCard = cardLayoutPopulator.getImageViews().get(viewModel.firstSelectedPosition);
        setBitmapForCard(firstSelectedCard, viewModel.firstSelectedPosition);
        firstSelectedCard.animate().rotationY(180).setDuration(1).start();
    }


    public void notifyClickOnPosition(ImageView view){
        int position = (int)view.getTag(R.string.position_tag);
        mainActivity.dismissAboutDialog();
        if(viewModel.gameState == GameState.NOTHING_SELECTED){
            handleFirstSelection(view, position);
        }
        else if(viewModel.gameState == GameState.FIRST_CARD_SELECTED) {
            handleSecondSelection(view, position);
        }
    }


    private void handleFirstSelection(ImageView view, int position){
        log("Entered handleFirstSelection");
        mainActivity.setTitleWithTurns(++viewModel.numberOfTurns);
        viewModel.gameState = GameState.FIRST_CARD_SELECTED;
        viewModel.firstSelectedPosition = position;
        firstSelectedCard = view;
        flipCard(position);
        flipOver(firstSelectedCard, position, false);
    }


    private void handleSecondSelection(ImageView view, int position){
        log("Entered handleSecondSelection");
            if(position == viewModel.firstSelectedPosition){
                return;
            }
            viewModel.gameState = GameState.SECOND_CARD_SELECTED;
            viewModel.secondSelectedPosition = position;
            flipCard(position);
            secondSelectedCard = view;
            flipOver(secondSelectedCard, position, true);
    }


    private void flipCard(int position){
        viewModel.cards.get(position).flipCard();
    }


    private boolean matches(){
        Card card1 = viewModel.cards.get(viewModel.firstSelectedPosition);
        Card card2 = viewModel.cards.get(viewModel.secondSelectedPosition);
        return card1.getRank() == card2.getRank();
    }


    private String getStr(int resId){
        return context.getString(resId);
    }


    private void removeSelectedCards(){
        Handler handler = new Handler(Looper.getMainLooper());
        cardAnimator.addSwipeOutAnimationTo(firstSelectedCard);
        cardAnimator.addSwipeOutAnimationTo(secondSelectedCard);
        viewModel.cards.get(viewModel.firstSelectedPosition).setVisible(false);
        viewModel.cards.get(viewModel.secondSelectedPosition).setVisible(false);
        handler.postDelayed(() -> {
            viewModel.remainingCards -=2;
            viewModel.gameState = GameState.NOTHING_SELECTED;
            if(viewModel.remainingCards <= 0){
                displayResults();
            }
        }, 1000);
    }


    private void displayResults(){
        String recordText;
        int currentRecord = recordKeeper.getCurrentTurnsRecordFromPreferences(viewModel.numberOfCards);
        if(viewModel.numberOfTurns < currentRecord){
            recordText = getStr(R.string.results_status_new_record);
            recordKeeper.saveNewTurnsRecord(viewModel.numberOfTurns, viewModel.numberOfCards);
        }
        else if(viewModel.numberOfTurns == currentRecord){
            recordText = getStr(R.string.results_status_matching_record);
        }
        else{
            recordText = getStr(R.string.results_status_current_record) + currentRecord;
        }
        mainActivity.displayResults(viewModel.numberOfTurns, recordText);
    }


    public void startAgain(DeckSize deckSize){
        viewModel.numberOfTurns = 0;
        viewModel.numberOfCards = deckSize.getValue();
        viewModel.remainingCards = viewModel.numberOfCards;
        viewModel.cards = viewModel.deck.get(deckSize);
        cardLayoutPopulator.addCardViews(viewModel.numberOfCards);
        viewModel.gameState = GameState.NOTHING_SELECTED;
        if(viewModel.cards == null){
            return;
        }
        Collections.shuffle(viewModel.cards);
        for(Card card : viewModel.cards){
            card.init();
        }
        setAllCardsFaceDown();
        int initialDelay = mainActivity.getResources().getInteger(R.integer.swipe_in_cards_initial_delay);
        new Handler(Looper.getMainLooper()).postDelayed(this::swipeInCards, initialDelay);
    }


    private void swipeInCards(){
        cardAnimator.swipeInAll(cardLayoutPopulator.getImageViews());
    }


    private void turnOverCards(){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            flipCardBack(firstSelectedCard);
            flipCardBack(secondSelectedCard);
            viewModel.gameState = GameState.NOTHING_SELECTED;
        }, getInt(R.integer.flip_cards_back_delay));
    }


    private void setAllCardsFaceDown(){
        for(ImageView card : cardLayoutPopulator.getImageViews()){
            setCardFaceDown(card);
        }
    }


    private void setCardFaceDown(ImageView imageView){
        cardBackManager.setCardBackTo(imageView);
    }


    private void flipOver(ImageView cardView, int position, boolean isSecondCardSelected){
        viewModel.currentPosition = position;
        flipCardView(cardView, isSecondCardSelected);
    }


    private void flipCardView(ImageView cardView, boolean isSecondCard) {
        Animator.AnimatorListener halfWayFlip = createAnimatorListener(() -> onFinishedHalfFlip(cardView, isSecondCard));
        animateCardFlip(cardView, 1, halfWayFlip);
    }


    private void animateCardFlip(ImageView cardView, int rotationMultiplier, Animator.AnimatorListener onFinishedListener){
        long duration = getInt(R.integer.flip_card_duration);
        float halfRotation = getInt(R.integer.flip_card_half_rotation);
        cardView.animate()
                .rotationY(halfRotation * rotationMultiplier)
                .setDuration(duration)
                .setListener(onFinishedListener)
                .start();
    }


    private void onFinishedHalfFlip(ImageView cardView, boolean isSecondCard){
        Animator.AnimatorListener fullWayFlippedListener = createAnimatorListener(() ->{
            cardView.clearAnimation();
            checkCards(isSecondCard);
        });
        cardView.clearAnimation();
        setBitmapForCard(cardView, viewModel.currentPosition);
        animateCardFlip(cardView, 2, fullWayFlippedListener);
    }


    private void checkCards(boolean hasSecondCardBeenTurnedOver){
        if(!hasSecondCardBeenTurnedOver || firstSelectedCard == null || secondSelectedCard == null){
            return;
        }
        if(matches()){
            removeSelectedCards();
        }
        else{
            turnOverCards();
        }
    }


    private void flipCardBack(ImageView cardView) {
        Animator.AnimatorListener onFullWayFlippedBack = createAnimatorListener(cardView::clearAnimation);
        Animator.AnimatorListener onHalfWayFlippedBack = createAnimatorListener( () -> onHalfWayFlippedBack(cardView, onFullWayFlippedBack));
        animateCardFlip(cardView, 1, onHalfWayFlippedBack);
    }


    private void onHalfWayFlippedBack(ImageView cardView, Animator.AnimatorListener fullWayFlippedBackListener){
        if(viewModel.cards == null){
            return;
        }
        setBitmapForCard(cardView, viewModel.currentPosition);
        setCardFaceDown(cardView);
        animateCardFlip(cardView, 0, fullWayFlippedBackListener);
    }


    private void setBitmapForCard(ImageView cardView, int position){
        bitmapLoader.setBitmap(cardView, getImageIdAt(position));
    }


    private int getImageIdAt(int position){
       return viewModel.cards.get(position).getImageId();
    }


    private void initHandler(){
        handler = new Handler(Looper.getMainLooper());
    }


    private Animator.AnimatorListener createAnimatorListener(Runnable onFinished){
        return new Animator.AnimatorListener() {
            public void onAnimationEnd(Animator animator) {
                handler.post(onFinished);
            }
            public void onAnimationStart(Animator animator) {}
            public void onAnimationCancel(Animator animator) {}
            public void onAnimationRepeat(Animator animator) {}
        };
    }


    private int getInt(int resId){
        return mainActivity.getResources().getInteger(resId);
    }


    private void log(String msg){
        System.out.println("^^^ Game: " + msg);
    }
}
