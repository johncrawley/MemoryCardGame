package com.jcrawley.memorycardgame.game;

import android.content.Context;
import android.view.View;

import com.jcrawley.memorycardgame.R;
import com.jcrawley.memorycardgame.card.Card;
import com.jcrawley.memorycardgame.card.CardFactory;

import java.util.Collections;
import java.util.List;

public class Game {

    private List<Card> cards;
    private int firstSelectedPosition = -1;
    private int secondPosition = -1;
    private Context context;


    public Game(Context context){
        this.context = context;
        cards = CardFactory.createCards();
        shuffleCards();
    }


    public void shuffleCards(){
        Collections.shuffle(cards);
    }


    public int getCardDrawableIdForPosition(int position){
        return cards.get(position).getImageId();
    }


    public void check(int position1, int position2){

        if(cards.get(position1).getRank() == cards.get(position2).getRank()){

        }

    }

    public void notifyClickOnPosition(View view){
        int position = (int)view.getTag(R.string.position_tag);

    }



}
