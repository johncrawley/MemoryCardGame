package com.jcrawley.memorycardgame.list;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jcrawley.memorycardgame.BitmapLoader;
import com.jcrawley.memorycardgame.GamePreferences;
import com.jcrawley.memorycardgame.R;
import com.jcrawley.memorycardgame.card.CardTypeSetter;
import com.jcrawley.memorycardgame.card.cardType.CardType;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardTypeRecyclerAdapter extends RecyclerView.Adapter<CardTypeRecyclerAdapter.CardTypeViewHolder> {

    private final List<CardType> cardTypes;
    private final BitmapLoader bitmapLoader;
    private final CardTypeSetter cardTypeSetter;
    private final Runnable onClickExtra;
    private final RecyclerHelper recyclerHelper;
    private String preferenceName;
    private GamePreferences gamePreferences;

    class CardTypeViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        CardType cardType;

        CardTypeViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.itemImage);

            view.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                recyclerHelper.deselectPreviouslySelectedView();
                recyclerHelper.select(v, position);
                imageView = view.findViewById(R.id.itemImage);
                cardTypeSetter.setCardType(cardType);
                gamePreferences.saveInt(preferenceName, position);
                onClickExtra.run();
            });
        }
    }


    public CardTypeRecyclerAdapter(List<CardType> cardTypes, BitmapLoader bitmapLoader, CardTypeSetter cardTypeSetter, Runnable onClickExtra){
        recyclerHelper = new RecyclerHelper();
        this.cardTypes = new ArrayList<>(cardTypes);
        this.bitmapLoader = bitmapLoader;
        this.cardTypeSetter = cardTypeSetter;
        this.onClickExtra = onClickExtra;
    }


    public void init(RecyclerView recyclerView, Context context, GamePreferences gamePreferences, String preferenceName){
        this.preferenceName = preferenceName;
        this.gamePreferences = gamePreferences;
        int savedPosition = getSavedPosition();
        recyclerHelper.init(this, recyclerView, context);
        changePositionTo(savedPosition);
        setIndexToScrollTo(savedPosition);
        setSavedCardType(savedPosition);
        recyclerView.scrollToPosition(savedPosition);
    }


    private void setSavedCardType(int savedPosition){
        CardType savedCardType = cardTypes.get(savedPosition);
        cardTypeSetter.setCardType(savedCardType);
    }


    public int getSavedPosition(){
        return Math.min(gamePreferences.getInt(preferenceName), cardTypes.size()-1);
    }


    @Override
    @NonNull
    public CardTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        return new CardTypeViewHolder(recyclerHelper.createViewForHolder(parent, R.layout.list_item_card));
    }


    @Override
    public void onBindViewHolder(@NonNull CardTypeViewHolder holder, int position){
        CardType cardType = cardTypes.get(position);
        holder.cardType = cardTypes.get(position);
        bitmapLoader.setBitmap(holder.imageView, cardType.getResourceId());
        recyclerHelper.selectItem(holder, position);
    }


    @Override
    public int getItemCount(){
        return cardTypes.size();
    }


    private void changePositionTo(int newPosition){
        recyclerHelper.changePositionTo(this, newPosition);
    }


    public void setIndexToScrollTo(int index){
        recyclerHelper.setIndexToScrollTo(index);
    }



}