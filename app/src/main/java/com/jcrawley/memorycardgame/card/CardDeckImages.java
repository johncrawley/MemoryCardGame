package com.jcrawley.memorycardgame.card;

import com.jcrawley.memorycardgame.R;
import com.jcrawley.memorycardgame.card.cardType.CardType;

import java.util.HashMap;
import java.util.Map;

public class CardDeckImages {

    private final Map<CardType, Map<String, Integer>> decksMap;
    private Map<String, Integer> currentDeckMap;

    public CardDeckImages(){
        decksMap = new HashMap<>();
        addStandardDeck();
        addEasyReadDeck();
        currentDeckMap = decksMap.get(CardType.STANDARD);
    }


    public int getImageIdFor(Card card){
        String key = card.getSuit() + "_" + card.getRank();
        Integer imageId = currentDeckMap.get(key);
        if(imageId == null){
            return R.drawable.card_back_1;
        }
        return imageId;
    }


    public void setDeckType(CardType cardType){
        currentDeckMap = decksMap.get(cardType);
    }

    private void addStandardDeck(){
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
        suit = Suit.SPADES;
        add(deckMap, suit, Rank.TWO, R.drawable.card_spades_2_2);
        add(deckMap, suit, Rank.THREE, R.drawable.card_spades_3_2);
        add(deckMap, suit, Rank.FOUR, R.drawable.card_spades_4_2);
        add(deckMap, suit, Rank.FIVE, R.drawable.card_spades_5_2);
        add(deckMap, suit, Rank.SIX, R.drawable.card_spades_6_2);
        add(deckMap, suit, Rank.SEVEN, R.drawable.card_spades_7_2);
        add(deckMap, suit, Rank.EIGHT, R.drawable.card_spades_8_2);
        add(deckMap, suit, Rank.NINE, R.drawable.card_spades_9_2);
        add(deckMap, suit, Rank.TEN, R.drawable.card_spades_10_2);
        add(deckMap, suit, Rank.JACK, R.drawable.card_spades_jack_2);
        add(deckMap, suit, Rank.QUEEN, R.drawable.card_spades_queen_2);
        add(deckMap, suit, Rank.KING, R.drawable.card_spades_king_2);
        add(deckMap, suit, Rank.ACE, R.drawable.card_spades_ace_2);
        suit = Suit.HEARTS;
        add(deckMap, suit, Rank.TWO, R.drawable.card_hearts_2_2);
        add(deckMap, suit, Rank.THREE, R.drawable.card_hearts_3_2);
        add(deckMap, suit, Rank.FOUR, R.drawable.card_hearts_4_2);
        add(deckMap, suit, Rank.FIVE, R.drawable.card_hearts_5_2);
        add(deckMap, suit, Rank.SIX, R.drawable.card_hearts_6_2);
        add(deckMap, suit, Rank.SEVEN, R.drawable.card_hearts_7_2);
        add(deckMap, suit, Rank.EIGHT, R.drawable.card_hearts_8_2);
        add(deckMap, suit, Rank.NINE, R.drawable.card_hearts_9_2);
        add(deckMap, suit, Rank.TEN, R.drawable.card_hearts_10_2);
        add(deckMap, suit, Rank.JACK, R.drawable.card_hearts_jack_2);
        add(deckMap, suit, Rank.QUEEN, R.drawable.card_hearts_queen_2);
        add(deckMap, suit, Rank.KING, R.drawable.card_hearts_king_2);
        add(deckMap, suit, Rank.ACE, R.drawable.card_hearts_ace_2);
        suit = Suit.CLUBS;
        add(deckMap, suit, Rank.TWO, R.drawable.card_clubs_2_2);
        add(deckMap, suit, Rank.THREE, R.drawable.card_clubs_3_2);
        add(deckMap, suit, Rank.FOUR, R.drawable.card_clubs_4_2);
        add(deckMap, suit, Rank.FIVE, R.drawable.card_clubs_5_2);
        add(deckMap, suit, Rank.SIX, R.drawable.card_clubs_6_2);
        add(deckMap, suit, Rank.SEVEN, R.drawable.card_clubs_7_2);
        add(deckMap, suit, Rank.EIGHT, R.drawable.card_clubs_8_2);
        add(deckMap, suit, Rank.NINE, R.drawable.card_clubs_9_2);
        add(deckMap, suit, Rank.TEN, R.drawable.card_clubs_10_2);
        add(deckMap, suit, Rank.JACK, R.drawable.card_clubs_jack_2);
        add(deckMap, suit, Rank.QUEEN, R.drawable.card_clubs_queen_2);
        add(deckMap, suit, Rank.KING, R.drawable.card_clubs_king_2);
        add(deckMap, suit, Rank.ACE, R.drawable.card_clubs_ace_2);

        decksMap.put(CardType.STANDARD, deckMap);
    }


