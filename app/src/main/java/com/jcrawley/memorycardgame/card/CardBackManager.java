package com.jcrawley.memorycardgame.card;

import android.widget.ImageView;

import com.jcrawley.memorycardgame.utils.BitmapLoader;
import com.jcrawley.memorycardgame.MainViewModel;
import com.jcrawley.memorycardgame.card.cardType.CardType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CardBackManager implements CardTypeSetter {

    private final MainViewModel viewModel;
    private final BitmapLoader bitmapLoader;
    private List<CardType> usableCardBackTypes;
    private List<CardType> selectableCardBackTypes;
    private boolean isRandomEnabled;
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


    public void setCardType(CardType cardType){
        if(cardType == CardType.BACK_RANDOM){
            if(!viewModel.isAlreadyInitialised || viewModel.previouslySelectedCardTypeBack != CardType.BACK_RANDOM) {
                setRandomCardBackType();
                viewModel.previouslySelectedCardTypeBack  = CardType.BACK_RANDOM;
            }
            isRandomEnabled = true;
            return;
        }
        viewModel.currentCardBackResourceId = cardType.getResourceId();
        viewModel.previouslySelectedCardTypeBack  = cardType;
        isRandomEnabled = false;
    }


    public void refreshCardBackType(){
        if(!isRandomEnabled){
            return;
        }
        setRandomCardBackType();
    }


    private void setRandomCardBackType(){
        CardType randomCardType = viewModel.previouslySelectedCardTypeBack ;
        while(randomCardType == viewModel.previouslySelectedCardTypeBack ){
            int randomIndex = random.nextInt(usableCardBackTypes.size());
           randomCardType = usableCardBackTypes.get(randomIndex);
        }
        viewModel.currentCardBackResourceId = randomCardType.getResourceId();
    }


    public void setCardBackTo(ImageView imageView){
        bitmapLoader.setCardBack(imageView, viewModel.currentCardBackResourceId);
    }

}
