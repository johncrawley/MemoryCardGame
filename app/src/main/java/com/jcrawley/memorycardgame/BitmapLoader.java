package com.jcrawley.memorycardgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

public class BitmapLoader {

    private final Context context;
    private final Map<Integer, Bitmap> bitmapMap;

    public BitmapLoader(Context context){
        this.context = context;
        bitmapMap = new HashMap<>();
    }

    public void setBitmap(ImageView imageView, int bitmapId){

        if(!bitmapMap.containsKey(bitmapId)){
            bitmapMap.put(bitmapId, BitmapFactory.decodeResource(context.getResources(), bitmapId));
        }
        imageView.setImageBitmap(bitmapMap.get(bitmapId));
    }



}
