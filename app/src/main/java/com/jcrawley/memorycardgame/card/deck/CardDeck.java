package com.jcrawley.memorycardgame.card.deck;

import com.jcrawley.memorycardgame.card.Card;


public interface CardDeck {

    int getImageIdFor(Card card);
    void init();
}
