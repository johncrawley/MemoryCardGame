package com.jcrawley.memorycardgame.game;

import com.jcrawley.memorycardgame.card.Card;
import com.jcrawley.memorycardgame.card.CardFactory;

import java.util.Collections;
import java.util.List;

public class Game {

    List<Card> cards;
    int firstSelectedPosition = -1;
    int secondPosition = -1;


    public Game(){
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



}
