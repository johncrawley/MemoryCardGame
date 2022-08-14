package com.jcrawley.memorycardgame.card;

import android.widget.ImageView;

import com.jcrawley.memorycardgame.BitmapLoader;
import com.jcrawley.memorycardgame.MainViewModel;
import com.jcrawley.memorycardgame.card.cardType.CardType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CardBackManager implements CardTypeSetter {

    private final MainViewModel viewModel;
    private final BitmapLoader bitmapLoader;
    private List<CardType> usableCardBackTypes;
    private List<CardType> selectableCardBackTypes;
    private boolean isRandomEnabled;
    private final Random random;
    private CardType previouslySelectedCardType = CardType.BACK_PLAIN_RED;

    public CardBackManager(MainViewModel viewModel, BitmapLoader bitmapLoader) {
        this.viewModel = viewModel;
        this.bitmapLoader = bitmapLoader;
        setupCardBackTypes();
        random = new Random(System.currentTimeMillis());
    }


    private void setupCardBackTypes(){
        selectableCardBackTypes = new ArrayList<>(Arrays.asList(CardType.values()));
        selectableCardBackTypes.remove(CardType.SIMPLE);
        selectableCardBackTypes.remove(CardType.STANDARD);

        usableCardBackTypes = new ArrayList<>(selectableCardBackTypes);
        usableCardBackTypes.remove(CardType.BACK_RANDOM);
    }


    public List<CardType> getSelectableCardBackTypes(){
        return selectableCardBackTypes;
    }


    public void setCardType(CardType cardType){
        if(cardType == CardType.BACK_RANDOM){
            setRandomCardBackType();
            isRandomEnabled = true;
            return;
        }
        viewModel.currentCardBackResourceId = cardType.getResourceId();
        previouslySelectedCardType = cardType;
        isRandomEnabled = false;
    }


    public void refreshCardBackType(){
        if(!isRandomEnabled){
            return;
        }
        setRandomCardBackType();
    }


    private void setRandomCardBackType(){
        CardType randomCardType = previouslySelectedCardType;
        while(randomCardType == previouslySelectedCardType){
            int randomIndex = random.nextInt(usableCardBackTypes.size());
           randomCardType = usableCardBackTypes.get(randomIndex);
        }
        viewModel.currentCardBackResourceId = randomCardType.getResourceId();
    }


    public void setCardBackTo(ImageView imageView){
        bitmapLoader.setBitmap(imageView, viewModel.currentCardBackResourceId);
    }

}
