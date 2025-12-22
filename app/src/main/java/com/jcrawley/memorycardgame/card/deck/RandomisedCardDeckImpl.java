package com.jcrawley.memorycardgame.card.deck;

import com.jcrawley.memorycardgame.card.Rank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomisedCardDeckImpl extends CardDeckImpl implements CardDeck{

    private final List<Integer> imageIds;

    public RandomisedCardDeckImpl(List<Integer> ids){
        imageIds = new ArrayList<>(ids);
        assignRandomIdsToRank();
    }


    @Override
    public void init(){
        assignRandomIdsToRank();
    }


    private void assignRandomIdsToRank(){
        var ranks = Rank.values();
        Collections.shuffle(imageIds);
        if(imageIds.size() < ranks.length){
            throw new RuntimeException("At least one deck has less than the minimum number of card images");
        }
        for(int i = 0; i < ranks.length; i++) {
            assign(ranks[i], imageIds.get(i));
        }

    }

}
