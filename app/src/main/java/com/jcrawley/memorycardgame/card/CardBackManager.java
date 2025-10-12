package com.jcrawley.memorycardgame.card;

import android.widget.ImageView;

import com.jcrawley.memorycardgame.view.utils.BitmapLoader;
import com.jcrawley.memorycardgame.view.MainViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CardBackManager implements CardTypeSetter {

    private final MainViewModel viewModel;
    private final BitmapLoader bitmapLoader;
    private List<CardType> usableCardBackTypes;
    private List<CardType> selectableCardBackTypes;
    private final Random random;

    public CardBackManager(MainViewModel viewModel, BitmapLoader bitmapLoader) {
        this.viewModel = viewModel;
        this.bitmapLoader = bitmapLoader;
        setupCardBackTypes();
        random = new Random(System.currentTimeMillis());

    }


    private void setupCardBackTypes(){
        CardType[] cardTypes = CardType.values();
        selectableCardBackTypes = new ArrayList<>(cardTypes.length);
        usableCardBackTypes = new ArrayList<>(cardTypes.length);

        for (CardType cardType : cardTypes) {
            addCardBackToLists(cardType);
        }
    }


    private void addCardBackToLists(CardType cardType){
        if(cardType.isCardBack()){
            selectableCardBackTypes.add(cardType);
            if(cardType.isUsable()){
                usableCardBackTypes.add(cardType);
            }
        }
    }


    public List<CardType> getSelectableCardBackTypes(){
        return selectableCardBackTypes;
    }


    public void setCardType(int savedTypeIndex){
       if(viewModel.isCardBackInitialised){
           return;
       }
       viewModel.isCardBackInitialised = true;
       List<CardType> cardBackTypes = getSelectableCardBackTypes();
       setCardType(cardBackTypes.get(savedTypeIndex));
    }


    public boolean isRandomEnabled(){
        return viewModel.isRandomCardBackEnabled;
    }


    public void setCardType(CardType cardType){
        if(cardType == CardType.BACK_RANDOM){
            handleRandomSelection();
            return;
        }
        viewModel.currentCardBackResourceId = cardType.getResourceId();
        viewModel.previouslySelectedCardTypeBack  = cardType;
        viewModel.isRandomCardBackEnabled = false;
    }


    public void chooseNewRandomBack(){
        if(isRandomEnabled()){
            setRandomCardBackType();
        }
    }


    private void handleRandomSelection(){
        if(viewModel.previouslySelectedCardTypeBack != CardType.BACK_RANDOM) {
            setRandomCardBackType();
            viewModel.previouslySelectedCardTypeBack  = CardType.BACK_RANDOM;
        }
        viewModel.isRandomCardBackEnabled = true;
    }


    private void setRandomCardBackType(){
        CardType randomCardBack;
        do{
            randomCardBack = getRandomCardBack();
        }
        while(randomCardBack == viewModel.previouslySelectedCardTypeBack);

        viewModel.previouslySelectedCardTypeBack  = randomCardBack;
        viewModel.currentCardBackResourceId = randomCardBack.getResourceId();
    }


    private CardType getRandomCardBack(){
        int randomIndex = random.nextInt(usableCardBackTypes.size());
        return usableCardBackTypes.get(randomIndex);
    }

    public void setCardBackOf(ImageView imageView){
        bitmapLoader.setCardBack(imageView, viewModel.currentCardBackResourceId);
    }

}
