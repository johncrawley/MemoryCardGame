package com.jcrawley.memorycardgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

public class BitmapLoader {

    private final Context context;
    private final MainViewModel viewModel;

    public BitmapLoader(Context context, MainViewModel viewModel){
        this.context = context;
        this.viewModel = viewModel;
        if(viewModel.bitmapMap == null){
            viewModel.bitmapMap = new HashMap<>();
        }
    }

    public void setBitmap(ImageView imageView, int bitmapId){

        if(!viewModel.bitmapMap.containsKey(bitmapId)){
            viewModel.bitmapMap.put(bitmapId, BitmapFactory.decodeResource(context.getResources(), bitmapId));
        }
        imageView.setImageBitmap(viewModel.bitmapMap.get(bitmapId));
    }



}
