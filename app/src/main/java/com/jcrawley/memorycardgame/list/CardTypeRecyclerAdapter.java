package com.jcrawley.memorycardgame.list;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jcrawley.memorycardgame.BitmapLoader;
import com.jcrawley.memorycardgame.MainActivity;
import com.jcrawley.memorycardgame.R;
import com.jcrawley.memorycardgame.card.CardTypeSetter;
import com.jcrawley.memorycardgame.card.cardType.CardType;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CardTypeRecyclerAdapter extends RecyclerView.Adapter<CardTypeRecyclerAdapter.CardTypeViewHolder> {

    private final List<CardType> cardTypes;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private View currentlySelectedView;
    private int indexToScrollTo = -1;
    private final BitmapLoader bitmapLoader;
    private final CardTypeSetter cardTypeSetter;
    private final int HIGHLIGHTED_COLOR;


    class CardTypeViewHolder extends RecyclerView.ViewHolder {

        ImageView cardTypeImageView;
        CardType cardType;

        CardTypeViewHolder(View view) {
            super(view);
            cardTypeImageView = view.findViewById(R.id.itemImage);

            view.setOnClickListener(v -> {
                if(currentlySelectedView != null){
                    currentlySelectedView.setSelected(false);
                    currentlySelectedView.setBackgroundColor(Color.TRANSPARENT);
                }

                currentlySelectedView = v;
                currentlySelectedView.setSelected(true);
                currentlySelectedView.setBackgroundColor(HIGHLIGHTED_COLOR);
                cardTypeImageView = view.findViewById(R.id.itemImage);
                cardTypeSetter.setCardType(cardType);
            });
        }
    }

    public void init(RecyclerView recyclerView, Context context){
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(this);
    }


    public CardTypeRecyclerAdapter(List<CardType> cardTypes, BitmapLoader bitmapLoader, CardTypeSetter cardTypeSetter){
        this.cardTypes = new ArrayList<>(cardTypes);
        this.bitmapLoader = bitmapLoader;
        this.cardTypeSetter = cardTypeSetter;
        HIGHLIGHTED_COLOR = Color.parseColor("#F57F17");
    }


    @Override
    @NonNull
    public CardTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list_item, parent,false);
        return new CardTypeViewHolder(view);
    }


    public void deselectCurrentlySelectedItem(){
        if(currentlySelectedView != null){
            System.out.println("TrackListAdapter.deselectCurrentlySelectedItem() - setting to false");
            currentlySelectedView.setSelected(false);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull CardTypeViewHolder holder, int position){
        CardType cardType = cardTypes.get(position);
        holder.cardType = cardTypes.get(position);
        bitmapLoader.setBitmap(holder.cardTypeImageView, cardType.getResourceId());
        holder.itemView.setSelected(selectedPosition == position);


        if(position == indexToScrollTo){
            deselectCurrentlySelectedItem();
            currentlySelectedView = holder.itemView;
            currentlySelectedView.setSelected(true);
        }
    }


    @Override
    public int getItemCount(){
        return cardTypes.size();
    }


    private void changePositionTo(int newPosition){
        notifyItemChanged(selectedPosition);
        selectedPosition = newPosition;
        notifyItemChanged(selectedPosition);
    }


    public void setIndexToScrollTo(int index){
        this.indexToScrollTo = index;
    }



}