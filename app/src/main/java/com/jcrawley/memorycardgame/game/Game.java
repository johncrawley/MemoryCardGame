package com.jcrawley.memorycardgame.game;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcrawley.memorycardgame.BitmapLoader;
import com.jcrawley.memorycardgame.R;
import com.jcrawley.memorycardgame.card.Card;
import com.jcrawley.memorycardgame.card.CardFactory;

import java.util.Collections;
import java.util.List;

public class Game {

    private final List<Card> cards;
    private int firstSelectedPosition = -1;
    private int secondSelectedPosition = -1;
    private ImageView selectedCard1, selectedCard2;
    private enum GameState { NOTHING_SELECTED, FIRST_CARD_SELECTED, SECOND_CARD_SELECTED}
    private GameState gameState;
    private final BitmapLoader bitmapLoader;
    private CardLayoutPopulator cardLayoutPopulator;
    private final int NUMBER_OF_CARDS;
    private int remainingCards;
    private int numberOfTurns;
    private final TextView numberOfTurnsTextView;
    private boolean isFirstTurn = true;
    private final int screenWidth;


    public Game(Context context, TextView numberOfTurnsTextView, int screenWidth){
        bitmapLoader = new BitmapLoader(context);
        gameState = GameState.NOTHING_SELECTED;
        cards = CardFactory.createCards();
        NUMBER_OF_CARDS = cards.size();
        remainingCards = NUMBER_OF_CARDS;
        this.screenWidth = screenWidth;
        shuffleCards();
        this.numberOfTurnsTextView = numberOfTurnsTextView;
    }


    public void shuffleCards(){
        Collections.shuffle(cards);
    }


    public int getNumberOfCards(){
        return NUMBER_OF_CARDS;
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
        if(!isFirstTurn){
            numberOfTurns++;
            updateNumberOfTurns();
        }
        gameState = GameState.FIRST_CARD_SELECTED;
        firstSelectedPosition = position;
        selectedCard1 = view;
        bitmapLoader.setBitmap(selectedCard1, cards.get(firstSelectedPosition).getImageId());
    }


    private void handleSecondSelection(ImageView view, int position){
            if(position == firstSelectedPosition){
                return;
            }
            isFirstTurn = false;
            gameState = GameState.SECOND_CARD_SELECTED;
            secondSelectedPosition = position;
            selectedCard2 = view;
            bitmapLoader.setBitmap(selectedCard2, cards.get(secondSelectedPosition).getImageId());

            if(matches()){
                removeCards();
            }
            else{
                turnOverCards();
            }
    }


    private boolean matches(){
        Card card1 = cards.get(firstSelectedPosition);
        Card card2 = cards.get(secondSelectedPosition);
        return card1.getRank() == card2.getRank();
    }


    private void removeCards(){
        Handler handler = new Handler(Looper.getMainLooper());
        addSwipeAnimationTo(selectedCard1);
        addSwipeAnimationTo(selectedCard2);
        handler.postDelayed(() -> {
            remainingCards -=2;
            gameState = GameState.NOTHING_SELECTED;
           // selectedCard1.animate();
           // selectedCard2.animate();
            if(remainingCards <= 0){
                startAgain();
            }
        }, 1000);
    }

    private void log(String msg){
        System.out.println("^^^ Game: " + msg);
    }


    private void addSwipeAnimationTo(View card){
        float previousElevation = card.getElevation();
        card.setElevation(15);
        Animation animation = new TranslateAnimation(
                0,
                screenWidth + 100,
                0,
                0);
        animation.setDuration(1100);
        animation.setStartOffset(400);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                card.setVisibility(View.INVISIBLE);
                card.clearAnimation();
                card.setElevation(previousElevation);
            }
        });
        card.startAnimation(animation);
    }


    private void startAgain(){
        Collections.shuffle(cards);
        numberOfTurns = 1;
        isFirstTurn = true;
        updateNumberOfTurns();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(this::reset, 2000);
    }


    private void updateNumberOfTurns(){
        String turnsText = "Turn " + numberOfTurns;
        numberOfTurnsTextView.setText(turnsText);
    }


    private void turnOverCards(){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            setCardFaceDown(selectedCard1);
            setCardFaceDown(selectedCard2);
            gameState = GameState.NOTHING_SELECTED;
        }, 1000);
    }


    private void reset(){
        remainingCards = NUMBER_OF_CARDS;
        for(ImageView imageView : cardLayoutPopulator.getImageViews()){
            setCardFaceDown(imageView);
            imageView.setVisibility(View.VISIBLE);
        }
    }


    private void setCardFaceDown(ImageView imageView){
        bitmapLoader.setBitmap(imageView, R.drawable.card_back_2);
    }

}
