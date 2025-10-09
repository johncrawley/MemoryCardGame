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
        Rank[] ranks = Rank.values();
        for(Rank rank : ranks){
            List<Card> cards = new ArrayList<>();
            for(Suit suit : Suit.values()){
                Card card = new Card(rank, suit);
                cards.add(card);
                allCards.add(card);
            }
            rankMap.put(rank, cards);
        }
    }


    public List<Card> getCards(int number){
        List<Card> listToAdd = number == allCards.size() ? allCards : getRandomCardsFromSuits(number);
        List<Card> cards = new ArrayList<>(listToAdd);
        log("getCards() size: " + cards.size());
        Collections.shuffle(cards);
        init(cards);
        return cards;
    }


    private void log(String msg){
        System.out.println("^^^ CardFactory: " + msg);
    }

    private void init(List<Card> cards){
        for(int i = 0 ; i < cards.size(); i++){
            cards.get(i).setPosition(i);
            cards.get(i).init();
        }
    }


    private List<Card> getRandomCardsFromSuits(int number){
        List<Card> cards = new ArrayList<>();
        int numberOfPairs = number / 2;
        for(Rank rank : getRandomRanks(numberOfPairs)){
            List<Card> cardsInRank = rankMap.get(rank);
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
        Set<Rank> rankSet = new HashSet<>(number);
        initRandom();

        while(rankSet.size() < number && rankSet.size() < ranks.length){
            rankSet.add(ranks[getRandomRankIndex()]);
        }
        return rankSet;
    }


    private Set<Card> getTwoRandomCardsFrom(List<Card> cardsToChooseFrom){
        initRandom();
        Set<Card> cards = new HashSet<>();
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
