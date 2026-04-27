package com.jcrawley.memorycardgame.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;


public class CardFactory {

    private final List<Card> allCards;
    private Map<Rank, List<Card>> rankMap;
    private Random random;
    private final static Rank[] ranks = Rank.values();


    public CardFactory(){
       allCards = new ArrayList<>(52);
       setupCardMap();
    }


    private void setupCardMap(){
        rankMap = new HashMap<>(14);
        var ranks = Rank.values();
        for(var rank : ranks){
            var cards = new ArrayList<Card>();
            for(var suit : Suit.values()){
                var card = new Card(rank, suit);
                cards.add(card);
                allCards.add(card);
            }
            rankMap.put(rank, cards);
        }
    }


    public List<Card> getCards(int number){
        var listToAdd = number == allCards.size() ? allCards : getRandomCardsFromSuits(number);
        var cards = new ArrayList<>(listToAdd);
        Collections.shuffle(cards);
        init(cards);
        return cards;
    }


    private void init(List<Card> cards){
        for(int i = 0 ; i < cards.size(); i++){
            cards.get(i).setPosition(i);
            cards.get(i).init();
        }
    }


    private List<Card> getRandomCardsFromSuits(int number){
        var cards = new ArrayList<Card>();
        int numberOfPairs = number / 2;
        for(var rank : getRandomRanks(numberOfPairs)){
            var cardsInRank = rankMap.get(rank);
            assert(cardsInRank != null);
            if(number > 26){
                cards.addAll(cardsInRank);
                continue;
            }
            cards.addAll(getTwoRandomCardsFrom(cardsInRank));

            if(cards.size() == number){
                break;
            }
        }
        return cards;
    }


    private Set<Rank> getRandomRanks(int number){
        var rankSet = new HashSet<Rank>(number);
        initRandom();

        while(rankSet.size() < number && rankSet.size() < ranks.length){
            rankSet.add(ranks[getRandomRankIndex()]);
        }
        return rankSet;
    }


    private Set<Card> getTwoRandomCardsFrom(List<Card> cardsToChooseFrom){
        initRandom();
        var cards = new HashSet<Card>();
        while(cards.size() < 2){
            cards.add(cardsToChooseFrom.get(getRandomSuitIndex()));
        }
        return cards;
    }


    private void initRandom(){
        random = new Random(System.currentTimeMillis());
    }


    private int getRandomRankIndex(){
        return random.nextInt(Rank.values().length);
    }


    private int getRandomSuitIndex(){
        return random.nextInt(Suit.values().length);
    }
}
