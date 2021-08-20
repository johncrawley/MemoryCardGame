package com.jcrawley.memorycardgame.card;

import android.graphics.Bitmap;

public class Card {
    private final Rank rank;
    private final Suit suit;
    private final int imageId;
    private boolean isDisplayed;
    private boolean isFaceDown;

    public Card(Rank rank, Suit suit, int imageId){
        this.rank = rank;
        this.suit = suit;
        this.imageId = imageId;
        this.isFaceDown = true;
    }

    public Rank getRank(){
        return rank;
    }

    public void setDisplayed(boolean displayed){
        this.isDisplayed = displayed;
    }


    public void flipCard(){
        this.isFaceDown = !isFaceDown;
    }


    public boolean isDisplayed(){
        return isDisplayed;
    }


    public boolean isFaceDown(){
        return isFaceDown;
    }

    public Suit getSuit(){
        return suit;
    }

    public int getImageId(){
        return imageId;
    }


}
