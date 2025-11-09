package com.jcrawley.memorycardgame.card.deck;

import static com.jcrawley.memorycardgame.card.CardType.CAT;
import static com.jcrawley.memorycardgame.card.CardType.DOG;
import static com.jcrawley.memorycardgame.card.CardType.EASY_READ;
import static com.jcrawley.memorycardgame.card.CardType.STANDARD;
import static com.jcrawley.memorycardgame.card.Suit.CLUBS;
import static com.jcrawley.memorycardgame.card.Suit.DIAMONDS;
import static com.jcrawley.memorycardgame.card.Suit.HEARTS;
import static com.jcrawley.memorycardgame.card.Suit.SPADES;

import com.jcrawley.memorycardgame.R;
import com.jcrawley.memorycardgame.card.Card;
import com.jcrawley.memorycardgame.card.CardType;
import com.jcrawley.memorycardgame.card.CardTypeSetter;
import com.jcrawley.memorycardgame.card.Rank;
import com.jcrawley.memorycardgame.card.Suit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardFaceImages implements CardTypeSetter {

    private final Map<CardType, CardDeck> deckMap;
    private CardDeck currentDeck;

    public CardFaceImages(){
        deckMap = new HashMap<>();
        addStandardDeck();
        addEasyReadDeck();
        addCatDeck();
        addDogDeck();
        currentDeck = deckMap.get(STANDARD);
    }


    public int getImageIdFor(Card card){
        return currentDeck.getImageIdFor(card);
    }


    public void setCardType(CardType cardType){
        currentDeck = deckMap.get(cardType);
    }


    private void addStandardDeck(){
        var diamonds = List.of(R.drawable.card_face_standard_diamonds_2,
                R.drawable.card_face_standard_diamonds_3,
                R.drawable.card_face_standard_diamonds_4,
                R.drawable.card_face_standard_diamonds_5,
                R.drawable.card_face_standard_diamonds_6,
                R.drawable.card_face_standard_diamonds_7,
                R.drawable.card_face_standard_diamonds_8,
                R.drawable.card_face_standard_diamonds_9,
                R.drawable.card_face_standard_diamonds_10,
                R.drawable.card_face_standard_diamonds_jack,
                R.drawable.card_face_standard_diamonds_king,
                R.drawable.card_face_standard_diamonds_queen,
                R.drawable.card_face_standard_diamonds_ace);

        var clubs = List.of(R.drawable.card_face_standard_clubs_2,
                R.drawable.card_face_standard_clubs_3,
                R.drawable.card_face_standard_clubs_4,
                R.drawable.card_face_standard_clubs_5,
                R.drawable.card_face_standard_clubs_6,
                R.drawable.card_face_standard_clubs_7,
                R.drawable.card_face_standard_clubs_8,
                R.drawable.card_face_standard_clubs_9,
                R.drawable.card_face_standard_clubs_10,
                R.drawable.card_face_standard_clubs_jack,
                R.drawable.card_face_standard_clubs_king,
                R.drawable.card_face_standard_clubs_queen,
                R.drawable.card_face_standard_clubs_ace);

        var hearts = List.of(R.drawable.card_face_standard_hearts_2,
                R.drawable.card_face_standard_hearts_3,
                R.drawable.card_face_standard_hearts_4,
                R.drawable.card_face_standard_hearts_5,
                R.drawable.card_face_standard_hearts_6,
                R.drawable.card_face_standard_hearts_7,
                R.drawable.card_face_standard_hearts_8,
                R.drawable.card_face_standard_hearts_9,
                R.drawable.card_face_standard_hearts_10,
                R.drawable.card_face_standard_hearts_jack,
                R.drawable.card_face_standard_hearts_king,
                R.drawable.card_face_standard_hearts_queen,
                R.drawable.card_face_standard_hearts_ace);

        var spades = List.of(R.drawable.card_face_standard_spades_2,
                R.drawable.card_face_standard_spades_3,
                R.drawable.card_face_standard_spades_4,
                R.drawable.card_face_standard_spades_5,
                R.drawable.card_face_standard_spades_6,
                R.drawable.card_face_standard_spades_7,
                R.drawable.card_face_standard_spades_8,
                R.drawable.card_face_standard_spades_9,
                R.drawable.card_face_standard_spades_10,
                R.drawable.card_face_standard_spades_jack,
                R.drawable.card_face_standard_spades_king,
                R.drawable.card_face_standard_spades_queen,
                R.drawable.card_face_standard_spades_ace);

        var deck = new CardDeckImpl();
        assign(deck, DIAMONDS, diamonds);
        assign(deck, HEARTS, hearts);
        assign(deck, CLUBS, clubs);
        assign(deck, SPADES, spades);
        deckMap.put(STANDARD, deck);
    }


    private void addEasyReadDeck(){
        var diamonds = List.of(R.drawable.card_face_simple_diamonds_2,
                R.drawable.card_face_simple_diamonds_3,
                R.drawable.card_face_simple_diamonds_4,
                R.drawable.card_face_simple_diamonds_5,
                R.drawable.card_face_simple_diamonds_6,
                R.drawable.card_face_simple_diamonds_7,
                R.drawable.card_face_simple_diamonds_8,
                R.drawable.card_face_simple_diamonds_9,
                R.drawable.card_face_simple_diamonds_10,
                R.drawable.card_face_simple_diamonds_jack,
                R.drawable.card_face_simple_diamonds_king,
                R.drawable.card_face_simple_diamonds_queen,
                R.drawable.card_face_simple_diamonds_ace);

        var clubs = List.of(R.drawable.card_face_simple_clubs_2,
                R.drawable.card_face_simple_clubs_3,
                R.drawable.card_face_simple_clubs_4,
                R.drawable.card_face_simple_clubs_5,
                R.drawable.card_face_simple_clubs_6,
                R.drawable.card_face_simple_clubs_7,
                R.drawable.card_face_simple_clubs_8,
                R.drawable.card_face_simple_clubs_9,
                R.drawable.card_face_simple_clubs_10,
                R.drawable.card_face_simple_clubs_jack,
                R.drawable.card_face_simple_clubs_king,
                R.drawable.card_face_simple_clubs_queen,
                R.drawable.card_face_simple_clubs_ace);

        var hearts = List.of(R.drawable.card_face_simple_hearts_2,
                R.drawable.card_face_simple_hearts_3,
                R.drawable.card_face_simple_hearts_4,
                R.drawable.card_face_simple_hearts_5,
                R.drawable.card_face_simple_hearts_6,
                R.drawable.card_face_simple_hearts_7,
                R.drawable.card_face_simple_hearts_8,
                R.drawable.card_face_simple_hearts_9,
                R.drawable.card_face_simple_hearts_10,
                R.drawable.card_face_simple_hearts_jack,
                R.drawable.card_face_simple_hearts_king,
                R.drawable.card_face_simple_hearts_queen,
                R.drawable.card_face_simple_hearts_ace);

        var spades = List.of(R.drawable.card_face_simple_spades_2,
                R.drawable.card_face_simple_spades_3,
                R.drawable.card_face_simple_spades_4,
                R.drawable.card_face_simple_spades_5,
                R.drawable.card_face_simple_spades_6,
                R.drawable.card_face_simple_spades_7,
                R.drawable.card_face_simple_spades_8,
                R.drawable.card_face_simple_spades_9,
                R.drawable.card_face_simple_spades_10,
                R.drawable.card_face_simple_spades_jack,
                R.drawable.card_face_simple_spades_king,
                R.drawable.card_face_simple_spades_queen,
                R.drawable.card_face_simple_spades_ace);

        var deck = new CardDeckImpl();
        assign(deck, DIAMONDS, diamonds);
        assign(deck, HEARTS, hearts);
        assign(deck, CLUBS, clubs);
        assign(deck, SPADES, spades);
        deckMap.put(EASY_READ, deck);
    }


    private void addCatDeck(){
        var cats = List.of(R.drawable.card_face_cat_2,
                R.drawable.card_face_cat_3,
                R.drawable.card_face_cat_4,
                R.drawable.card_face_cat_5,
                R.drawable.card_face_cat_6,
                R.drawable.card_face_cat_7,
                R.drawable.card_face_cat_8,
                R.drawable.card_face_cat_9,
                R.drawable.card_face_cat_10,
                R.drawable.card_face_cat_jack,
                R.drawable.card_face_cat_king,
                R.drawable.card_face_cat_queen,
                R.drawable.card_face_cat_ace);
        createSimpleDeck(CAT, cats);
    }


    private void addDogDeck(){
        var dogs = List.of(R.drawable.card_face_dog_2,
                R.drawable.card_face_dog_3,
                R.drawable.card_face_dog_4,
                R.drawable.card_face_dog_5,
                R.drawable.card_face_dog_6,
                R.drawable.card_face_dog_7,
                R.drawable.card_face_dog_8,
                R.drawable.card_face_dog_9,
                R.drawable.card_face_dog_10,
                R.drawable.card_face_dog_jack,
                R.drawable.card_face_dog_king,
                R.drawable.card_face_dog_queen,
                R.drawable.card_face_dog_ace);
        createSimpleDeck(DOG, dogs);
    }


    private void createSimpleDeck(CardType cardType, List<Integer> ids){
        var deck = new CardDeckImpl();
        assign(deck, ids);
        deckMap.put(cardType, deck);
    }


    private void assign(CardDeck deck, Suit suit, List<Integer> ids){
        var ranks = Rank.values();
        for(int i = 0; i < ranks.length; i++){
            deck.assign(suit, ranks[i], ids.get(i) );
        }
    }


    private void assign(CardDeck deck, List<Integer> ids){
        var ranks = Rank.values();
        for(int i = 0; i < ranks.length; i++){
            deck.assign(ranks[i], ids.get(i) );
        }
    }

}
