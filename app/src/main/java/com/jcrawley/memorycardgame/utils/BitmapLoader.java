package com.jcrawley.memorycardgame.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.jcrawley.memorycardgame.MainViewModel;

import java.util.HashMap;
import java.util.Map;

public class BitmapLoader {

    private final Context context;
    private final MainViewModel viewModel;

    public BitmapLoader(Context context, MainViewModel viewModel){
        this.context = context;
        this.viewModel = viewModel;
        initMaps();
    }


    private void initMaps(){
        if(viewModel.cardFaceMap == null){
            viewModel.cardFaceMap = new HashMap<>();
        }
        if(viewModel.cardBackMap == null){
            viewModel.cardBackMap = new HashMap<>();
        }
    }


    public void setBitmap(ImageView imageView, int bitmapId){
        imageView.setImageBitmap(decode(bitmapId));
    }


    public void setCardFace(ImageView imageView, int bitmapId){
        setImage(imageView, bitmapId, viewModel.cardFaceMap);
    }


    public void setCardBack(ImageView imageView, int bitmapId){
        setImage(imageView, bitmapId, viewModel.cardBackMap);
    }


    private void setImage(ImageView imageView, int bitmapId, Map<Integer, Bitmap> bitmapMap){
        if(!bitmapMap.containsKey(bitmapId)){
            bitmapMap.put(bitmapId, decode(bitmapId));
        }
        imageView.setImageBitmap(bitmapMap.get(bitmapId));
    }

    public void clearCardFaceCache(){
        viewModel.cardFaceMap.clear();
    }


    public void clearCardBackCache(){
        viewModel.cardBackMap.clear();
    }


    private Bitmap decode(int bitmapId){
        return BitmapFactory.decodeResource(context.getResources(), bitmapId);
    }


}
