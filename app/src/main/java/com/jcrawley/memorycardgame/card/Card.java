package com.jcrawley.memorycardgame.card;

import java.util.concurrent.atomic.AtomicBoolean;

public class Card {
    private final Rank rank;
    private final Suit suit;
    private boolean isVisible;
    private boolean isFaceDown;
    private int position;
    private final AtomicBoolean isAvailable = new AtomicBoolean(true);


    public Card(Rank rank, Suit suit){
        this.rank = rank;
        this.suit = suit;
        init();
    }


    public void init(){
        this.isVisible = true;
        this.isFaceDown = true;
        isAvailable.set(true);
    }


    public void setPosition(int position){
        this.position = position;
    }


    public void setFaceDown(){
        isFaceDown = true;
    }


    public int getPosition(){
        return position;
    }


    public boolean isUnavailable(){
        return !isAvailable.get();
    }


    public void setUnavailable(){
        isAvailable.set(false);
    }


    public Rank getRank(){
        return rank;
    }

    public void setVisible(boolean visible){
        this.isVisible = visible;
    }


    public void flipCard(){
        this.isFaceDown = !isFaceDown;
    }


    public boolean isVisible(){
        return isVisible;
    }


    public boolean isFaceDown(){
        return isFaceDown;
    }

    public Suit getSuit(){
        return suit;
    }

}
