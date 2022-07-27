package com.jcrawley.memorycardgame.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.jcrawley.memorycardgame.BitmapLoader;
import com.jcrawley.memorycardgame.R;
import com.jcrawley.memorycardgame.card.cardType.CardType;

import java.util.List;

import androidx.annotation.NonNull;

public class ImageListArrayAdapter extends ArrayAdapter<CardType> {

    private final Context context;
    private final List<CardType> items;
    private final BitmapLoader bitmapLoader;
    private final int itemViewId;

    public ImageListArrayAdapter(Context context, int itemViewId, List<CardType> items, BitmapLoader bitmapLoader){
        super(context, itemViewId, items);
        this.context = context;
        this.itemViewId = itemViewId;
        this.items = items;
        this.bitmapLoader = bitmapLoader;
    }


    @Override
    @NonNull
    public View getView(int position, View view, @NonNull ViewGroup parent){
        if(view == null){
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(itemViewId,null);
            ImageView imageView = view.findViewById(R.id.itemImage);
            CardType cardType = items.get(position);
            bitmapLoader.setBitmap(imageView, cardType.getResourceId());
        }

        return view;
    }

}


