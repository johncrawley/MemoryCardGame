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
    private final BitmapLoader bitmapLoader;

    public CardBackManager(MainViewModel viewModel, BitmapLoader bitmapLoader){
        this.viewModel = viewModel;
        this.bitmapLoader = bitmapLoader;
    }


    public void setCardType(CardType cardType){
        viewModel.currentCardBackResourceId = cardType.getResourceId();
    }


    public void setCardBackTo(ImageView imageView){
        bitmapLoader.setBitmap(imageView, viewModel.currentCardBackResourceId);
    }

}
