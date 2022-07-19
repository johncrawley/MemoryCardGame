package com.jcrawley.memorycardgame.card;

import com.jcrawley.memorycardgame.R;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CardDeckImages {

    public enum DeckType { STANDARD, EASY_READ }
    private Map<DeckType, Map<String, Integer>> decksMap;
    private DeckType currentDeckType;
    private Map<String, Integer> currentDeckMap;

    public CardDeckImages(){
        decksMap = new HashMap<>();

    }


    public int getImageIdFor(Card card){
        String key = card.getSuit() + "_" + card.getRank();
        Integer imageId = currentDeckMap.get(key);
        if(imageId == null){
            return R.drawable.card_back_1;
        }
        return imageId;
    }


    public void setDeckType(DeckType deckType){
        currentDeckMap = decksMap.get(deckType);
    }

    private void createStandardDeck(){
        Map<String, Integer> deckMap = new HashMap<>();
        Suit suit = Suit.DIAMONDS;
        add(deckMap, suit, Rank.TWO, R.drawable.card_diamonds_2_2);
        add(deckMap, suit, Rank.THREE, R.drawable.card_diamonds_3_2);
        add(deckMap, suit, Rank.FOUR, R.drawable.card_diamonds_4_2);
        add(deckMap, suit, Rank.FIVE, R.drawable.card_diamonds_5_2);
        add(deckMap, suit, Rank.SIX, R.drawable.card_diamonds_6_2);
        add(deckMap, suit, Rank.SEVEN, R.drawable.card_diamonds_7_2);
        add(deckMap, suit, Rank.EIGHT, R.drawable.card_diamonds_8_2);
        add(deckMap, suit, Rank.NINE, R.drawable.card_diamonds_9_2);
        add(deckMap, suit, Rank.TEN, R.drawable.card_diamonds_10_2);
        add(deckMap, suit, Rank.JACK, R.drawable.card_diamonds_jack_2);
        add(deckMap, suit, Rank.QUEEN, R.drawable.card_diamonds_queen_2);
        add(deckMap, suit, Rank.KING, R.drawable.card_diamonds_king_2);
        add(deckMap, suit, Rank.ACE, R.drawable.card_diamonds_ace_2);


    }

    private void add(Map<String, Integer> deckMap, Suit suit, Rank rank, int id){
        deckMap.put(suit + "_" + rank, id);
    }

}
