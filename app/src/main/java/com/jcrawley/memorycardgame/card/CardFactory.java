package com.jcrawley.memorycardgame.card;

import com.jcrawley.memorycardgame.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jcrawley.memorycardgame.card.Rank.*;
import static com.jcrawley.memorycardgame.card.Suit.*;

public class CardFactory {
    
    private static List<Card> cards;
    private static Suit suit;

    public static Map<Integer, List<Card>> createDeck(){
        Map<Integer, List<Card>> deckMap = new HashMap<>(4);
        deckMap.put(8, create8Cards());
        deckMap.put(16, create16Cards());
        deckMap.put(26, create26Cards());
        deckMap.put(52, create52Cards());
        return deckMap;
    }


    public static List<Card> create8Cards(){
        cards = new ArrayList<>(8);
        add(TWO, R.drawable.card_diamonds_2);
        add(THREE, R.drawable.card_diamonds_3);
        add(FOUR, R.drawable.card_diamonds_4);
        add(FIVE, R.drawable.card_diamonds_5);
        suit = CLUBS;
        add(TWO, R.drawable.card_clubs_2);
        add(THREE, R.drawable.card_clubs_3);
        add(FOUR, R.drawable.card_clubs_4);
        add(FIVE, R.drawable.card_clubs_5);
        return cards;
    }


    public static List<Card> create16Cards(){

        cards = new ArrayList<>(16);
        suit = HEARTS;
        add(TWO, R.drawable.card_diamonds_2);
        add(THREE, R.drawable.card_diamonds_3);
        add(FOUR, R.drawable.card_diamonds_4);
        add(FIVE, R.drawable.card_diamonds_5);
        add(SIX, R.drawable.card_diamonds_6);
        add(SEVEN, R.drawable.card_diamonds_7);
        add(EIGHT, R.drawable.card_diamonds_8);
        add(NINE, R.drawable.card_diamonds_9);

        suit = SPADES;
        add(TWO, R.drawable.card_clubs_2);
        add(THREE, R.drawable.card_clubs_3);
        add(FOUR, R.drawable.card_clubs_4);
        add(FIVE, R.drawable.card_clubs_5);
        add(SIX, R.drawable.card_clubs_6);
        add(SEVEN, R.drawable.card_clubs_7);
        add(EIGHT, R.drawable.card_clubs_8);
        add(NINE, R.drawable.card_clubs_9);
        return cards;
    }


    public static List<Card> create26Cards(){
        cards = new ArrayList<>(26);
        suit = CLUBS;
        add(TWO, R.drawable.card_clubs_2);
        add(THREE,R.drawable.card_clubs_3);
        add(FOUR,R.drawable.card_clubs_4);
        add(FIVE,R.drawable.card_clubs_5);
        add(SIX,R.drawable.card_clubs_6);
        add(SEVEN,R.drawable.card_clubs_7);
        add(EIGHT,R.drawable.card_clubs_8);
        add(NINE,R.drawable.card_clubs_9);
        add(TEN,R.drawable.card_clubs_10);
        add(JACK,R.drawable.card_clubs_jack);
        add(QUEEN,R.drawable.card_clubs_queen);
        add(KING,R.drawable.card_clubs_king);
        add(ACE,R.drawable.card_clubs_ace);

        suit = DIAMONDS;
        add(TWO, R.drawable.card_diamonds_2);
        add(THREE,R.drawable.card_diamonds_3);
        add(FOUR,R.drawable.card_diamonds_4);
        add(FIVE,R.drawable.card_diamonds_5);
        add(SIX,R.drawable.card_diamonds_6);
        add(SEVEN,R.drawable.card_diamonds_7);
        add(EIGHT,R.drawable.card_diamonds_8);
        add(NINE,R.drawable.card_diamonds_9);
        add(TEN,R.drawable.card_diamonds_10);
        add(JACK,R.drawable.card_diamonds_jack);
        add(QUEEN,R.drawable.card_diamonds_queen);
        add(KING,R.drawable.card_diamonds_king);
        add(ACE,R.drawable.card_diamonds_ace);
        return cards;
    }




    public static List<Card> create52Cards(){
        cards = new ArrayList<>(52);
        suit = CLUBS;
        add(TWO, R.drawable.card_clubs_2);
        add(THREE,R.drawable.card_clubs_3);
        add(FOUR,R.drawable.card_clubs_4);
        add(FIVE,R.drawable.card_clubs_5);
        add(SIX,R.drawable.card_clubs_6);
        add(SEVEN,R.drawable.card_clubs_7);
        add(EIGHT,R.drawable.card_clubs_8);
        add(NINE,R.drawable.card_clubs_9);
        add(TEN,R.drawable.card_clubs_10);
        add(JACK,R.drawable.card_clubs_jack);
        add(QUEEN,R.drawable.card_clubs_queen);
        add(KING,R.drawable.card_clubs_king);
        add(ACE,R.drawable.card_clubs_ace);

        suit = DIAMONDS;
        add(TWO, R.drawable.card_diamonds_2);
        add(THREE,R.drawable.card_diamonds_3);
        add(FOUR,R.drawable.card_diamonds_4);
        add(FIVE,R.drawable.card_diamonds_5);
        add(SIX,R.drawable.card_diamonds_6);
        add(SEVEN,R.drawable.card_diamonds_7);
        add(EIGHT,R.drawable.card_diamonds_8);
        add(NINE,R.drawable.card_diamonds_9);
        add(TEN,R.drawable.card_diamonds_10);
        add(JACK,R.drawable.card_diamonds_jack);
        add(QUEEN,R.drawable.card_diamonds_queen);
        add(KING,R.drawable.card_diamonds_king);
        add(ACE,R.drawable.card_diamonds_ace);

        suit = SPADES;
        add(TWO, R.drawable.card_spades_2);
        add(THREE,R.drawable.card_spades_3);
        add(FOUR,R.drawable.card_spades_4);
        add(FIVE,R.drawable.card_spades_5);
        add(SIX,R.drawable.card_spades_6);
        add(SEVEN,R.drawable.card_spades_7);
        add(EIGHT,R.drawable.card_spades_8);
        add(NINE,R.drawable.card_spades_9);
        add(TEN,R.drawable.card_spades_10);
        add(JACK,R.drawable.card_spades_jack);
        add(QUEEN,R.drawable.card_spades_queen);
        add(KING,R.drawable.card_spades_king);
        add(ACE,R.drawable.card_spades_ace);

        suit = HEARTS;
        add(TWO, R.drawable.card_hearts_2);
        add(THREE,R.drawable.card_hearts_3);
        add(FOUR,R.drawable.card_hearts_4);
        add(FIVE,R.drawable.card_hearts_5);
        add(SIX,R.drawable.card_hearts_6);
        add(SEVEN,R.drawable.card_hearts_7);
        add(EIGHT,R.drawable.card_hearts_8);
        add(NINE,R.drawable.card_hearts_9);
        add(TEN,R.drawable.card_hearts_10);
        add(JACK,R.drawable.card_hearts_jack);
        add(QUEEN,R.drawable.card_hearts_queen);
        add(KING,R.drawable.card_hearts_king);
        add(ACE,R.drawable.card_hearts_ace);
        return new ArrayList<>(cards);
    }
    
    
    private static void add(Rank rank, int id){
        cards.add(new Card(rank,suit, id));
    }
    
}
