package com.jcrawley.memorycardgame.card;

public class Card {
    private final Rank rank;
    private final Suit suit;
    private boolean isVisible;
    private boolean isFaceDown;


    public Card(Rank rank, Suit suit){
        this.rank = rank;
        this.suit = suit;
        init();
    }


    public void init(){
        this.isVisible = true;
        this.isFaceDown = true;
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
