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
        add(TWO, R.drawable.card_diamonds_2_mirror);
        add(THREE, R.drawable.card_diamonds_3_mirror);
        add(FOUR, R.drawable.card_diamonds_4_mirror);
        add(FIVE, R.drawable.card_diamonds_5_mirror);
        suit = CLUBS;
        add(TWO, R.drawable.card_clubs_2_mirror);
        add(THREE, R.drawable.card_clubs_3_mirror);
        add(FOUR, R.drawable.card_clubs_4_mirror);
        add(FIVE, R.drawable.card_clubs_5_mirror);
        return cards;
    }


    public static List<Card> create16Cards(){

        cards = new ArrayList<>(16);
        suit = HEARTS;
        add(TWO, R.drawable.card_diamonds_2_mirror);
        add(THREE, R.drawable.card_diamonds_3_mirror);
        add(FOUR, R.drawable.card_diamonds_4_mirror);
        add(FIVE, R.drawable.card_diamonds_5_mirror);
        add(SIX, R.drawable.card_diamonds_6_mirror);
        add(SEVEN, R.drawable.card_diamonds_7_mirror);
        add(EIGHT, R.drawable.card_diamonds_8_mirror);
        add(NINE, R.drawable.card_diamonds_9_mirror);

        suit = SPADES;
        add(TWO, R.drawable.card_clubs_2_mirror);
        add(THREE, R.drawable.card_clubs_3_mirror);
        add(FOUR, R.drawable.card_clubs_4_mirror);
        add(FIVE, R.drawable.card_clubs_5_mirror);
        add(SIX, R.drawable.card_clubs_6_mirror);
        add(SEVEN, R.drawable.card_clubs_7_mirror);
        add(EIGHT, R.drawable.card_clubs_8_mirror);
        add(NINE, R.drawable.card_clubs_9_mirror);
        return cards;
    }


    public static List<Card> create26Cards(){
        cards = new ArrayList<>(26);
        addClubs();
        addDiamonds();
        return cards;
    }


    public static List<Card> create52Cards(){
        cards = new ArrayList<>(52);
        addClubs();
        addDiamonds();
        addSpades();
        addHearts();
        return new ArrayList<>(cards);
    }


    private static void addDiamonds(){
        suit = DIAMONDS;
        add(TWO, R.drawable.card_diamonds_2_mirror);
        add(THREE,R.drawable.card_diamonds_3_mirror);
        add(FOUR,R.drawable.card_diamonds_4_mirror);
        add(FIVE,R.drawable.card_diamonds_5_mirror);
        add(SIX,R.drawable.card_diamonds_6_mirror);
        add(SEVEN,R.drawable.card_diamonds_7_mirror);
        add(EIGHT,R.drawable.card_diamonds_8_mirror);
        add(NINE,R.drawable.card_diamonds_9_mirror);
        add(TEN,R.drawable.card_diamonds_10_mirror);
        add(JACK,R.drawable.card_diamonds_jack_mirror);
        add(QUEEN,R.drawable.card_diamonds_queen_mirror);
        add(KING,R.drawable.card_diamonds_king_mirror);
        add(ACE,R.drawable.card_diamonds_ace_mirror);
    }


    private static void addClubs(){
        suit = CLUBS;
        add(TWO, R.drawable.card_clubs_2_mirror);
        add(THREE,R.drawable.card_clubs_3_mirror);
        add(FOUR,R.drawable.card_clubs_4_mirror);
        add(FIVE,R.drawable.card_clubs_5_mirror);
        add(SIX,R.drawable.card_clubs_6_mirror);
        add(SEVEN,R.drawable.card_clubs_7_mirror);
        add(EIGHT,R.drawable.card_clubs_8_mirror);
        add(NINE,R.drawable.card_clubs_9_mirror);
        add(TEN,R.drawable.card_clubs_10_mirror);
        add(JACK,R.drawable.card_clubs_jack_mirror);
        add(QUEEN,R.drawable.card_clubs_queen_mirror);
        add(KING,R.drawable.card_clubs_king_mirror);
        add(ACE,R.drawable.card_clubs_ace_mirror);
    }


    private static void addSpades(){
        suit = SPADES;
        add(TWO, R.drawable.card_spades_2_mirror);
        add(THREE,R.drawable.card_spades_3_mirror);
        add(FOUR,R.drawable.card_spades_4_mirror);
        add(FIVE,R.drawable.card_spades_5_mirror);
        add(SIX,R.drawable.card_spades_6_mirror);
        add(SEVEN,R.drawable.card_spades_7_mirror);
        add(EIGHT,R.drawable.card_spades_8_mirror);
        add(NINE,R.drawable.card_spades_9_mirror);
        add(TEN,R.drawable.card_spades_10_mirror);
        add(JACK,R.drawable.card_spades_jack_mirror);
        add(QUEEN,R.drawable.card_spades_queen_mirror);
        add(KING,R.drawable.card_spades_king_mirror);
        add(ACE,R.drawable.card_spades_ace_mirror);
    }


    private static void addHearts(){
        suit = HEARTS;
        add(TWO, R.drawable.card_hearts_2_mirror);
        add(THREE,R.drawable.card_hearts_3_mirror);
        add(FOUR,R.drawable.card_hearts_4_mirror);
        add(FIVE,R.drawable.card_hearts_5_mirror);
        add(SIX,R.drawable.card_hearts_6_mirror);
        add(SEVEN,R.drawable.card_hearts_7_mirror);
        add(EIGHT,R.drawable.card_hearts_8_mirror);
        add(NINE,R.drawable.card_hearts_9_mirror);
        add(TEN,R.drawable.card_hearts_10_mirror);
        add(JACK,R.drawable.card_hearts_jack_mirror);
        add(QUEEN,R.drawable.card_hearts_queen_mirror);
        add(KING,R.drawable.card_hearts_king_mirror);
        add(ACE,R.drawable.card_hearts_ace_mirror);
    }


    private static void add(Rank rank, int id){
        cards.add(new Card(rank,suit, id));
    }
    
}
