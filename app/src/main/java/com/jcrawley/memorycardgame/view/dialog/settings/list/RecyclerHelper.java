package com.jcrawley.memorycardgame.view.dialog.settings.list;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jcrawley.memorycardgame.R;


public class RecyclerHelper {

    private int selectedPosition = RecyclerView.NO_POSITION;
    private View currentlySelectedView;
    private int indexToScrollTo = -1;


    void deselectPreviouslySelectedView(){
        if(currentlySelectedView != null){
            currentlySelectedView.setSelected(false);
            unhighlight(currentlySelectedView);
        }
    }


    void select(View v, int position){
        currentlySelectedView = v;
        currentlySelectedView.setSelected(true);
        highlight(currentlySelectedView);
        selectedPosition = position;
    }


    public void init(RecyclerView.Adapter<?> adapter, RecyclerView recyclerView, Context context){
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }


    @NonNull
    public View createViewForHolder(@NonNull ViewGroup parent, int itemResourceId){
        return LayoutInflater.from(parent.getContext()).inflate(itemResourceId, parent,false);
    }


    public void deselectCurrentlySelectedItem(){
        if(currentlySelectedView != null){
            currentlySelectedView.setSelected(false);
        }
    }


    public void changePositionTo(RecyclerView.Adapter<?> adapter, int newPosition){
        adapter.notifyItemChanged(selectedPosition);
        selectedPosition = newPosition;
        adapter.notifyItemChanged(selectedPosition);

    }


    public void selectItem(RecyclerView.ViewHolder holder, int position){
        holder.itemView.setSelected(selectedPosition == position);
        if(selectedPosition == position){
            highlight(holder.itemView);
        }
        else{
            unhighlight(holder.itemView);
        }
        if(position == indexToScrollTo){
            deselectCurrentlySelectedItem();
            currentlySelectedView = holder.itemView;
            currentlySelectedView.setSelected(true);
        }
    }


    public void setIndexToScrollTo(int index){
        this.indexToScrollTo = index;
    }


    private void highlight(View view){
        view.setBackgroundResource(R.drawable.settings_item_bg_selected);
    }


    private void unhighlight(View view){
        view.setBackgroundResource(R.drawable.settings_item_bg_unselected);
    }



}
