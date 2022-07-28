package com.jcrawley.memorycardgame.card;

import android.widget.ImageView;

import com.jcrawley.memorycardgame.BitmapLoader;
import com.jcrawley.memorycardgame.MainViewModel;
import com.jcrawley.memorycardgame.R;
import com.jcrawley.memorycardgame.card.cardType.CardType;

import java.util.HashMap;
import java.util.Map;

public class CardBackManager implements CardTypeSetter {

    private final MainViewModel viewModel;
    private final Map<Integer, Integer> cardBackMap;
    private final BitmapLoader bitmapLoader;

    public CardBackManager(MainViewModel viewModel, BitmapLoader bitmapLoader){
        this.viewModel = viewModel;
        this.bitmapLoader = bitmapLoader;
        this.cardBackMap = new HashMap<>();
        cardBackMap.put(1, R.drawable.card_back_2);
    }


    public void setCardType(CardType cardType){
        viewModel.currentCardBackKey = cardType.getResourceId();
    }


    public void setCardBackTo(ImageView imageView){
        bitmapLoader.setBitmap(imageView, getCurrentCardBack());
    }


    private int getCurrentCardBack(){
        Integer drawableId = cardBackMap.get(viewModel.currentCardBackKey);
        return drawableId == null ?  R.drawable.card_back_1 : drawableId;
    }


}
