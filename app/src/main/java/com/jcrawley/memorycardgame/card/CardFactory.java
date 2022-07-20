package com.jcrawley.memorycardgame.card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jcrawley.memorycardgame.card.Rank.*;
import static com.jcrawley.memorycardgame.card.Suit.*;

public class CardFactory {
    
    private static List<Card> cards;
    private static Suit suit;


    public static Map<DeckSize, List<Card>> createDecks(){
        Map<DeckSize, List<Card>> deckMap = new HashMap<>(4);
        deckMap.put(DeckSize.EIGHT, create8Cards());
        deckMap.put(DeckSize.SIXTEEN, create16Cards());
        deckMap.put(DeckSize.TWENTY_SIX, create26Cards());
        deckMap.put(DeckSize.FIFTY_TWO, create52Cards());
        return deckMap;
    }


    public static List<Card> create8Cards(){
        cards = new ArrayList<>(8);
        suit = DIAMONDS;
        add(TWO);
        add(THREE);
        add(FOUR);
        add(FIVE);
        suit = CLUBS;
        add(TWO);
        add(THREE);
        add(FOUR);
        add(FIVE);
        return cards;
    }


    public static List<Card> create16Cards(){

        cards = new ArrayList<>(16);
        suit = HEARTS;
        add(TWO);
        add(THREE);
        add(FOUR);
        add(FIVE);
        add(SIX);
        add(SEVEN);
        add(EIGHT);
        add(NINE);

        suit = SPADES;
        add(TWO);
        add(THREE);
        add(FOUR);
        add(FIVE);
        add(SIX);
        add(SEVEN);
        add(EIGHT);
        add(NINE);
        return cards;
    }


    public static List<Card> create26Cards(){
        cards = new ArrayList<>(26);
        addAllRanks(CLUBS);
        addAllRanks(DIAMONDS);
        return cards;
    }


    public static List<Card> create52Cards(){
        cards = new ArrayList<>(52);
        addAllRanks(HEARTS);
        addAllRanks(CLUBS);
        addAllRanks(SPADES);
        addAllRanks(DIAMONDS);
        return new ArrayList<>(cards);
    }



    private static void addAllRanks(Suit suit1){
        suit = suit1;
        add(TWO);
        add(THREE);
        add(FOUR);
        add(FIVE);
        add(SIX);
        add(SEVEN);
        add(EIGHT);
        add(NINE);
        add(TEN);
        add(JACK);
        add(QUEEN);
        add(KING);
        add(ACE);
    }


    private static void add(Rank rank){
        cards.add(new Card(rank,suit));
    }
    
}
