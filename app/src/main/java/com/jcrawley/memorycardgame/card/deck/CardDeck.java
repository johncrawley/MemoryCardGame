package com.jcrawley.memorycardgame.card.deck;

import com.jcrawley.memorycardgame.card.Card;
import com.jcrawley.memorycardgame.card.Rank;
import com.jcrawley.memorycardgame.card.Suit;

import java.util.Map;

public interface CardDeck {

    Map<String, Integer> getImageMap();
    void assign(Suit suit, Rank rank, int drawableId);
    int getImageIdFor(Card card);
}
