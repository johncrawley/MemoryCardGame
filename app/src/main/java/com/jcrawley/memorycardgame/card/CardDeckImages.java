package com.jcrawley.memorycardgame.card;

import com.jcrawley.memorycardgame.R;
import com.jcrawley.memorycardgame.card.cardType.CardType;

import java.util.HashMap;
import java.util.Map;

public class CardDeckImages implements CardTypeSetter{

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


    public void setCardType(CardType cardType){
        currentDeckMap = decksMap.get(cardType);
    }

    private void addStandardDeck(){
        Map<String, Integer> deckMap = new HashMap<>();
        Suit suit = Suit.DIAMONDS;
        add(deckMap, suit, Rank.TWO,    R.drawable.card_face_standard_diamonds_2);
        add(deckMap, suit, Rank.THREE,  R.drawable.card_face_standard_diamonds_3);
        add(deckMap, suit, Rank.FOUR,   R.drawable.card_face_standard_diamonds_4);
        add(deckMap, suit, Rank.FIVE,   R.drawable.card_face_standard_diamonds_5);
        add(deckMap, suit, Rank.SIX,    R.drawable.card_face_standard_diamonds_6);
        add(deckMap, suit, Rank.SEVEN,  R.drawable.card_face_standard_diamonds_7);
        add(deckMap, suit, Rank.EIGHT,  R.drawable.card_face_standard_diamonds_8);
        add(deckMap, suit, Rank.NINE,   R.drawable.card_face_standard_diamonds_9);
        add(deckMap, suit, Rank.TEN,    R.drawable.card_face_standard_diamonds_10);
        add(deckMap, suit, Rank.JACK,   R.drawable.card_face_standard_diamonds_jack);
        add(deckMap, suit, Rank.QUEEN,  R.drawable.card_face_standard_diamonds_queen);
        add(deckMap, suit, Rank.KING,   R.drawable.card_face_standard_diamonds_king);
        add(deckMap, suit, Rank.ACE,    R.drawable.card_face_standard_diamonds_ace);
        suit = Suit.SPADES;
        add(deckMap, suit, Rank.TWO,    R.drawable.card_face_standard_spades_2);
        add(deckMap, suit, Rank.THREE,  R.drawable.card_face_standard_spades_3);
        add(deckMap, suit, Rank.FOUR,   R.drawable.card_face_standard_spades_4);
        add(deckMap, suit, Rank.FIVE,   R.drawable.card_face_standard_spades_5);
        add(deckMap, suit, Rank.SIX,    R.drawable.card_face_standard_spades_6);
        add(deckMap, suit, Rank.SEVEN,  R.drawable.card_face_standard_spades_7);
        add(deckMap, suit, Rank.EIGHT,  R.drawable.card_face_standard_spades_8);
        add(deckMap, suit, Rank.NINE,   R.drawable.card_face_standard_spades_9);
        add(deckMap, suit, Rank.TEN,    R.drawable.card_face_standard_spades_10);
        add(deckMap, suit, Rank.JACK,   R.drawable.card_face_standard_spades_jack);
        add(deckMap, suit, Rank.QUEEN,  R.drawable.card_face_standard_spades_queen);
        add(deckMap, suit, Rank.KING,   R.drawable.card_face_standard_spades_king);
        add(deckMap, suit, Rank.ACE,    R.drawable.card_face_standard_spades_ace);
        suit = Suit.HEARTS;
        add(deckMap, suit, Rank.TWO,    R.drawable.card_face_standard_hearts_2);
        add(deckMap, suit, Rank.THREE,  R.drawable.card_face_standard_hearts_3);
        add(deckMap, suit, Rank.FOUR,   R.drawable.card_face_standard_hearts_4);
        add(deckMap, suit, Rank.FIVE,   R.drawable.card_face_standard_hearts_5);
        add(deckMap, suit, Rank.SIX,    R.drawable.card_face_standard_hearts_6);
        add(deckMap, suit, Rank.SEVEN,  R.drawable.card_face_standard_hearts_7);
        add(deckMap, suit, Rank.EIGHT,  R.drawable.card_face_standard_hearts_8);
        add(deckMap, suit, Rank.NINE,   R.drawable.card_face_standard_hearts_9);
        add(deckMap, suit, Rank.TEN,    R.drawable.card_face_standard_hearts_10);
        add(deckMap, suit, Rank.JACK,   R.drawable.card_face_standard_hearts_jack);
        add(deckMap, suit, Rank.QUEEN,  R.drawable.card_face_standard_hearts_queen);
        add(deckMap, suit, Rank.KING,   R.drawable.card_face_standard_hearts_king);
        add(deckMap, suit, Rank.ACE,    R.drawable.card_face_standard_hearts_ace);
        suit = Suit.CLUBS;
        add(deckMap, suit, Rank.TWO,    R.drawable.card_face_standard_clubs_2);
        add(deckMap, suit, Rank.THREE,  R.drawable.card_face_standard_clubs_3);
        add(deckMap, suit, Rank.FOUR,   R.drawable.card_face_standard_clubs_4);
        add(deckMap, suit, Rank.FIVE,   R.drawable.card_face_standard_clubs_5);
        add(deckMap, suit, Rank.SIX,    R.drawable.card_face_standard_clubs_6);
        add(deckMap, suit, Rank.SEVEN,  R.drawable.card_face_standard_clubs_7);
        add(deckMap, suit, Rank.EIGHT,  R.drawable.card_face_standard_clubs_8);
        add(deckMap, suit, Rank.NINE,   R.drawable.card_face_standard_clubs_9);
        add(deckMap, suit, Rank.TEN,    R.drawable.card_face_standard_clubs_10);
        add(deckMap, suit, Rank.JACK,   R.drawable.card_face_standard_clubs_jack);
        add(deckMap, suit, Rank.QUEEN,  R.drawable.card_face_standard_clubs_queen);
        add(deckMap, suit, Rank.KING,   R.drawable.card_face_standard_clubs_king);
        add(deckMap, suit, Rank.ACE,    R.drawable.card_face_standard_clubs_ace);

        decksMap.put(CardType.STANDARD, deckMap);
    }


