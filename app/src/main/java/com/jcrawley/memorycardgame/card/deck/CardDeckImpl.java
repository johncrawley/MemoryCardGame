package com.jcrawley.memorycardgame.card.deck;

import com.jcrawley.memorycardgame.R;
import com.jcrawley.memorycardgame.card.Card;
import com.jcrawley.memorycardgame.card.Rank;
import com.jcrawley.memorycardgame.card.Suit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardDeckImpl implements CardDeck{

    private final Map<String, Integer> imageMap;


    public CardDeckImpl(){
        imageMap = new HashMap<>();
    }


    @Override
    public Map<String, Integer> getImageMap() {
        return imageMap;
    }


    @Override
    public void assign(Suit suit, Rank rank, int drawableId) {
        var key = getKeyFor(suit, rank);
        imageMap.put(key, drawableId);
    }


    @Override
    public void assign(Rank rank, int drawableId) {
        assign(Suit.DIAMONDS, rank, drawableId);
        assign(Suit.HEARTS, rank, drawableId);
        assign(Suit.SPADES, rank, drawableId);
        assign(Suit.CLUBS, rank, drawableId);
    }


    @Override
    public int getImageIdFor(Card card){
        String key = getKeyFor(card.getSuit(), card.getRank());
        Integer imageId = imageMap.get(key);
        if(imageId == null){
            return R.drawable.card_back_kaleidoscope_red;
        }
        return imageId;
    }


    private String getKeyFor(Suit suit, Rank rank){
        return suit + "_" + rank;
    }
}
