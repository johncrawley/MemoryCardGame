package com.jcrawley.memorycardgame.card;

import java.util.ArrayList;
import java.util.List;
import static com.jcrawley.memorycardgame.card.Rank.*;
import static com.jcrawley.memorycardgame.card.Suit.*;

public class CardFactory {
    
    private static List<Card> cards;
    private static Suit suit;

    public static List<Card> createCards(){
        cards = new ArrayList<>(52);
        suit = CLUBS;
        add(TWO,0);
        add(THREE,0);
        add(FOUR,0);
        add(FIVE,0);
        add(SIX,0);
        add(SEVEN,0);
        add(EIGHT,0);
        add(NINE,0);
        add(TEN,0);
        add(JACK,0);
        add(QUEEN,0);
        add(KING,0);
        add(ACE,0);

        suit = DIAMONDS;
        add(TWO,0);
        add(THREE,0);
        add(FOUR,0);
        add(FIVE,0);
        add(SIX,0);
        add(SEVEN,0);
        add(EIGHT,0);
        add(NINE,0);
        add(TEN,0);
        add(JACK,0);
        add(QUEEN,0);
        add(KING,0);
        add(ACE,0);

        add(TWO,0);
        add(THREE,0);
        add(FOUR,0);
        add(FIVE,0);
        add(SIX,0);
        add(SEVEN,0);
        add(EIGHT,0);
        add(NINE,0);
        add(TEN,0);
        add(JACK,0);
        add(QUEEN,0);
        add(KING,0);
        add(ACE,0);

        suit = SPADES;
        add(TWO,0);
        add(THREE,0);
        add(FOUR,0);
        add(FIVE,0);
        add(SIX,0);
        add(SEVEN,0);
        add(EIGHT,0);
        add(NINE,0);
        add(TEN,0);
        add(JACK,0);
        add(QUEEN,0);
        add(KING,0);
        add(ACE,0);

        suit = HEARTS;
        add(TWO,0);
        add(THREE,0);
        add(FOUR,0);
        add(FIVE,0);
        add(SIX,0);
        add(SEVEN,0);
        add(EIGHT,0);
        add(NINE,0);
        add(TEN,0);
        add(JACK,0);
        add(QUEEN,0);
        add(KING,0);
        add(ACE,0);

        return new ArrayList<>(cards);
    }
    
    
    private static void add(Rank rank, int id){
        cards.add(new Card(rank,suit, id));
    }
    
}