    private void addEasyReadDeck(){
        Map<String, Integer> deckMap = new HashMap<>();
        Suit suit = Suit.DIAMONDS;
        add(deckMap, suit, Rank.TWO,    R.drawable.card_face_simple_diamonds_2);
        add(deckMap, suit, Rank.THREE,  R.drawable.card_face_simple_diamonds_3);
        add(deckMap, suit, Rank.FOUR,   R.drawable.card_face_simple_diamonds_4);
        add(deckMap, suit, Rank.FIVE,   R.drawable.card_face_simple_diamonds_5);
        add(deckMap, suit, Rank.SIX,    R.drawable.card_face_simple_diamonds_6);
        add(deckMap, suit, Rank.SEVEN,  R.drawable.card_face_simple_diamonds_7);
        add(deckMap, suit, Rank.EIGHT,  R.drawable.card_face_simple_diamonds_8);
        add(deckMap, suit, Rank.NINE,   R.drawable.card_face_simple_diamonds_9);
        add(deckMap, suit, Rank.TEN,    R.drawable.card_face_simple_diamonds_10);
        add(deckMap, suit, Rank.JACK,   R.drawable.card_face_simple_diamonds_jack);
        add(deckMap, suit, Rank.QUEEN,  R.drawable.card_face_simple_diamonds_queen);
        add(deckMap, suit, Rank.KING,   R.drawable.card_face_simple_diamonds_king);
        add(deckMap, suit, Rank.ACE,    R.drawable.card_face_simple_diamonds_ace);
        suit = Suit.SPADES;
        add(deckMap, suit, Rank.TWO,    R.drawable.card_face_simple_spades_2);
        add(deckMap, suit, Rank.THREE,  R.drawable.card_face_simple_spades_3);
        add(deckMap, suit, Rank.FOUR,   R.drawable.card_face_simple_spades_4);
        add(deckMap, suit, Rank.FIVE,   R.drawable.card_face_simple_spades_5);
        add(deckMap, suit, Rank.SIX,    R.drawable.card_face_simple_spades_6);
        add(deckMap, suit, Rank.SEVEN,  R.drawable.card_face_simple_spades_7);
        add(deckMap, suit, Rank.EIGHT,  R.drawable.card_face_simple_spades_8);
        add(deckMap, suit, Rank.NINE,   R.drawable.card_face_simple_spades_9);
        add(deckMap, suit, Rank.TEN,    R.drawable.card_face_simple_spades_10);
        add(deckMap, suit, Rank.JACK,   R.drawable.card_face_simple_spades_jack);
        add(deckMap, suit, Rank.QUEEN,  R.drawable.card_face_simple_spades_queen);
        add(deckMap, suit, Rank.KING,   R.drawable.card_face_simple_spades_king);
        add(deckMap, suit, Rank.ACE,    R.drawable.card_face_simple_spades_ace);
        suit = Suit.HEARTS;
        add(deckMap, suit, Rank.TWO,    R.drawable.card_face_simple_hearts_2);
        add(deckMap, suit, Rank.THREE,  R.drawable.card_face_simple_hearts_3);
        add(deckMap, suit, Rank.FOUR,   R.drawable.card_face_simple_hearts_4);
        add(deckMap, suit, Rank.FIVE,   R.drawable.card_face_simple_hearts_5);
        add(deckMap, suit, Rank.SIX,    R.drawable.card_face_simple_hearts_6);
        add(deckMap, suit, Rank.SEVEN,  R.drawable.card_face_simple_hearts_7);
        add(deckMap, suit, Rank.EIGHT,  R.drawable.card_face_simple_hearts_8);
        add(deckMap, suit, Rank.NINE,   R.drawable.card_face_simple_hearts_9);
        add(deckMap, suit, Rank.TEN,    R.drawable.card_face_simple_hearts_10);
        add(deckMap, suit, Rank.JACK,   R.drawable.card_face_simple_hearts_jack);
        add(deckMap, suit, Rank.QUEEN,  R.drawable.card_face_simple_hearts_queen);
        add(deckMap, suit, Rank.KING,   R.drawable.card_face_simple_hearts_king);
        add(deckMap, suit, Rank.ACE,    R.drawable.card_face_simple_hearts_ace);
        suit = Suit.CLUBS;
        add(deckMap, suit, Rank.TWO,    R.drawable.card_face_simple_clubs_2);
        add(deckMap, suit, Rank.THREE,  R.drawable.card_face_simple_clubs_3);
        add(deckMap, suit, Rank.FOUR,   R.drawable.card_face_simple_clubs_4);
        add(deckMap, suit, Rank.FIVE,   R.drawable.card_face_simple_clubs_5);
        add(deckMap, suit, Rank.SIX,    R.drawable.card_face_simple_clubs_6);
        add(deckMap, suit, Rank.SEVEN,  R.drawable.card_face_simple_clubs_7);
        add(deckMap, suit, Rank.EIGHT,  R.drawable.card_face_simple_clubs_8);
        add(deckMap, suit, Rank.NINE,   R.drawable.card_face_simple_clubs_9);
        add(deckMap, suit, Rank.TEN,    R.drawable.card_face_simple_clubs_10);
        add(deckMap, suit, Rank.JACK,   R.drawable.card_face_simple_clubs_jack);
        add(deckMap, suit, Rank.QUEEN,  R.drawable.card_face_simple_clubs_queen);
        add(deckMap, suit, Rank.KING,   R.drawable.card_face_simple_clubs_king);
        add(deckMap, suit, Rank.ACE,    R.drawable.card_face_simple_clubs_ace);

        decksMap.put(CardType.SIMPLE, deckMap);
    }

    private void add(Map<String, Integer> deckMap, Suit suit, Rank rank, int id){
        deckMap.put(suit + "_" + rank, id);
    }

}
