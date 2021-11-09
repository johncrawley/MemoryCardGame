package com.jcrawley.memorycardgame.game;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;

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
    private ImageView selectedView1, selectedView2;
    private enum GameState { NOTHING_SELECTED, FIRST_CARD_SELECTED, SECOND_CARD_SELECTED}
    private GameState gameState;
    private final BitmapLoader bitmapLoader;
    private CardLayoutPopulator cardLayoutPopulator;
    private final int NUMBER_OF_CARDS;
    private int remainingCards;


    public Game(Context context){
        bitmapLoader = new BitmapLoader(context);
        gameState = GameState.NOTHING_SELECTED;
        cards = CardFactory.createCards();
        NUMBER_OF_CARDS = cards.size();
        remainingCards = NUMBER_OF_CARDS;
        shuffleCards();
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
            gameState = GameState.FIRST_CARD_SELECTED;
            firstSelectedPosition = position;
            selectedView1 = view;
            bitmapLoader.setBitmap(selectedView1, cards.get(firstSelectedPosition).getImageId());
            return;
        }
        if(gameState == GameState.FIRST_CARD_SELECTED){

            if(position == firstSelectedPosition){
                return;
            }
            gameState = GameState.SECOND_CARD_SELECTED;
            secondSelectedPosition = position;
            selectedView2 = view;
            bitmapLoader.setBitmap(selectedView2, cards.get(secondSelectedPosition).getImageId());

            if(matches()){
                removeCards();
            }
            else{
                turnOverCards();
            }

        }
    }


    private boolean matches(){
        Card card1 = cards.get(firstSelectedPosition);
        Card card2 = cards.get(secondSelectedPosition);
        return card1.getRank() == card2.getRank();
    }


    private void removeCards(){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            selectedView1.setVisibility(View.INVISIBLE);
            selectedView2.setVisibility(View.INVISIBLE);
            remainingCards -=2;
            gameState = GameState.NOTHING_SELECTED;
            if(remainingCards <= 0){
                startAgain();
            }
        }, 1000);
    }


    private void startAgain(){
        Collections.shuffle(cards);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(this::reset, 2000);
    }


    private void turnOverCards(){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            setCardFaceDown(selectedView1);
            setCardFaceDown(selectedView2);
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
        bitmapLoader.setBitmap(imageView, R.drawable.card_back_1);
    }

}
