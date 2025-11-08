package com.jcrawley.memorycardgame.card.deck;

import static com.jcrawley.memorycardgame.card.CardType.STANDARD;
import static com.jcrawley.memorycardgame.card.Rank.ACE;
import static com.jcrawley.memorycardgame.card.Rank.EIGHT;
import static com.jcrawley.memorycardgame.card.Rank.FIVE;
import static com.jcrawley.memorycardgame.card.Rank.FOUR;
import static com.jcrawley.memorycardgame.card.Rank.JACK;
import static com.jcrawley.memorycardgame.card.Rank.KING;
import static com.jcrawley.memorycardgame.card.Rank.NINE;
import static com.jcrawley.memorycardgame.card.Rank.QUEEN;
import static com.jcrawley.memorycardgame.card.Rank.SEVEN;
import static com.jcrawley.memorycardgame.card.Rank.SIX;
import static com.jcrawley.memorycardgame.card.Rank.TEN;
import static com.jcrawley.memorycardgame.card.Rank.THREE;
import static com.jcrawley.memorycardgame.card.Rank.TWO;
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

    private final Map<CardType, Map<String, Integer>> decksMap;
    private final Map<CardType, CardDeck> deckMap;
    private Map<String, Integer> currentDeckMap;
    private CardDeck currentDeck;
    private final List<Rank> ranks = List.of(TWO,THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE);

    public CardFaceImages(){
        decksMap = new HashMap<>();
        deckMap = new HashMap<>();
        addStandardDeck();
        addEasyReadDeck();
        addCatDeck();
        addDogDeck();
        currentDeckMap = decksMap.get(STANDARD);
        currentDeck = deckMap.get(STANDARD);
    }


    public int getImageIdFor(Card card){
        return currentDeck.getImageIdFor(card);
    }


    public void setCardType(CardType cardType){
        currentDeckMap = decksMap.get(cardType);
    }


    private void addStandardDeck(){
        Map<String, Integer> deckMap = new HashMap<>();
        var deck = new CardDeckImpl();

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

        assign(deck, DIAMONDS, diamonds);
        assign(deck, HEARTS, hearts);
        assign(deck, CLUBS, clubs);
        assign(deck, SPADES, spades);

        decksMap.put(STANDARD, deckMap);
    }


    private void assign(CardDeck deck, Suit suit, List<Integer> ids){
        var ranks = Rank.values();
        for(int i = 0; i < ranks.length; i++){
            deck.assign(suit, ranks[i], ids.get(i) );
        }
    }


    private void addEasyReadDeck(){
        Map<String, Integer> deckMap = new HashMap<>();
        Suit suit = DIAMONDS;
        add(deckMap, suit, TWO,    R.drawable.card_face_simple_diamonds_2);
        add(deckMap, suit, THREE,  R.drawable.card_face_simple_diamonds_3);
        add(deckMap, suit, FOUR,   R.drawable.card_face_simple_diamonds_4);
        add(deckMap, suit, FIVE,   R.drawable.card_face_simple_diamonds_5);
        add(deckMap, suit, SIX,    R.drawable.card_face_simple_diamonds_6);
        add(deckMap, suit, SEVEN,  R.drawable.card_face_simple_diamonds_7);
        add(deckMap, suit, EIGHT,  R.drawable.card_face_simple_diamonds_8);
        add(deckMap, suit, NINE,   R.drawable.card_face_simple_diamonds_9);
        add(deckMap, suit, TEN,    R.drawable.card_face_simple_diamonds_10);
        add(deckMap, suit, JACK,   R.drawable.card_face_simple_diamonds_jack);
        add(deckMap, suit, Rank.QUEEN,  R.drawable.card_face_simple_diamonds_queen);
        add(deckMap, suit, Rank.KING,   R.drawable.card_face_simple_diamonds_king);
        add(deckMap, suit, ACE,    R.drawable.card_face_simple_diamonds_ace);
        suit = SPADES;
        add(deckMap, suit, TWO,    R.drawable.card_face_simple_spades_2);
        add(deckMap, suit, THREE,  R.drawable.card_face_simple_spades_3);
        add(deckMap, suit, FOUR,   R.drawable.card_face_simple_spades_4);
        add(deckMap, suit, FIVE,   R.drawable.card_face_simple_spades_5);
        add(deckMap, suit, SIX,    R.drawable.card_face_simple_spades_6);
        add(deckMap, suit, SEVEN,  R.drawable.card_face_simple_spades_7);
        add(deckMap, suit, EIGHT,  R.drawable.card_face_simple_spades_8);
        add(deckMap, suit, NINE,   R.drawable.card_face_simple_spades_9);
        add(deckMap, suit, TEN,    R.drawable.card_face_simple_spades_10);
        add(deckMap, suit, JACK,   R.drawable.card_face_simple_spades_jack);
        add(deckMap, suit, Rank.QUEEN,  R.drawable.card_face_simple_spades_queen);
        add(deckMap, suit, Rank.KING,   R.drawable.card_face_simple_spades_king);
        add(deckMap, suit, ACE,    R.drawable.card_face_simple_spades_ace);
        suit = HEARTS;
        add(deckMap, suit, TWO,    R.drawable.card_face_simple_hearts_2);
        add(deckMap, suit, THREE,  R.drawable.card_face_simple_hearts_3);
        add(deckMap, suit, FOUR,   R.drawable.card_face_simple_hearts_4);
        add(deckMap, suit, FIVE,   R.drawable.card_face_simple_hearts_5);
        add(deckMap, suit, SIX,    R.drawable.card_face_simple_hearts_6);
        add(deckMap, suit, SEVEN,  R.drawable.card_face_simple_hearts_7);
        add(deckMap, suit, EIGHT,  R.drawable.card_face_simple_hearts_8);
        add(deckMap, suit, NINE,   R.drawable.card_face_simple_hearts_9);
        add(deckMap, suit, TEN,    R.drawable.card_face_simple_hearts_10);
        add(deckMap, suit, JACK,   R.drawable.card_face_simple_hearts_jack);
        add(deckMap, suit, Rank.QUEEN,  R.drawable.card_face_simple_hearts_queen);
        add(deckMap, suit, Rank.KING,   R.drawable.card_face_simple_hearts_king);
        add(deckMap, suit, ACE,    R.drawable.card_face_simple_hearts_ace);
        suit = CLUBS;
        add(deckMap, suit, TWO,    R.drawable.card_face_simple_clubs_2);
        add(deckMap, suit, THREE,  R.drawable.card_face_simple_clubs_3);
        add(deckMap, suit, FOUR,   R.drawable.card_face_simple_clubs_4);
        add(deckMap, suit, FIVE,   R.drawable.card_face_simple_clubs_5);
        add(deckMap, suit, SIX,    R.drawable.card_face_simple_clubs_6);
        add(deckMap, suit, SEVEN,  R.drawable.card_face_simple_clubs_7);
        add(deckMap, suit, EIGHT,  R.drawable.card_face_simple_clubs_8);
        add(deckMap, suit, NINE,   R.drawable.card_face_simple_clubs_9);
        add(deckMap, suit, TEN,    R.drawable.card_face_simple_clubs_10);
        add(deckMap, suit, JACK,   R.drawable.card_face_simple_clubs_jack);
        add(deckMap, suit, Rank.QUEEN,  R.drawable.card_face_simple_clubs_queen);
        add(deckMap, suit, Rank.KING,   R.drawable.card_face_simple_clubs_king);
        add(deckMap, suit, ACE,    R.drawable.card_face_simple_clubs_ace);

        decksMap.put(CardType.SIMPLE, deckMap);
    }


    private void addCatDeck(){
        Map<Rank, Integer> drawableMap = new HashMap<>();
        drawableMap.put(TWO,    R.drawable.card_face_cat_2);
        drawableMap.put(THREE,  R.drawable.card_face_cat_3);
        drawableMap.put(FOUR,   R.drawable.card_face_cat_4);
        drawableMap.put(FIVE,   R.drawable.card_face_cat_5);
        drawableMap.put(SIX,    R.drawable.card_face_cat_6);
        drawableMap.put(SEVEN,  R.drawable.card_face_cat_7);
        drawableMap.put(EIGHT,  R.drawable.card_face_cat_8);
        drawableMap.put(NINE,   R.drawable.card_face_cat_9);
        drawableMap.put(TEN,    R.drawable.card_face_cat_10);
        drawableMap.put(JACK,   R.drawable.card_face_cat_jack);
        drawableMap.put(Rank.QUEEN,  R.drawable.card_face_cat_queen);
        drawableMap.put(Rank.KING,   R.drawable.card_face_cat_king);
        drawableMap.put(ACE,    R.drawable.card_face_cat_ace);
        decksMap.put(CardType.CAT, addForAllSuits(drawableMap));
    }


    private void addDogDeck(){
        Map<Rank, Integer> drawableMap = new HashMap<>();
        drawableMap.put(TWO,    R.drawable.card_face_dog_2);
        drawableMap.put(THREE,  R.drawable.card_face_dog_3);
        drawableMap.put(FOUR,   R.drawable.card_face_dog_4);
        drawableMap.put(FIVE,   R.drawable.card_face_dog_5);
        drawableMap.put(SIX,    R.drawable.card_face_dog_6);
        drawableMap.put(SEVEN,  R.drawable.card_face_dog_7);
        drawableMap.put(EIGHT,  R.drawable.card_face_dog_8);
        drawableMap.put(NINE,   R.drawable.card_face_dog_9);
        drawableMap.put(TEN,    R.drawable.card_face_dog_10);
        drawableMap.put(JACK,   R.drawable.card_face_dog_jack);
        drawableMap.put(Rank.QUEEN,  R.drawable.card_face_dog_queen);
        drawableMap.put(Rank.KING,   R.drawable.card_face_dog_king);
        drawableMap.put(ACE,    R.drawable.card_face_dog_ace);
        decksMap.put(CardType.DOG, addForAllSuits(drawableMap));
    }


    private void add(Map<String, Integer> deckMap, Suit suit, Rank rank, int id){
        deckMap.put(suit + "_" + rank, id);
    }


    private  Map<String, Integer> addForAllSuits(Map<Rank, Integer> drawableMap){
        Map<String, Integer> deckMap = new HashMap<>();
        for(Rank rank : drawableMap.keySet()){
            for(Suit suit : Suit.values()){
                Integer drawableId = drawableMap.get(rank);
                if(drawableId != null){
                    add(deckMap, suit, rank, drawableId);
                }
            }
        }
        return deckMap;
    }
}
