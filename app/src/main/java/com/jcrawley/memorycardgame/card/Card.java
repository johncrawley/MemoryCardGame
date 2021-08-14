package com.jcrawley.memorycardgame.card;

public class Card {
    private final Rank rank;
    private final Suit suit;
    private final int imageId;

    public Card(Rank rank, Suit suit, int imageId){
        this.rank = rank;
        this.suit = suit;
        this.imageId = imageId;
    }

    public Rank getRank(){
        return rank;
    }

    public Suit getSuit(){
        return suit;
    }

    public int getImageId(){
        return imageId;
    }


}