    private void addEasyReadDeck(){
        Map<String, Integer> deckMap = new HashMap<>();
        Suit suit = Suit.DIAMONDS;
        add(deckMap, suit, Rank.TWO, R.drawable.card_diamonds_2_mirror);
        add(deckMap, suit, Rank.THREE, R.drawable.card_diamonds_3_mirror);
        add(deckMap, suit, Rank.FOUR, R.drawable.card_diamonds_4_mirror);
        add(deckMap, suit, Rank.FIVE, R.drawable.card_diamonds_5_mirror);
        add(deckMap, suit, Rank.SIX, R.drawable.card_diamonds_6_mirror);
        add(deckMap, suit, Rank.SEVEN, R.drawable.card_diamonds_7_mirror);
        add(deckMap, suit, Rank.EIGHT, R.drawable.card_diamonds_8_mirror);
        add(deckMap, suit, Rank.NINE, R.drawable.card_diamonds_9_mirror);
        add(deckMap, suit, Rank.TEN, R.drawable.card_diamonds_10_mirror);
        add(deckMap, suit, Rank.JACK, R.drawable.card_diamonds_jack_mirror);
        add(deckMap, suit, Rank.QUEEN, R.drawable.card_diamonds_queen_mirror);
        add(deckMap, suit, Rank.KING, R.drawable.card_diamonds_king_mirror);
        add(deckMap, suit, Rank.ACE, R.drawable.card_diamonds_ace_mirror);
        suit = Suit.SPADES;
        add(deckMap, suit, Rank.TWO, R.drawable.card_spades_2_mirror);
        add(deckMap, suit, Rank.THREE, R.drawable.card_spades_3_mirror);
        add(deckMap, suit, Rank.FOUR, R.drawable.card_spades_4_mirror);
        add(deckMap, suit, Rank.FIVE, R.drawable.card_spades_5_mirror);
        add(deckMap, suit, Rank.SIX, R.drawable.card_spades_6_mirror);
        add(deckMap, suit, Rank.SEVEN, R.drawable.card_spades_7_mirror);
        add(deckMap, suit, Rank.EIGHT, R.drawable.card_spades_8_mirror);
        add(deckMap, suit, Rank.NINE, R.drawable.card_spades_9_mirror);
        add(deckMap, suit, Rank.TEN, R.drawable.card_spades_10_mirror);
        add(deckMap, suit, Rank.JACK, R.drawable.card_spades_jack_mirror);
        add(deckMap, suit, Rank.QUEEN, R.drawable.card_spades_queen_mirror);
        add(deckMap, suit, Rank.KING, R.drawable.card_spades_king_mirror);
        add(deckMap, suit, Rank.ACE, R.drawable.card_spades_ace_mirror);
        suit = Suit.HEARTS;
        add(deckMap, suit, Rank.TWO, R.drawable.card_hearts_2_mirror);
        add(deckMap, suit, Rank.THREE, R.drawable.card_hearts_3_mirror);
        add(deckMap, suit, Rank.FOUR, R.drawable.card_hearts_4_mirror);
        add(deckMap, suit, Rank.FIVE, R.drawable.card_hearts_5_mirror);
        add(deckMap, suit, Rank.SIX, R.drawable.card_hearts_6_mirror);
        add(deckMap, suit, Rank.SEVEN, R.drawable.card_hearts_7_mirror);
        add(deckMap, suit, Rank.EIGHT, R.drawable.card_hearts_8_mirror);
        add(deckMap, suit, Rank.NINE, R.drawable.card_hearts_9_mirror);
        add(deckMap, suit, Rank.TEN, R.drawable.card_hearts_10_mirror);
        add(deckMap, suit, Rank.JACK, R.drawable.card_hearts_jack_mirror);
        add(deckMap, suit, Rank.QUEEN, R.drawable.card_hearts_queen_mirror);
        add(deckMap, suit, Rank.KING, R.drawable.card_hearts_king_mirror);
        add(deckMap, suit, Rank.ACE, R.drawable.card_hearts_ace_mirror);
        suit = Suit.CLUBS;
        add(deckMap, suit, Rank.TWO, R.drawable.card_clubs_2_mirror);
        add(deckMap, suit, Rank.THREE, R.drawable.card_clubs_3_mirror);
        add(deckMap, suit, Rank.FOUR, R.drawable.card_clubs_4_mirror);
        add(deckMap, suit, Rank.FIVE, R.drawable.card_clubs_5_mirror);
        add(deckMap, suit, Rank.SIX, R.drawable.card_clubs_6_mirror);
        add(deckMap, suit, Rank.SEVEN, R.drawable.card_clubs_7_mirror);
        add(deckMap, suit, Rank.EIGHT, R.drawable.card_clubs_8_mirror);
        add(deckMap, suit, Rank.NINE, R.drawable.card_clubs_9_mirror);
        add(deckMap, suit, Rank.TEN, R.drawable.card_clubs_10_mirror);
        add(deckMap, suit, Rank.JACK, R.drawable.card_clubs_jack_mirror);
        add(deckMap, suit, Rank.QUEEN, R.drawable.card_clubs_queen_mirror);
        add(deckMap, suit, Rank.KING, R.drawable.card_clubs_king_mirror);
        add(deckMap, suit, Rank.ACE, R.drawable.card_clubs_ace_mirror);

        decksMap.put(CardType.SIMPLE, deckMap);
    }

    private void add(Map<String, Integer> deckMap, Suit suit, Rank rank, int id){
        deckMap.put(suit + "_" + rank, id);
    }

}
