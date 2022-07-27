package com.jcrawley.memorycardgame.list;

import android.content.Context;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jcrawley.memorycardgame.BitmapLoader;
import com.jcrawley.memorycardgame.R;
import com.jcrawley.memorycardgame.card.cardType.CardType;

import java.util.List;

import androidx.core.util.Consumer;

public class ImageListHelper {
    private final Context context;
    private ArrayAdapter<CardType> arrayAdapter;
    private final ListView listView;
    private final Consumer<CardType> clickConsumer;
    private final Consumer<CardType> longClickConsumer;
    private int selectedIndex;

    public ImageListHelper(Context context, ListView listView,
                           Consumer<CardType> clickConsumer,
                           Consumer<CardType> longClickConsumer){
        this.context = context;
        this.listView = listView;
        this.clickConsumer = clickConsumer;
        this.longClickConsumer = longClickConsumer;
    }


    public void setSelectedIndex(int selectedIndex){
        this.selectedIndex = Math.min(arrayAdapter.getCount()-1, selectedIndex);
    }


    public int getSelectedIndex(){
        return selectedIndex;
    }


    private CardType createDefaultCardType(){
        return CardType.SIMPLE;
    }


    public void setupList(final List<CardType> items, BitmapLoader bitmapLoader){
        this.arrayAdapter = new ImageListArrayAdapter(context, R.layout.image_list_item, items, bitmapLoader);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(createClickListener(items));
        listView.setOnItemLongClickListener(createLongClickListener(items));
    }


    private AdapterView.OnItemClickListener createClickListener(List<CardType> cardTypes){
        return (parent, view, position, id) -> {
            if(position >= cardTypes.size()){
                return;
            }
            selectedIndex = position;
            CardType cardType = cardTypes.get(position);
            clickConsumer.accept(cardType);
        };
    }


    private AdapterView.OnItemLongClickListener createLongClickListener(List<CardType> cardTypes){
        return (parent, view, position, id) -> {
            if(position < cardTypes.size()){
                CardType item = cardTypes.get(position);
                longClickConsumer.accept(item);
                return true;
            }
            return false;
        };
    }

}
