package com.jcrawley.memorycardgame.list;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jcrawley.memorycardgame.BitmapLoader;
import com.jcrawley.memorycardgame.MainActivity;
import com.jcrawley.memorycardgame.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

public class BackgroundRecyclerAdapter extends RecyclerView.Adapter<BackgroundRecyclerAdapter.BackgroundViewHolder> {

    private final List<Background> backgrounds;
    private final BitmapLoader bitmapLoader;
    private final RecyclerHelper recyclerHelper;
    private MainActivity mainActivity;


    class BackgroundViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        Background background;

        BackgroundViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.itemImage);

            view.setOnClickListener(v -> {
                recyclerHelper.deselectPreviouslySelectedView();
                recyclerHelper.select(v);
                imageView = view.findViewById(R.id.itemImage);
                mainActivity.setBackground(background.getResourceId());
            });
        }
    }


    public BackgroundRecyclerAdapter(List<Background> backgrounds, BitmapLoader bitmapLoader){
        recyclerHelper = new RecyclerHelper();
        this.backgrounds = new ArrayList<>(backgrounds);
        this.bitmapLoader = bitmapLoader;
    }


    public void init(RecyclerView recyclerView, MainActivity mainActivity){
        recyclerHelper.init(this, recyclerView, mainActivity);
        this.mainActivity = mainActivity;
    }


    @Override
    @NonNull
    public BackgroundRecyclerAdapter.BackgroundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        return new BackgroundRecyclerAdapter.BackgroundViewHolder(recyclerHelper.createViewForHolder(parent, R.layout.image_list_item));
    }


    @Override
    public void onBindViewHolder(@NonNull BackgroundRecyclerAdapter.BackgroundViewHolder holder, int position){
        Background background = backgrounds.get(position);
        holder.background = background;
        holder.imageView.setBackground(AppCompatResources.getDrawable(mainActivity, background.getResourceId()));
        recyclerHelper.selectItem(holder, position);
    }


    @Override
    public int getItemCount(){
        return backgrounds.size();
    }


    private void changePositionTo(int newPosition){
        recyclerHelper.changePositionTo(this,newPosition);
    }


    public void setIndexToScrollTo(int index){
        recyclerHelper.setIndexToScrollTo(index);
    }



}