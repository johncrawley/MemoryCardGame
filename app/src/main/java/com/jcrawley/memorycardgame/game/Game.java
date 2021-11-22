package com.jcrawley.memorycardgame.game;

import android.animation.Animator;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.jcrawley.memorycardgame.BitmapLoader;
import com.jcrawley.memorycardgame.MainActivity;
import com.jcrawley.memorycardgame.R;
import com.jcrawley.memorycardgame.card.Card;
import com.jcrawley.memorycardgame.card.CardFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Game {

    private List<Card> cards;
    private int firstSelectedPosition = -1;
    private int secondSelectedPosition = -1;
    private ImageView firstSelectedCard, secondSelectedCard;
    private enum GameState { NOTHING_SELECTED, FIRST_CARD_SELECTED, SECOND_CARD_SELECTED}
    private GameState gameState;
    private final BitmapLoader bitmapLoader;
    private CardLayoutPopulator cardLayoutPopulator;
    private int numberOfCards;
    private int remainingCards;
    private int numberOfTurns;
    private final CardAnimator cardAnimator;
    private final RecordKeeper recordKeeper;
    private final MainActivity mainActivity;
    private final Context context;
    private final Map<Integer, List<Card>> deck;
    private int currentPosition;


    public Game(MainActivity mainActivity, int screenWidth){
        this.mainActivity = mainActivity;
        context = mainActivity.getApplicationContext();
        this.recordKeeper = new RecordKeeper(mainActivity.getApplicationContext());
        bitmapLoader = new BitmapLoader(mainActivity.getApplicationContext());
        gameState = GameState.NOTHING_SELECTED;
        deck = CardFactory.createDeck();
        cards = deck.get(8);
        //cards = CardFactory.createCards();
        assert cards != null;
        numberOfCards = cards.size();
        remainingCards = numberOfCards;
        shuffleCards();
        cardAnimator = new CardAnimator(screenWidth);
    }


    public void shuffleCards(){
        Collections.shuffle(cards);
    }


    public int getNumberOfCards(){
        return numberOfCards;
    }


    public void setCardLayoutPopulator(CardLayoutPopulator cardLayoutPopulator){
        this.cardLayoutPopulator = cardLayoutPopulator;
    }


    public void notifyClickOnPosition(ImageView view){
        int position = (int)view.getTag(R.string.position_tag);

        if(gameState == GameState.NOTHING_SELECTED){
            handleFirstSelection(view, position);
        }
        else if(gameState == GameState.FIRST_CARD_SELECTED) {
            handleSecondSelection(view, position);
        }
    }


    private void handleFirstSelection(ImageView view, int position){
        mainActivity.setTitleWithTurns(++numberOfTurns);
        gameState = GameState.FIRST_CARD_SELECTED;
        firstSelectedPosition = position;
        firstSelectedCard = view;
        flipOver(firstSelectedCard, position, false);
    }


    private void handleSecondSelection(ImageView view, int position){
            if(position == firstSelectedPosition){
                return;
            }
            gameState = GameState.SECOND_CARD_SELECTED;
            secondSelectedPosition = position;
            secondSelectedCard = view;
            flipOver(secondSelectedCard, position, true);
    }


    private boolean matches(){
        Card card1 = cards.get(firstSelectedPosition);
        Card card2 = cards.get(secondSelectedPosition);
        return card1.getRank() == card2.getRank();
    }


    private String getStr(int resId){
        return context.getString(resId);
    }


    private void removeSelectedCards(){
        Handler handler = new Handler(Looper.getMainLooper());
        cardAnimator.addSwipeAnimationTo(firstSelectedCard);
        cardAnimator.addSwipeAnimationTo(secondSelectedCard);
        handler.postDelayed(() -> {
            remainingCards -=2;
            gameState = GameState.NOTHING_SELECTED;
            if(remainingCards <= 0){
                displayResults();
            }
        }, 1000);
    }


    private void displayResults(){
        String recordText;
        String numberOfTurnsStr = numberOfTurns + getStr(R.string.results_status_turns_taken);
        int currentRecord = recordKeeper.getCurrentTurnsRecordFromPreferences(numberOfCards);
        if(numberOfTurns < currentRecord){
            recordText = getStr(R.string.results_status_new_record);
            recordKeeper.saveNewTurnsRecord(numberOfTurns, numberOfCards);
        }
        else if(numberOfTurns == currentRecord){
            recordText = getStr(R.string.results_status_matching_record);
        }
        else{
            recordText = getStr(R.string.results_status_current_record) + currentRecord;
        }
        mainActivity.displayResults(numberOfTurnsStr, recordText);
    }


    public void startAgain(int numberOfCards){
        resetNumberOfTurns();
        this.numberOfCards = numberOfCards;
        cards = deck.get(numberOfCards);
        cardLayoutPopulator.addCardViews(numberOfCards);
        if(cards == null){
            return;
        }
        Collections.shuffle(cards);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(this::reset, 500);
    }


    private void reset(){
        gameState = GameState.NOTHING_SELECTED;
        remainingCards = numberOfCards;
        setAllCardsFaceDown();
        cardAnimator.swipeInAll(cardLayoutPopulator.getImageViews());
    }


    private void resetNumberOfTurns(){
        numberOfTurns = 0;
    }


    private void turnOverCards(){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            flipBack(firstSelectedCard);
            flipBack(secondSelectedCard);
            gameState = GameState.NOTHING_SELECTED;
        }, 1000);
    }


    private void setAllCardsFaceDown(){
        for(ImageView card : cardLayoutPopulator.getImageViews()){
            setCardFaceDown(card);
        }
    }


    private void setCardFaceDown(ImageView imageView){
        bitmapLoader.setBitmap(imageView, R.drawable.card_back_2);
    }


    private void flipOver(ImageView card, int position, boolean isSecondCardSelected){
        currentPosition = position;
        flipCard(card, isSecondCardSelected);
    }


    private void flipBack(ImageView card){
        flipCardBack(card);
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


    private void flipCard(ImageView card, boolean isSecondCard) {
        long duration = 130;
        float halfRotation = 90;

        Animator.AnimatorListener fullWayDone = new Animator.AnimatorListener() {
            public void onAnimationEnd(Animator animator) {
                card.clearAnimation();
                checkCards(isSecondCard);
            }
            public void onAnimationCancel(Animator animator) {}
            public void onAnimationRepeat(Animator animator) {}
            public void onAnimationStart(Animator animator) {}
        };

        Animator.AnimatorListener halfWayDone = new Animator.AnimatorListener() {
            public void onAnimationEnd(Animator animator) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(() -> {
                    card.clearAnimation();
                    int imageId = cards.get(currentPosition).getImageId();
                    bitmapLoader.setBitmap(card, imageId);
                    card.animate().rotationY(halfRotation * 2).setDuration(duration).setListener(fullWayDone).start();
                },10);
            }
            public void onAnimationStart(Animator animator) {}
            public void onAnimationCancel(Animator animator) {}
            public void onAnimationRepeat(Animator animator) {}
        };

        card.animate().rotationY(halfRotation).setDuration(duration).setListener(halfWayDone).start();
    }


    private void flipCardBack(ImageView card) {
        long duration = 160;
        float halfRotation = 90;

        Animator.AnimatorListener fullWayDone = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}
            @Override
            public void onAnimationEnd(Animator animator) {
                card.clearAnimation();
            }
            public void onAnimationCancel(Animator animator) {}
            public void onAnimationRepeat(Animator animator) { }
        };


        Animator.AnimatorListener halfWayDone = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}
            @Override
            public void onAnimationEnd(Animator animator) {
                if(cards == null){
                    return;
                }
                int imageId = cards.get(currentPosition).getImageId();
                bitmapLoader.setBitmap(card, imageId);

                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(() -> {
                    setCardFaceDown(card);
                    card.animate().rotationY(0).setDuration(duration).setListener(fullWayDone).start();
                },10);
            }
            public void onAnimationCancel(Animator animator) {}
            public void onAnimationRepeat(Animator animator) { }
        };

        card.animate().rotationY(halfRotation).setDuration(duration).setListener(halfWayDone).start();
    }


}
