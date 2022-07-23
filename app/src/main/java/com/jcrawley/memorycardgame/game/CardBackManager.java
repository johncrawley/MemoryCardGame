package com.jcrawley.memorycardgame.game;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.jcrawley.memorycardgame.BitmapLoader;
import com.jcrawley.memorycardgame.MainViewModel;
import com.jcrawley.memorycardgame.R;

import java.util.HashMap;
import java.util.Map;

public class CardBackManager {

    private final MainViewModel viewModel;
    private final Map<Integer, Integer> cardBackMap;
    private final BitmapLoader bitmapLoader;

    public CardBackManager(MainViewModel viewModel, BitmapLoader bitmapLoader){
        this.viewModel = viewModel;
        this.bitmapLoader = bitmapLoader;
        this.cardBackMap = new HashMap<>();
        cardBackMap.put(1, R.drawable.card_back_2);
    }


    public void setCurrentCardBackId(int key){
        viewModel.currentCardBackKey = key;
    }


    public void setCardBackTo(ImageView imageView){
        bitmapLoader.setBitmap(imageView, getCurrentCardBack());
    }


    private int getCurrentCardBack(){
        Integer drawableId = cardBackMap.get(viewModel.currentCardBackKey);
        return drawableId == null ?  R.drawable.card_back_1 : drawableId;
    }


}
