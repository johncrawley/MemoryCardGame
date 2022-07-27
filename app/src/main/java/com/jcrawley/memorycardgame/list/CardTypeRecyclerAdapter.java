package com.jcrawley.memorycardgame.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jcrawley.memorycardgame.BitmapLoader;
import com.jcrawley.memorycardgame.R;
import com.jcrawley.memorycardgame.card.cardType.CardType;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardTypeRecyclerAdapter extends RecyclerView.Adapter<CardTypeRecyclerAdapter.CardTypeViewHolder> {

    private final List<CardType> cardTypes;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private View currentlySelectedView;
    private int indexToScrollTo = -1;
    private final BitmapLoader bitmapLoader;


    class CardTypeViewHolder extends RecyclerView.ViewHolder {

        ImageView cardTypeImageView;

        CardTypeViewHolder(View view) {
            super(view);
            cardTypeImageView = view.findViewById(R.id.itemImage);

            view.setOnClickListener(v -> {
                if(currentlySelectedView != null){
                    currentlySelectedView.setSelected(false);
                }
                currentlySelectedView = v;
                currentlySelectedView.setSelected(true);
                setIndexToScrollTo(getLayoutPosition());
                currentlySelectedView.setSelected(true);
            });
        }
    }


    public CardTypeRecyclerAdapter(List<CardType> cardTypes, BitmapLoader bitmapLoader){
        this.cardTypes = new ArrayList<>(cardTypes);
        this.bitmapLoader = bitmapLoader;
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
        bitmapLoader.setBitmap(holder.cardTypeImageView, cardTypes.get(position).getResourceId());
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